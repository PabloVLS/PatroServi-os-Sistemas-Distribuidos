package com.example.patroservicosSD2.servico2.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "usuarios_clientes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_ucli_cliente", columnNames = {"cliente_id"}),
        @UniqueConstraint(name = "uk_ucli_login", columnNames = {"login"})
    }
)
public class UsuarioClientes implements Serializable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    // referência lógica ao cliente do Serviço 1
    @Column(name = "cliente_id", nullable = false, columnDefinition = "uuid")
    private UUID clienteId;

    @Column(nullable = false, unique = true)
    private String login;

    // OBS: para demo pode ser texto; em produção use hash (bcrypt/argon2)
    @Column(nullable = false)
    private String senha;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }

    // getters e setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "UsuarioClientes{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", login='" + login + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}