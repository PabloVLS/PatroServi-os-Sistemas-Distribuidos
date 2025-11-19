package com.example.patroservicosSDGestor.gateway;

import com.example.patroservicosSDGestor.loadbalancer.BalanceadorCargaRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Gateway para Profissionais com balanceamento automático.
 * Cada requisição é distribuída entre SD1 e SD1Replica.
 * Se uma cair, as requisições vão para a outra automaticamente.
 */
@RestController
@RequestMapping("/api/profissionais")
public class ProfissionaisGatewayController {

    private final BalanceadorCargaRestTemplate loadBalancedRest;

    public ProfissionaisGatewayController(BalanceadorCargaRestTemplate loadBalancedRest) {
        this.loadBalancedRest = loadBalancedRest;
    }

    /** Lista profissionais com filtros - vai para o próximo na fila de rodízio */
    @GetMapping
    public ResponseEntity<String> listar(@RequestParam(required = false) String cidade,
                                         @RequestParam(required = false) String profissao) {
        StringBuilder path = new StringBuilder("/api/profissionais");
        if (cidade != null || profissao != null) {
            path.append("?");
            if (cidade != null) path.append("cidade=").append(cidade).append("&");
            if (profissao != null) path.append("profissao=").append(profissao).append("&");
        }
        ResponseEntity<String> resp = loadBalancedRest.obterEntidade(path.toString(), String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    /** Busca profissional por ID com balanceamento automático */
    @GetMapping("/{id}")
    public ResponseEntity<String> buscar(@PathVariable UUID id) {
        ResponseEntity<String> resp = loadBalancedRest.obterEntidade("/api/profissionais/" + id, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    /** Obtém perfil detalhado */
    @GetMapping("/{id}/perfil")
    public ResponseEntity<String> obterPerfil(@PathVariable UUID id) {
        ResponseEntity<String> resp = loadBalancedRest.obterEntidade("/api/profissionais/" + id + "/perfil", String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    /** Atualiza perfil (JSON) */
    @PutMapping("/{id}/perfil")
    public ResponseEntity<String> atualizarPerfil(@PathVariable UUID id, @RequestBody String bodyJson) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> ent = new HttpEntity<>(bodyJson, h);
        ResponseEntity<String> resp = loadBalancedRest.trocar("/api/profissionais/" + id + "/perfil", HttpMethod.PUT, ent, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    /** Cadastro completo sem upload (somente JSON) */
    @PostMapping("/cadastro-completo")
    public ResponseEntity<String> cadastroCompleto(@RequestBody String bodyJson) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> ent = new HttpEntity<>(bodyJson, h);
        ResponseEntity<String> resp = loadBalancedRest.postParaEntidade("/api/profissionais/cadastro-completo", ent, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    /** Cadastro completo com upload (multipart repassado) */
    @PostMapping(value="/cadastro-completo-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> cadastroCompletoUpload(@RequestPart("dados") String dadosJson,
                                                         @RequestPart(value="fotoCpf", required=false) MultipartFile fotoCpf,
                                                         @RequestPart(value="fotoRg", required=false) MultipartFile fotoRg,
                                                         @RequestPart(value="fotoPerfil", required=false) MultipartFile fotoPerfil,
                                                         @RequestPart(value="portfolio", required=false) MultipartFile[] portfolio) {
        // Monta estrutura multipart para enviar ao Serviço 1
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> jsonPart = new HttpEntity<>(dadosJson, jsonHeaders);
        parts.add("dados", jsonPart);

        if (fotoCpf != null && !fotoCpf.isEmpty()) parts.add("fotoCpf", MultipartSupport.asEntity(fotoCpf));
        if (fotoRg != null && !fotoRg.isEmpty()) parts.add("fotoRg", MultipartSupport.asEntity(fotoRg));
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) parts.add("fotoPerfil", MultipartSupport.asEntity(fotoPerfil));
        if (portfolio != null) {
            for (MultipartFile f : portfolio) {
                if (f != null && !f.isEmpty()) parts.add("portfolio", MultipartSupport.asEntity(f));
            }
        }

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(parts, h);
        ResponseEntity<String> resp = loadBalancedRest.postParaEntidade("/api/profissionais/cadastro-completo-upload", reqEntity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    /** Atualiza perfil com upload de foto de perfil e portfolio */
    @PostMapping(value="/{id}/perfil-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> atualizarPerfilComUpload(@PathVariable UUID id,
                                                           @RequestPart("dados") String dadosJson,
                                                           @RequestPart(value="fotoPerfil", required=false) MultipartFile fotoPerfil,
                                                           @RequestPart(value="portfolio", required=false) MultipartFile[] portfolio) {
        // Monta estrutura multipart para enviar ao Serviço 1
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> jsonPart = new HttpEntity<>(dadosJson, jsonHeaders);
        parts.add("dados", jsonPart);

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) parts.add("fotoPerfil", MultipartSupport.asEntity(fotoPerfil));
        if (portfolio != null) {
            for (MultipartFile f : portfolio) {
                if (f != null && !f.isEmpty()) parts.add("portfolio", MultipartSupport.asEntity(f));
            }
        }

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> reqEntity = new HttpEntity<>(parts, h);
        ResponseEntity<String> resp = loadBalancedRest.trocar("/api/profissionais/" + id + "/perfil-upload", HttpMethod.POST, reqEntity, String.class);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }
}

/** Utilitário para converter MultipartFile em HttpEntity<byte[]> reutilizável no proxy */
class MultipartSupport {
    static HttpEntity<byte[]> asEntity(MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData(file.getName(), file.getOriginalFilename());
            return new HttpEntity<>(file.getBytes(), headers);
        } catch (Exception e) {
            throw new RuntimeException("Erro lendo arquivo multipart", e);
        }
    }
}
