package com.example.patroservicosSD.servico1.repositories;

import com.example.patroservicosSD.servico1.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentoRepository extends JpaRepository<Documento, UUID> {
  boolean existsByCpf(String cpf);
  boolean existsByRg(String rg);
  boolean existsByProfissionalId(UUID profissionalId);

  Optional<Documento> findByProfissionalId(UUID profissionalId);
  void deleteByProfissionalId(UUID profissionalId);
}