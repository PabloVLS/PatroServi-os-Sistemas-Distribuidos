package com.example.patroservicosSD.servico1.controllers;

import com.example.patroservicosSD.servico1.dto.ProfissionalCadastroRequest;
import com.example.patroservicosSD.servico1.dto.ProfissionalCadastroResponse;
import com.example.patroservicosSD.servico1.dto.ProfissionalResponse;
import com.example.patroservicosSD.servico1.dto.ProfissionalPerfilRequest;
import com.example.patroservicosSD.servico1.dto.ProfissionalPerfilResponse;
import com.example.patroservicosSD.servico1.mappers.ProfissionalMapper;
import com.example.patroservicosSD.servico1.model.Profissional;
import com.example.patroservicosSD.servico1.services.ProfissionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Base64;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

  private final ProfissionalService servico;

  public ProfissionalController(ProfissionalService servico) {
    this.servico = servico;
  }

  @GetMapping
  public ResponseEntity<List<ProfissionalResponse>> listar(
      @RequestParam(required = false) String cidade,
      @RequestParam(required = false) String profissao
  ) {
    List<Profissional> lista;
    if (cidade != null && profissao != null) {
      lista = servico.buscarPorCidadeEProfissao(cidade, profissao);
    } else if (cidade != null) {
      lista = servico.buscarPorCidade(cidade);
    } else if (profissao != null) {
      lista = servico.buscarPorProfissao(profissao);
    } else {
      lista = servico.listarTodos();
    }
    List<ProfissionalResponse> resp = lista.stream()
      .map(ProfissionalMapper::paraResposta)
      .collect(Collectors.toList());
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfissionalResponse> buscar(@PathVariable UUID id) {
    Profissional p = servico.buscarPorId(id);
    return ResponseEntity.ok(ProfissionalMapper.paraResposta(p));
  }

  @GetMapping("/{id}/perfil")
  public ResponseEntity<ProfissionalPerfilResponse> obterPerfil(@PathVariable UUID id) {
    return ResponseEntity.ok(servico.buscarPerfil(id));
  }

  @PutMapping("/{id}/perfil")
  public ResponseEntity<ProfissionalPerfilResponse> atualizarPerfil(@PathVariable UUID id,
                                                                    @RequestBody ProfissionalPerfilRequest req) {
    return ResponseEntity.ok(servico.atualizarPerfil(id, req));
  }

  @PostMapping(value="/{id}/perfil-upload", consumes = "multipart/form-data")
  public ResponseEntity<ProfissionalPerfilResponse> atualizarPerfilComUpload(
      @PathVariable UUID id,
      @RequestPart("dados") ProfissionalPerfilRequest req,
      @RequestPart(value="fotoPerfil", required=false) MultipartFile fotoPerfil,
      @RequestPart(value="portfolio", required=false) MultipartFile[] portfolio
  ) throws IOException {
    // Converte fotoPerfil para base64
    String base64FotoPerfil = null;
    if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
      base64FotoPerfil = "data:" + fotoPerfil.getContentType() + ";base64," + 
                         Base64.getEncoder().encodeToString(fotoPerfil.getBytes());
    }

    // Converte portfólio para base64 (lista separada por vírgula)
    StringBuilder fotosBase64Sb = new StringBuilder();
    if (portfolio != null) {
      for (MultipartFile f : portfolio) {
        if (f != null && !f.isEmpty()) {
          String base64 = "data:" + f.getContentType() + ";base64," + 
                          Base64.getEncoder().encodeToString(f.getBytes());
          if (fotosBase64Sb.length() > 0) fotosBase64Sb.append(",");
          fotosBase64Sb.append(base64);
        }
      }
    }
    
    // Injeta base64s na requisição
    if (base64FotoPerfil != null) req.setFotoPerfil(base64FotoPerfil);
    if (fotosBase64Sb.length() > 0) req.setFotos(fotosBase64Sb.toString());

    return ResponseEntity.ok(servico.atualizarPerfil(id, req));
  }

  @PostMapping("/cadastro-completo")
  public ResponseEntity<ProfissionalCadastroResponse> cadastroCompleto(@RequestBody ProfissionalCadastroRequest req) {
    ProfissionalCadastroResponse resp = servico.criarCadastroCompleto(req);
    return ResponseEntity.created(URI.create("/api/profissionais/" + resp.getProfissionalId())).body(resp);
  }

  @PostMapping(value="/cadastro-completo-upload", consumes = "multipart/form-data")
  public ResponseEntity<ProfissionalCadastroResponse> cadastroCompletoUpload(
      @RequestPart("dados") ProfissionalCadastroRequest req,
      @RequestPart(value="fotoCpf", required=false) MultipartFile fotoCpf,
      @RequestPart(value="fotoRg", required=false) MultipartFile fotoRg,
      @RequestPart(value="fotoPerfil", required=false) MultipartFile fotoPerfil,
      @RequestPart(value="portfolio", required=false) MultipartFile[] portfolio
  ) throws IOException {
    // Converte documentos para base64
    String base64FotoCpf = null;
    if (fotoCpf != null && !fotoCpf.isEmpty()) {
      base64FotoCpf = "data:" + fotoCpf.getContentType() + ";base64," + 
                      Base64.getEncoder().encodeToString(fotoCpf.getBytes());
    }

    String base64FotoRg = null;
    if (fotoRg != null && !fotoRg.isEmpty()) {
      base64FotoRg = "data:" + fotoRg.getContentType() + ";base64," + 
                     Base64.getEncoder().encodeToString(fotoRg.getBytes());
    }

    // Converte fotoPerfil para base64
    String base64FotoPerfil = null;
    if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
      base64FotoPerfil = "data:" + fotoPerfil.getContentType() + ";base64," + 
                         Base64.getEncoder().encodeToString(fotoPerfil.getBytes());
    }

    // Converte portfólio para base64 (lista separada por vírgula)
    StringBuilder fotosBase64Sb = new StringBuilder();
    if (portfolio != null) {
      for (MultipartFile f : portfolio) {
        if (f != null && !f.isEmpty()) {
          String base64 = "data:" + f.getContentType() + ";base64," + 
                          Base64.getEncoder().encodeToString(f.getBytes());
          if (fotosBase64Sb.length() > 0) fotosBase64Sb.append(",");
          fotosBase64Sb.append(base64);
        }
      }
    }
    
    // Injeta base64s na requisição
    req.setFotos(fotosBase64Sb.toString());
    req.setFotoPerfil(base64FotoPerfil);
    
    // Pass base64s do documento
    ProfissionalCadastroResponse resp = servico.criarCadastroCompletoComImagens(req, base64FotoCpf, base64FotoRg);
    return ResponseEntity.created(URI.create("/api/profissionais/" + resp.getProfissionalId())).body(resp);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable UUID id) {
    servico.deletar(id);
    return ResponseEntity.noContent().build();
  }
}