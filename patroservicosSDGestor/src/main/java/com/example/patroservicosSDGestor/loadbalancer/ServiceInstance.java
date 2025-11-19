package com.example.patroservicosSDGestor.loadbalancer;

import java.time.Instant;

/**
 * Representa uma instância de um serviço (ex: Serviço 1, Serviço 1 Replica).
 * Se mudar MAX_FAILURES para 1 = rigoroso (qualquer falha inativa), 5+ = tolerante com falhas.
 */
public class ServiceInstance {
    private String id;                    // "sd1" ou "sd1replica"
    private String baseUrl;               // http://localhost:8082
    private boolean active;                // true = funcionando, false = caiu
    private long lastHealthCheckTime;      // Timestamp do último health check
    private int failureCount;              // Quantas vezes falhou seguidas (zera quando volta)
    private static final int MAX_FAILURES = 3;  // Limite de falhas antes de inativar

    public ServiceInstance(String id, String baseUrl) {
        this.id = id;
        this.baseUrl = baseUrl;
        this.active = true;
        this.lastHealthCheckTime = Instant.now().toEpochMilli();
        this.failureCount = 0;
    }

    public String obterID() {
        return id;
    }

    public String obterBaseUrl() {
        return baseUrl;
    }

    public boolean estaAtiva() {
        return active;
    }

    public void marcarAtiva() {
        this.active = true;
        this.failureCount = 0;
    }
    public void marcarInativa() {
        // Marca explicitamente como inativa e garante que o contador de falhas
        // reflita que está inativa (útil para logs e decisões de reativação).
        this.active = false;
        if (this.failureCount < MAX_FAILURES) this.failureCount = MAX_FAILURES;
        System.out.println("[INFO] Serviço '" + id + "' marcado como INATIVO manualmente.");
    }
    public void registrarFalha() {
        // Se já está inativa, não faz nada
        if (!this.active) {
            if (this.failureCount < MAX_FAILURES) this.failureCount = MAX_FAILURES;
            return;
        }

        this.failureCount++;
        // Quando atinge o limite, marca como inativa
        if (failureCount >= MAX_FAILURES) {
            this.active = false;
            System.out.println("[ALERT] Serviço '" + id + "' atingiu máximo de falhas e foi marcado INATIVO!");
        }
    }

    public void registrarSucesso() {
        // Quando responde bem, zera o contador de falhas
        this.failureCount = 0;
    }

    public int obterContagemFalhas() {
        return failureCount;
    }

    public void definirUltimoHealthCheck(long time) {
        this.lastHealthCheckTime = time;
    }

    public long obterUltimoHealthCheck() {
        return lastHealthCheckTime;
    }

    @Override
    public String toString() {
        return String.format("ServiceInstance{id='%s', url='%s', active=%s, failures=%d}", 
            id, baseUrl, active, failureCount);
    }
}
