package com.example.patroservicosSD.servico1.controllers;

import com.example.patroservicosSD.servico1.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

  private final FileStorageService storage;

  @Value("${server.port:0}")
  private String serverPort;

  @Value("${uploads.dir:uploads}")
  private String uploadsDir;

  public UploadController(FileStorageService storage) {
    this.storage = storage;
  }

  /**
   * Upload genérico que devolve a URL pública do arquivo salvo.
   * @param file arquivo enviado (campo "file")
   * @param prefixo prefixo para compor nome (ex.: port, doc_cpf, doc_rg)
   */
  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Map<String,String>> upload(@RequestPart("file") MultipartFile file,
                                                   @RequestParam(defaultValue = "file") String prefixo) {
    String url = storage.salvar(file, prefixo);
    Map<String,String> resp = new HashMap<>();
    resp.put("url", url);
    resp.put("instance_port", serverPort);
    resp.put("uploads_dir", uploadsDir);
    return ResponseEntity.status(HttpStatus.CREATED).header("X-Instance-Port", serverPort).body(resp);
  }
}
