package com.example.patroservicosSD.servico1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
  name = "clientes",
  indexes = {
    @Index(name = "ix_clientes_nome", columnList = "nome")
  }
)
public class Cliente {
  @Id
  @GeneratedValue
  @Column(name = "cliente_id")
  private UUID id;

  private String nome;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String numero;

  @Column(name = "data_cadastro", nullable = false)
  private LocalDateTime dataCadastro = LocalDateTime.now();

  // getters/setters
  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getNumero() { return numero; }
  public void setNumero(String numero) { this.numero = numero; }
  public LocalDateTime getDataCadastro() { return dataCadastro; }
  public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}