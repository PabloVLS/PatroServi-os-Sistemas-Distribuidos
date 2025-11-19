package com.example.patroservicosSDGestor.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Gateway para encaminhar chamadas de feedback ao Serviço 3 (SD3).
 * Valida que o cliente está autenticado antes de aceitar feedbacks.
 */
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackGatewayController {

    private final RestTemplate restTemplate;

    @Value("${servicos.sd3.base-url:http://localhost:8084}")
    private String sd3BaseUrl;

    public FeedbackGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * POST /api/feedbacks
     * Cria um novo feedback. Requer cliente_id do cliente logado.
     * Valida que cliente_id ou usuarioLogin está presente (indicando autenticação).
     */
    @PostMapping
    public ResponseEntity<String> criarFeedback(
            @RequestBody String body,
            @RequestHeader(value = "X-Cliente-Id", required = false) String clienteId,
            @RequestHeader(value = "X-Usuario-Login", required = false) String usuarioLogin) {
        
        // Validar que cliente está autenticado
        if ((clienteId == null || clienteId.isEmpty()) && 
            (usuarioLogin == null || usuarioLogin.isEmpty())) {
            return ResponseEntity.status(401).body("{\"error\": \"cliente não autenticado\"}");
        }

        String url = sd3BaseUrl + "/api/feedbacks";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    /**
     * GET /api/feedbacks/profissional/{profissional_id}
     * Lista feedbacks de um profissional específico (público).
     * NOTA: Esta rota deve vir antes de GET /{id} para evitar ambiguidade.
     */
    @GetMapping("/profissional/{profissional_id}")
    public ResponseEntity<String> listarFeedbacksProfissional(@PathVariable("profissional_id") String profissionalId) {
        String url = sd3BaseUrl + "/api/feedbacks/profissional/" + profissionalId;
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    /**
     * GET /api/feedbacks/{id}
     * Obtém um feedback específico por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> obterFeedback(@PathVariable("id") String id) {
        String url = sd3BaseUrl + "/api/feedbacks/" + id;
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}

/**
 * Gateway para encaminhar chamadas de perfis de clientes ao Serviço 3 (SD3).
 * Valida que o cliente está autenticado antes de permitir edições.
 */
@RestController
@RequestMapping("/api/perfil-cliente")
class PerfisClientesGatewayController {
    
    private final RestTemplate restTemplate;
    
    @Value("${servicos.sd3.base-url:http://localhost:8084}")
    private String sd3BaseUrl;
    
    public PerfisClientesGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * GET /api/perfil-cliente/{cliente_id}
     * Obtém perfil do cliente. Público (sem autenticação requerida).
     */
    @GetMapping("/{cliente_id}")
    public ResponseEntity<String> obterPerfilCliente(@PathVariable("cliente_id") String clienteId) {
        String url = sd3BaseUrl + "/api/perfil-cliente/" + clienteId;
        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
    
    /**
     * POST /api/perfil-cliente
     * Cria ou atualiza perfil do cliente logado.
     * Requer X-Cliente-Id no header para validar autenticação.
     */
    @PostMapping
    public ResponseEntity<String> criarOuAtualizarPerfil(
            @RequestBody String body,
            @RequestHeader(value = "X-Cliente-Id", required = false) String clienteId) {
        
        // Validar que cliente está autenticado
        if (clienteId == null || clienteId.isEmpty()) {
            return ResponseEntity.status(401).body("{\"error\": \"cliente não autenticado\"}");
        }
        
        String url = sd3BaseUrl + "/api/perfil-cliente";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        
        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
    
    /**
     * PUT /api/perfil-cliente/{cliente_id}
     * Atualiza perfil do cliente.
     * Requer X-Cliente-Id no header e validação de igualdade com path variable.
     */
    @PutMapping("/{cliente_id}")
    public ResponseEntity<String> atualizarPerfil(
            @PathVariable("cliente_id") String clienteId,
            @RequestBody String body,
            @RequestHeader(value = "X-Cliente-Id", required = false) String headerClienteId) {
        
        // Validar que cliente está autenticado e ID corresponde
        if (headerClienteId == null || headerClienteId.isEmpty()) {
            return ResponseEntity.status(401).body("{\"error\": \"cliente não autenticado\"}");
        }
        if (!headerClienteId.equals(clienteId)) {
            return ResponseEntity.status(403).body("{\"error\": \"não autorizado a editar outro cliente\"}");
        }
        
        // Usar POST ao SD3 (upsert implementado no SD3)
        String url = sd3BaseUrl + "/api/perfil-cliente/" + clienteId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        
        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
