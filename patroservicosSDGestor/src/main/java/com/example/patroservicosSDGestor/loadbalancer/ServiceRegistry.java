package com.example.patroservicosSDGestor.loadbalancer;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registro centralizado de instâncias.
 * ConcurrentHashMap garante thread-safety com múltiplas requisições simultâneas.
 */
@Component
public class ServiceRegistry {
    private final Map<String, ServiceInstance> instances = new ConcurrentHashMap<>();

    public void registrarInstancia(String id, String baseUrl) {
        instances.putIfAbsent(id, new ServiceInstance(id, baseUrl));
    }

    public ServiceInstance obterInstancia(String id) {
        return instances.get(id);
    }

    public Collection<ServiceInstance> obterTodasInstancias() {
        return instances.values();
    }

    public List<ServiceInstance> obterInstanciasAtivas() {
        // CRÍTICO: só retorna instâncias ativas - o LoadBalancer usa só este resultado
        List<ServiceInstance> active = new ArrayList<>();
        for (ServiceInstance inst : instances.values()) {
            if (inst.estaAtiva()) {
                active.add(inst);
            }
        }
        return active;
    }

    public void marcarInstanciaAtiva(String id) {
        ServiceInstance inst = instances.get(id);
        if (inst != null) {
            inst.marcarAtiva();
        }
    }

    public void marcarInstanciaInativa(String id) {
        ServiceInstance inst = instances.get(id);
        if (inst != null) {
            inst.marcarInativa();
        }
    }

    public void registrarSucesso(String id) {
        ServiceInstance inst = instances.get(id);
        if (inst != null) {
            inst.registrarSucesso();
        }
    }

    public void registrarFalha(String id) {
        ServiceInstance inst = instances.get(id);
        if (inst != null) {
            inst.registrarFalha();
        }
    }

    public void exibirStatus() {
        System.out.println("\n========== STATUS DO REGISTRO DE SERVIÇOS ==========");
        for (ServiceInstance inst : instances.values()) {
            System.out.println(inst);
        }
        System.out.println("====================================================\n");
    }
}
