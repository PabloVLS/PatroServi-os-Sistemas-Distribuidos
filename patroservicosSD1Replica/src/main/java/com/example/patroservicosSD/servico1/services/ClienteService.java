package com.example.patroservicosSD.servico1.services;

import com.example.patroservicosSD.servico1.model.Cliente;

import java.util.List;
import java.util.UUID;

public interface ClienteService {
  List<Cliente> listarTodos();
  Cliente buscarPorId(UUID id);
  Cliente criar(Cliente cliente);
  Cliente atualizar(UUID id, Cliente cliente);
  void deletar(UUID id);

  Cliente buscarPorEmail(String email);
  List<Cliente> buscarPorNome(String nome);
}