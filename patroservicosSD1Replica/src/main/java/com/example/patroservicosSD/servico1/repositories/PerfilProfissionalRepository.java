package com.example.patroservicosSD.servico1.repositories;

import com.example.patroservicosSD.servico1.model.PerfilProfissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PerfilProfissionalRepository extends JpaRepository<PerfilProfissional, UUID> {
  boolean existsByProfissionalId(UUID profissionalId);
  Optional<PerfilProfissional> findByProfissionalId(UUID profissionalId);
  void deleteByProfissionalId(UUID profissionalId);
}