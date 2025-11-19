package com.example.patroservicosSD.servico1.services.impl;

import com.example.patroservicosSD.servico1.model.Cliente;
import com.example.patroservicosSD.servico1.repositories.ClienteRepository;
import com.example.patroservicosSD.servico1.services.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

  private final ClienteRepository repositorio;

  public ClienteServiceImpl(ClienteRepository repositorio) {
    this.repositorio = repositorio;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Cliente> listarTodos() {
    return repositorio.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Cliente buscarPorId(UUID id) {
    return repositorio.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));
  }

  @Override
  public Cliente criar(Cliente cliente) {
    if (cliente.getEmail() != null && repositorio.existsByEmailIgnoreCase(cliente.getEmail())) {
      throw new IllegalArgumentException("Email já cadastrado");
    }
    if (cliente.getNumero() != null && repositorio.existsByNumero(cliente.getNumero())) {
      throw new IllegalArgumentException("Número já cadastrado");
    }
    cliente.setId(null);
    return repositorio.save(cliente);
  }

  @Override
  public Cliente atualizar(UUID id, Cliente dados) {
    Cliente existente = buscarPorId(id);

    if (dados.getNome() != null) existente.setNome(dados.getNome());

    if (dados.getNumero() != null && !dados.getNumero().equals(existente.getNumero())) {
      if (repositorio.existsByNumero(dados.getNumero())) throw new IllegalArgumentException("Número já cadastrado");
      existente.setNumero(dados.getNumero());
    }

    if (dados.getEmail() != null && (existente.getEmail() == null || !dados.getEmail().equalsIgnoreCase(existente.getEmail()))) {
      if (repositorio.existsByEmailIgnoreCase(dados.getEmail())) throw new IllegalArgumentException("Email já cadastrado");
      existente.setEmail(dados.getEmail());
    }

    if (dados.getDataCadastro() != null) existente.setDataCadastro(dados.getDataCadastro());

    return repositorio.save(existente);
  }

  @Override
  public void deletar(UUID id) {
    if (!repositorio.existsById(id)) throw new EntityNotFoundException("Cliente não encontrado: " + id);
    repositorio.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Cliente buscarPorEmail(String email) {
    return repositorio.findByEmailIgnoreCase(email).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Cliente> buscarPorNome(String nome) {
    return repositorio.findByNomeContainingIgnoreCase(nome);
  }
}