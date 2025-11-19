package com.example.patroservicosSDGestor.gateway;

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
import org.springframework.web.client.HttpClientErrorException;
import com.example.patroservicosSDGestor.loadbalancer.BalanceadorCargaRestTemplate;

/**
 * Gateway para encaminhar criação de Cliente ao Serviço 1 (SD1).
 * Agora usa BalanceadorCargaRestTemplate para failover automático.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClientesGatewayController {

    private final BalanceadorCargaRestTemplate balanceador;

    public ClientesGatewayController(BalanceadorCargaRestTemplate balanceador) {
        this.balanceador = balanceador;
    }

    @PostMapping
    public ResponseEntity<String> criarCliente(@RequestBody String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return balanceador.postParaEntidade("/api/clientes", entity, String.class);
    }

    /**
     * GET /api/clientes/{id}
     * Obtém dados básicos do cliente (nome, email, telefone) do SD1.
     * Público (sem autenticação requerida).
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> obterCliente(@PathVariable("id") String id) {
        try {
            return balanceador.obterEntidade("/api/clientes/" + id, String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
