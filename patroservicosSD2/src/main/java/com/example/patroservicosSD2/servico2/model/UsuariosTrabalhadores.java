package com.example.patroservicosSD2.servico2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.util.UUID;

/**
 * Entidade de usuários do Serviço 2 (SD2).
 * Armazena credenciais de login (com hash da senha) e timestamps de auditoria.
 */
@Entity
@Table(name = "usuarios_trabalhadores")
public class UsuariosTrabalhadores {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String login;

	// Hash da senha usando BCrypt (nunca salvar a senha em texto puro)
	@Column(name = "senha_hash", nullable = false, length = 100)
	private String senhaHash;

	@Column(name = "criado_em", nullable = false, updatable = false)
	private Instant criadoEm;

	@Column(name = "atualizado_em", nullable = false)
	private Instant atualizadoEm;

	// Associação lógica com o profissional do SD1 (armazenamos apenas o UUID)
	@Column(name = "profissional_id")
	private UUID profissionalId;

	@PrePersist
	public void prePersist() {
		Instant agora = Instant.now();
		this.criadoEm = agora;
		this.atualizadoEm = agora;
	}

	@PreUpdate
	public void preUpdate() {
		this.atualizadoEm = Instant.now();
	}

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Instant criadoEm) {
		this.criadoEm = criadoEm;
	}

	public Instant getAtualizadoEm() {
		return atualizadoEm;
	}

	public void setAtualizadoEm(Instant atualizadoEm) {
		this.atualizadoEm = atualizadoEm;
	}

	public UUID getProfissionalId() {
		return profissionalId;
	}

	public void setProfissionalId(UUID profissionalId) {
		this.profissionalId = profissionalId;
	}
}
