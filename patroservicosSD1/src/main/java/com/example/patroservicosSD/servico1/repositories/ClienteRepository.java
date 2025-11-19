package com.example.patroservicosSD.servico1.repositories;

import com.example.patroservicosSD.servico1.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
  boolean existsByEmailIgnoreCase(String email);
  boolean existsByNumero(String numero);

  Optional<Cliente> findByEmailIgnoreCase(String email);
  List<Cliente> findByNomeContainingIgnoreCase(String nome);
}