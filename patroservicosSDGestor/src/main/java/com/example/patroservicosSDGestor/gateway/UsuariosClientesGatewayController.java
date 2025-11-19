package com.example.patroservicosSDGestor.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Gateway para credenciais de clientes (SD2).
 */
@RestController
@RequestMapping("/api/usuarios-clientes")
public class UsuariosClientesGatewayController {

    private final RestTemplate restTemplate;

    @Value("${servicos.sd2.base-url:http://localhost:8083}")
    private String sd2BaseUrl;

    public UsuariosClientesGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody String body) {
        String url = sd2BaseUrl + "/api/usuarios-clientes";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String body) {
        String url = sd2BaseUrl + "/api/usuarios-clientes/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }
}
