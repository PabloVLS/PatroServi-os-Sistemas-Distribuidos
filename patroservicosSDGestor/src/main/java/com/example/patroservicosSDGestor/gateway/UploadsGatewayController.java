package com.example.patroservicosSDGestor.gateway;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.example.patroservicosSDGestor.loadbalancer.BalanceadorCargaRestTemplate;

/**
 * Gateway para upload de arquivos do profissional ao Serviço 1 com balanceamento.
 */
@RestController
@RequestMapping("/api/uploads")
public class UploadsGatewayController {

    private final BalanceadorCargaRestTemplate balanceador;

    public UploadsGatewayController(BalanceadorCargaRestTemplate balanceador) {
        this.balanceador = balanceador;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "prefixo", defaultValue = "file") String prefixo) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() { return file.getOriginalFilename(); }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        String path = "/api/uploads?prefixo=" + prefixo;
        return balanceador.postParaEntidade(path, entity, String.class);
    }
}
