package com.example.patroservicosSDGestor.loadbalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import java.time.Instant;

/**
 * Faz health check periódico (a cada 10 segundos) em todas as instâncias.
 * Se responde: marca ativa. Se não responde: incrementa falhas e pode inativar.
 * (Ajuste fixedDelay para 3000ms em testes, 30000ms em produção pesada)
 */
@Component
@EnableScheduling
public class VerificarSaude {
    private final ServiceRegistry serviceRegistry;
    private final RestTemplate restTemplate;

    @Value("${servicos.sd1.base-url}")
    private String sd1BaseUrl;

    @Value("${servicos.sd1replica.base-url:http://localhost:8085}")
    private String sd1ReplicaBaseUrl;

    @Value("${servicos.sd2.base-url:http://localhost:8083}")
    private String sd2BaseUrl;

    @Value("${servicos.sd3.base-url:http://localhost:8084}")
    private String sd3BaseUrl;

    private static final String HEALTH_CHECK_PATH = "/api/status";

    public VerificarSaude(ServiceRegistry serviceRegistry, RestTemplate restTemplate) {
        this.serviceRegistry = serviceRegistry;
        this.restTemplate = restTemplate;
    }

    /**
     * Inicializa automaticamente após a construção do bean.
     */
    @PostConstruct
    public void inicializarInstancias() {
        System.out.println("[HealthChecker] Inicializando instâncias do balanceador...");
        serviceRegistry.registrarInstancia("sd1", sd1BaseUrl);
        serviceRegistry.registrarInstancia("sd1replica", sd1ReplicaBaseUrl);
        // Registra também sd2 e sd3 (se configurados no application.properties)
        serviceRegistry.registrarInstancia("sd2", sd2BaseUrl);
        serviceRegistry.registrarInstancia("sd3", sd3BaseUrl);
        System.out.println("[HealthChecker] Instâncias registradas:");
        serviceRegistry.exibirStatus();
        
        // Executa verificação imediata para marcar quais instâncias estão disponíveis
        System.out.println("[HealthChecker] Executando verificação imediata de saúde...");
        executarVerificacaoSaude();
    }

    /**
     * Verifica saúde de cada instância a cada 5 segundos (ajustável para detecção mais rápida).
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 2000)
    public void executarVerificacaoSaude() {
        System.out.println("\n[HealthChecker] Iniciando verificação de saúde...");
        
        for (ServiceInstance instance : serviceRegistry.obterTodasInstancias()) {
            verificarInstancia(instance);
        }
        
        serviceRegistry.exibirStatus();
    }

    /**
     * Verifica saúde de uma instância fazendo GET /api/status.
     */
    private void verificarInstancia(ServiceInstance instance) {
        String healthUrl = instance.obterBaseUrl() + HEALTH_CHECK_PATH;
        
        try {
            // Tenta chamar o endpoint de status
            restTemplate.getForObject(healthUrl, String.class);
            
            // SUCESSO: zera falhas
            serviceRegistry.registrarSucesso(instance.obterID());
            if (!instance.estaAtiva()) {
                System.out.println("[HealthChecker] ✓ " + instance.obterID() + " voltou a responder!");
                serviceRegistry.marcarInstanciaAtiva(instance.obterID());
            } else {
                System.out.println("[HealthChecker] ✓ " + instance.obterID() + " OK");
            }
            instance.definirUltimoHealthCheck(Instant.now().toEpochMilli());
            
        } catch (Exception e) {
            // FALHA: incrementa contador de falhas
            System.out.println("[HealthChecker] ✗ " + instance.obterID() + " indisponível");
            serviceRegistry.registrarFalha(instance.obterID());
            instance.definirUltimoHealthCheck(Instant.now().toEpochMilli());
        }
    }
}
