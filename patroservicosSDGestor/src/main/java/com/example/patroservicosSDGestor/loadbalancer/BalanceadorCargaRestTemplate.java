package com.example.patroservicosSDGestor.loadbalancer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
// import java.net.URI; (removido; não utilizado)

/**
 * Wrapper com balanceamento e retry automático.
 * Se uma instância falhar, tenta a próxima (até MAX_RETRIES vezes).
 * (Se mudar MAX_RETRIES para 1 = sem retry; 5+ = muito resiliente mas lento)
 */
@Component
public class BalanceadorCargaRestTemplate {
    private final RestTemplate restTemplate;
    private final BalanceadorCarga loadBalancer;
    private final ServiceRegistry serviceRegistry;

    private static final int MAX_RETRIES = 5;

    public BalanceadorCargaRestTemplate(RestTemplate restTemplate, BalanceadorCarga loadBalancer, ServiceRegistry serviceRegistry) {
        this.restTemplate = restTemplate;
        this.loadBalancer = loadBalancer;
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * GET com balanceamento automático e retry.
     */
    public <T> ResponseEntity<T> obterEntidade(String path, Class<T> responseType) {
        return executarComRetry((url) -> restTemplate.getForEntity(url, responseType), path, "GET");
    }

    /**
     * POST com balanceamento automático e retry.
     */
    public <T> ResponseEntity<T> postParaEntidade(String path, HttpEntity<?> request, Class<T> responseType) {
        return executarComRetry((url) -> restTemplate.postForEntity(url, request, responseType), path, "POST");
    }

    /**
     * PUT com balanceamento automático e retry.
     */
    public <T> ResponseEntity<T> trocar(String path, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
        return executarComRetry((url) -> restTemplate.exchange(url, method, request, responseType), path, method.toString());
    }

    /**
     * Executa com retry automático: se falhar em uma instância, tenta na próxima.
     */
    private <T> ResponseEntity<T> executarComRetry(ExecutorRequisicao<T> executor, String path, String method) {
        int attempts = 0;
        Exception lastException = null;

        while (attempts < MAX_RETRIES) {
            ServiceInstance chosen = null;
            try {
                // Decide qual serviço deve atender este path
                String serviceKey = escolherServicoParaPath(path);

                // Pega próxima instância do balanceador para o serviço escolhido (rodízio)
                chosen = loadBalancer.obterProximaInstancia(serviceKey);
                String fullUrl = chosen.obterBaseUrl() + path;

                System.out.println("[Roteador] Tentativa " + (attempts + 1) + " → " + chosen.obterID());

                // Executa na instância escolhida
                ResponseEntity<T> response = executor.executar(fullUrl);

                // SUCESSO: registra sucesso
                serviceRegistry.registrarSucesso(chosen.obterID());
                System.out.println("[Roteador] O " + method + " '" + path + "' foi atendido pelo serviço '" + chosen.obterID() + "'");
                return response;

            } catch (Exception e) {
                // Se for um erro 4xx (erro do cliente), não faz retry: rethrow imediatamente.
                if (e instanceof HttpStatusCodeException) {
                    HttpStatusCodeException httpEx = (HttpStatusCodeException) e;
                    if (httpEx.getStatusCode().is4xxClientError()) {
                        // registra falha na instância atual e repassa a exceção (cliente precisa corrigir)
                        if (chosen != null) {
                            try { serviceRegistry.registrarFalha(chosen.obterID()); } catch (Exception ignored) {}
                        }
                        throw new RuntimeException(e);
                    }
                }

                // FALHA: tenta próxima instância
                lastException = e;
                attempts++;
                System.out.println("[Roteador] ✗ Tentativa " + attempts + " falhou");

                // Registra a falha
                if (chosen != null) {
                    try {
                        serviceRegistry.registrarFalha(chosen.obterID());
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        throw new RuntimeException("Todas as instâncias falharam após " + MAX_RETRIES + " tentativas", lastException);
    }

    /**
     * Mapeia prefixos de path para a chave de serviço registrada no service registry.
     * Aqui mantemos um mapeamento simples e explícito. Se adicionar novos endpoints,
     * atualize este método.
     */
    private String escolherServicoParaPath(String path) {
        if (path == null) return "sd1";

        // Clientes (SD2)
        if (path.startsWith("/api/clientes") || path.startsWith("/api/usuarios")) {
            return "sd2";
        }

        // Perfil de cliente (SD3)
        if (path.startsWith("/api/perfil-cliente") || path.startsWith("/api/perfil-clientes") || path.startsWith("/api/perfil")) {
            return "sd3";
        }

        // Profissionais e demais endpoints padrão ficam em SD1
        return "sd1";
    }

    @FunctionalInterface
    private interface ExecutorRequisicao<T> {
        ResponseEntity<T> executar(String url) throws Exception;
    }


}
