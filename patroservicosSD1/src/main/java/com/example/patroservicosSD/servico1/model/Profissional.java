package com.example.patroservicosSD.servico1.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
  name = "profissionais",
  indexes = {
    @Index(name = "ix_profissionais_cidade", columnList = "cidade"),
    @Index(name = "ix_profissionais_profissao", columnList = "profissao")
  }
)
public class Profissional {
  @Id
  @GeneratedValue
  private UUID id;

  private String nome;

  @Column(unique = true)
  private String numero;     // telefone simples

  @Column(unique = true)
  private String email;

  private String profissao;

  @Column(length = 1000)
  private String descricao;

  private String cidade;
  private String estado;

  @OneToOne(mappedBy = "profissional", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PerfilProfissional perfil;

  // getters/setters
  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public String getNumero() { return numero; }
  public void setNumero(String numero) { this.numero = numero; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getProfissao() { return profissao; }
  public void setProfissao(String profissao) { this.profissao = profissao; }
  public String getDescricao() { return descricao; }
  public void setDescricao(String descricao) { this.descricao = descricao; }
  public String getCidade() { return cidade; }
  public void setCidade(String cidade) { this.cidade = cidade; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public PerfilProfissional getPerfil() { return perfil; }
  public void setPerfil(PerfilProfissional perfil) { this.perfil = perfil; }
}