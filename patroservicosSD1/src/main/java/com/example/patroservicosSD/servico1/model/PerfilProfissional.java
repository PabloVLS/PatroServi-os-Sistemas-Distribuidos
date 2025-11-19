package com.example.patroservicosSD.servico1.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "perfil_profissional")
public class PerfilProfissional {
  @Id
  @GeneratedValue
  private UUID id;

  @OneToOne
  @JoinColumn(name = "profissional_id", unique = true, nullable = false)
  private Profissional profissional;

  @Column(length = 10000000)
  private String fotoPerfil; // base64 da foto de perfil do profissional

  @Column(length = 10000000)
  private String fotos; // lista de base64 separadas por vírgula
  @Column(length = 1000)
  private String certificados; // idem

  // getters/setters
  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public Profissional getProfissional() { return profissional; }
  public void setProfissional(Profissional profissional) { this.profissional = profissional; }
  public String getFotos() { return fotos; }
  public void setFotos(String fotos) { this.fotos = fotos; }
  public String getCertificados() { return certificados; }
  public void setCertificados(String certificados) { this.certificados = certificados; }
  public String getFotoPerfil() { return fotoPerfil; }
  public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}