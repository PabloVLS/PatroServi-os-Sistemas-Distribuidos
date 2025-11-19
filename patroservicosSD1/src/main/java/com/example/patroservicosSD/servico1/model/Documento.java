package com.example.patroservicosSD.servico1.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "documentos")
public class Documento {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(unique = true)
  private String cpf;

  @Column(unique = true)
  private String rg;

  @Column(length = 10000000)
  private String fotoCpf;
  @Column(length = 10000000)
  private String fotoRg;

  private Boolean verificado = false;

  @OneToOne
  @JoinColumn(name = "profissional_id", unique = true, nullable = false)
  private Profissional profissional;

  // getters/setters
  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }
  public String getCpf() { return cpf; }
  public void setCpf(String cpf) { this.cpf = cpf; }
  public String getRg() { return rg; }
  public void setRg(String rg) { this.rg = rg; }
  public String getFotoCpf() { return fotoCpf; }
  public void setFotoCpf(String fotoCpf) { this.fotoCpf = fotoCpf; }
  public String getFotoRg() { return fotoRg; }
  public void setFotoRg(String fotoRg) { this.fotoRg = fotoRg; }
  public Boolean getVerificado() { return verificado; }
  public void setVerificado(Boolean verificado) { this.verificado = verificado; }
  public Profissional getProfissional() { return profissional; }
  public void setProfissional(Profissional profissional) { this.profissional = profissional; }
}