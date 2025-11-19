package com.example.patroservicosSD.servico1.dto;

import java.util.UUID;

public class ProfissionalCadastroResponse {
  private UUID profissionalId;
  private UUID documentoId;
  private UUID perfilId;
  private Boolean documentoVerificado;

  public UUID getProfissionalId() { return profissionalId; }
  public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }
  public UUID getDocumentoId() { return documentoId; }
  public void setDocumentoId(UUID documentoId) { this.documentoId = documentoId; }
  public UUID getPerfilId() { return perfilId; }
  public void setPerfilId(UUID perfilId) { this.perfilId = perfilId; }
  public Boolean getDocumentoVerificado() { return documentoVerificado; }
  public void setDocumentoVerificado(Boolean documentoVerificado) { this.documentoVerificado = documentoVerificado; }
}