package com.example.patroservicosSD.servico1.repositories;

import com.example.patroservicosSD.servico1.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProfissionalRepository extends JpaRepository<Profissional, UUID> {
  List<Profissional> findByCidadeIgnoreCase(String cidade);
  List<Profissional> findByProfissaoIgnoreCase(String profissao);
  List<Profissional> findByCidadeIgnoreCaseAndProfissaoIgnoreCase(String cidade, String profissao);

  boolean existsByEmailIgnoreCase(String email);
  boolean existsByNumero(String numero);
}