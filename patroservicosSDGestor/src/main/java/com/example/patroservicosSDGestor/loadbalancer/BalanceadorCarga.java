package com.example.patroservicosSDGestor.loadbalancer;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Balanceador de carga round-robin.
 * Cada instância atende `REQUESTS_POR_INSTANCIA` requisições seguidas antes de alternar.
 */
@Component
public class BalanceadorCarga {
    private final ServiceRegistry serviceRegistry;
    private final AtomicInteger counter = new AtomicInteger(0);  // thread-safe
    private static final int REQUESTS_POR_INSTANCIA = 3; // ajustar conforme desejado

    public BalanceadorCarga(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Seleciona próxima instância: cada instância é usada REQUESTS_POR_INSTANCIA vezes seguidas.
     */
    public synchronized String obterProximaUrlInstancia(String serviceName) {
        List<ServiceInstance> activeInstances = filtrarPorServiceName(serviceName);

        if (activeInstances.isEmpty()) {
            throw new RuntimeException("Nenhuma instância ativa disponível para " + serviceName);
        }

        int index = (counter.getAndIncrement() / REQUESTS_POR_INSTANCIA) % activeInstances.size();
        ServiceInstance selected = activeInstances.get(index);

        System.out.println("[LoadBalancer] Selecionou: " + selected.obterID() + " (" + selected.obterBaseUrl() + ")");

        return selected.obterBaseUrl();
    }

    /**
     * Retorna a próxima instância em termos de objeto ServiceInstance.
     */
    public ServiceInstance obterProximaInstancia(String serviceName) {
        List<ServiceInstance> activeInstances = filtrarPorServiceName(serviceName);

        if (activeInstances.isEmpty()) {
            throw new RuntimeException("Nenhuma instância ativa disponível para " + serviceName);
        }

        int index = (counter.getAndIncrement() / REQUESTS_POR_INSTANCIA) % activeInstances.size();
        return activeInstances.get(index);
    }

    /**
     * Filtra as instâncias ativas do registry para aquelas cujo id indica o serviço desejado.
     * Ex: serviceName 'sd2' corresponderá a instâncias com id 'sd2' ou 'sd2-xyz' ou 'sd2replica'.
     */
    private List<ServiceInstance> filtrarPorServiceName(String serviceName) {
        List<ServiceInstance> allActive = serviceRegistry.obterInstanciasAtivas();
        List<ServiceInstance> filtered = new ArrayList<>();

        for (ServiceInstance inst : allActive) {
            if (inst.obterID() != null && inst.obterID().toLowerCase().startsWith(serviceName.toLowerCase())) {
                filtered.add(inst);
            }
        }

        // Se não encontrou correspondência específica, usa todas as ativas como fallback
        return filtered.isEmpty() ? allActive : filtered;
    }

    public List<ServiceInstance> obterInstanciasAtivas() {
        return serviceRegistry.obterInstanciasAtivas();
    }

    public int obterContagemInstanciasAtivas() {
        return serviceRegistry.obterInstanciasAtivas().size();
    }
}
