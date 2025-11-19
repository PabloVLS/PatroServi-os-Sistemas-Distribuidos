package com.example.patroservicosSD.servico1.services;

import com.example.patroservicosSD.servico1.model.Profissional;
import com.example.patroservicosSD.servico1.dto.ProfissionalCadastroRequest;
import com.example.patroservicosSD.servico1.dto.ProfissionalCadastroResponse;
import com.example.patroservicosSD.servico1.dto.ProfissionalPerfilRequest;
import com.example.patroservicosSD.servico1.dto.ProfissionalPerfilResponse;

import java.util.List;
import java.util.UUID;

public interface ProfissionalService {
  List<Profissional> listarTodos();
  Profissional buscarPorId(UUID id);
  ProfissionalCadastroResponse criarCadastroCompleto(ProfissionalCadastroRequest request);
  ProfissionalCadastroResponse criarCadastroCompletoComImagens(ProfissionalCadastroRequest req, String pathCpf, String pathRg);
  Profissional atualizar(UUID id, Profissional profissional);
  void deletar(UUID id);

  List<Profissional> buscarPorCidade(String cidade);
  List<Profissional> buscarPorProfissao(String profissao);
  List<Profissional> buscarPorCidadeEProfissao(String cidade, String profissao);
  ProfissionalPerfilResponse buscarPerfil(UUID profissionalId);
  ProfissionalPerfilResponse atualizarPerfil(UUID profissionalId, ProfissionalPerfilRequest request);
}