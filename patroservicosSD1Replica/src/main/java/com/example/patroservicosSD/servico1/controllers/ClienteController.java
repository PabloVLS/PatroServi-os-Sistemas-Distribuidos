package com.example.patroservicosSD.servico1.controllers;

import com.example.patroservicosSD.servico1.dto.ClienteRequest;
import com.example.patroservicosSD.servico1.dto.ClienteResponse;
import com.example.patroservicosSD.servico1.mappers.ClienteMapper;
import com.example.patroservicosSD.servico1.model.Cliente;
import com.example.patroservicosSD.servico1.services.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

  private final ClienteService servico;

  public ClienteController(ClienteService servico) { this.servico = servico; }

  @GetMapping
  public ResponseEntity<List<ClienteResponse>> listar() {
    List<ClienteResponse> resp = servico.listarTodos().stream()
      .map(ClienteMapper::paraResposta)
      .collect(Collectors.toList());
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClienteResponse> buscar(@PathVariable UUID id) {
    Cliente c = servico.buscarPorId(id);
    return ResponseEntity.ok(ClienteMapper.paraResposta(c));
  }

  @GetMapping("/por-email")
  public ResponseEntity<ClienteResponse> buscarPorEmail(@RequestParam String email) {
    Cliente c = servico.buscarPorEmail(email);
    return ResponseEntity.ok(ClienteMapper.paraResposta(c));
  }

  @GetMapping("/pesquisa")
  public ResponseEntity<List<ClienteResponse>> buscarPorNome(@RequestParam String nome) {
    List<ClienteResponse> resp = servico.buscarPorNome(nome).stream()
      .map(ClienteMapper::paraResposta)
      .collect(Collectors.toList());
    return ResponseEntity.ok(resp);
  }

  @PostMapping
  public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest req) {
    Cliente salvo = servico.criar(ClienteMapper.paraEntidade(req));
    return ResponseEntity.created(URI.create("/api/clientes/" + salvo.getId()))
      .body(ClienteMapper.paraResposta(salvo));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ClienteResponse> atualizar(@PathVariable UUID id, @RequestBody ClienteRequest req) {
    Cliente atual = servico.buscarPorId(id);
    ClienteMapper.atualizarEntidade(atual, req);
    Cliente salvo = servico.atualizar(id, atual);
    return ResponseEntity.ok(ClienteMapper.paraResposta(salvo));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletar(@PathVariable UUID id) {
    servico.deletar(id);
    return ResponseEntity.noContent().build();
  }
}