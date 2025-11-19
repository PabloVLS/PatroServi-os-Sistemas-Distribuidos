package com.example.patroservicosSD.servico1.dto;

import java.util.UUID;

public class ProfissionalPerfilResponse {
  private UUID profissionalId;
  private String nome;
  private String numero;
  private String email;
  private String profissao;
  private String descricao;
  private String cidade;
  private String estado;

  private DocumentoResumo documento;
  private PerfilResumo perfil;

  public static class DocumentoResumo {
    private UUID id;
    private String cpf;
    private String rg;
    private String fotoCpf;
    private String fotoRg;
    private Boolean verificado;

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
  }

  public static class PerfilResumo {
    private UUID id;
    private String fotoPerfil;
    private String fotos;
    private String certificados;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFotos() { return fotos; }
    public void setFotos(String fotos) { this.fotos = fotos; }
    public String getCertificados() { return certificados; }
    public void setCertificados(String certificados) { this.certificados = certificados; }
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
  }

  public UUID getProfissionalId() { return profissionalId; }
  public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }
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
  public DocumentoResumo getDocumento() { return documento; }
  public void setDocumento(DocumentoResumo documento) { this.documento = documento; }
  public PerfilResumo getPerfil() { return perfil; }
  public void setPerfil(PerfilResumo perfil) { this.perfil = perfil; }
}