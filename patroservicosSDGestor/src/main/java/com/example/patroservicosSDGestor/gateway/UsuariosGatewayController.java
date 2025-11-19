package com.example.patroservicosSDGestor.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Gateway para encaminhar chamadas de usuários profissionais ao Serviço 2 (SD2).
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosGatewayController {

    private final RestTemplate restTemplate;

    @Value("${servicos.sd2.base-url:http://localhost:8083}")
    private String sd2BaseUrl;

    public UsuariosGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody String body) {
        String url = sd2BaseUrl + "/api/usuarios";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException e) {
            // Propaga o status e corpo de erro do SD2 para o cliente
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> obterUsuario(@PathVariable("id") Long id) {
        String url = sd2BaseUrl + "/api/usuarios/" + id;
        return restTemplate.getForEntity(url, String.class);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String body) {
        String url = sd2BaseUrl + "/api/usuarios/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException e) {
            // Retorna o mesmo código e corpo quando SD2 responde 4xx (ex: 401)
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
