package com.example.patroservicosSD2.servico2.repositories;

import com.example.patroservicosSD2.servico2.model.UsuarioClientes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioClientesRepository extends JpaRepository<UsuarioClientes, UUID> {
    Optional<UsuarioClientes> findByLogin(String login);
}
