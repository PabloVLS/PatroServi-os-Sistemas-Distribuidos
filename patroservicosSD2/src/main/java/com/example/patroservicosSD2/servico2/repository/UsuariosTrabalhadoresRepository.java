package com.example.patroservicosSD2.servico2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.patroservicosSD2.servico2.model.UsuariosTrabalhadores;

/**
 * Repositório JPA para a entidade UsuariosTrabalhadores.
 */
public interface UsuariosTrabalhadoresRepository extends JpaRepository<UsuariosTrabalhadores, Long> {
    Optional<UsuariosTrabalhadores> findByLogin(String login);
}
