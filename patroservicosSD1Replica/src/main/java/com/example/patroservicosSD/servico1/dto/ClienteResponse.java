package com.example.patroservicosSD.servico1.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClienteResponse {
  private UUID id;
  private String nome;
  private String email;
  private String numero;
  private LocalDateTime dataCadastro;

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