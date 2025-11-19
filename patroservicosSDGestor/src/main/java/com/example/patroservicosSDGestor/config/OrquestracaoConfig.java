package com.example.patroservicosSDGestor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração central de orquestração do Gestor.
 * 
 * - Injeta as URLs base dos serviços downstream (serviço 1 e 2)
 * - Expõe um RestTemplate com timeouts definidos para chamadas HTTP
 */
@Configuration
public class OrquestracaoConfig {

    /** URL base do Serviço 1 (APIs de profissionais) */
    @Value("${servicos.sd1.base-url}")
    private String sd1BaseUrl;

    /** URL base do Serviço 2 (reservado para futuras rotas) */
    @Value("${servicos.sd2.base-url}")
    private String sd2BaseUrl;

    /**
     * Cliente HTTP utilizado para repassar (proxy) as requisições aos serviços.
     * Define timeouts para evitar travamentos em chamadas lentas.
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout(5000);
        f.setReadTimeout(8000);
        return new RestTemplate(f);
    }

    public String getSd1BaseUrl() { return sd1BaseUrl; }
    public String getSd2BaseUrl() { return sd2BaseUrl; }
}
