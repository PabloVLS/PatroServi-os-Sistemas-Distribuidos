package com.example.patroservicosSD.servico1.mappers;

import com.example.patroservicosSD.servico1.dto.ProfissionalResponse;
import com.example.patroservicosSD.servico1.model.Profissional;
import com.example.patroservicosSD.servico1.model.PerfilProfissional;

public final class ProfissionalMapper {

  private ProfissionalMapper() {}

  public static ProfissionalResponse paraResposta(Profissional entidade) {
    ProfissionalResponse r = new ProfissionalResponse();
    r.setId(entidade.getId());
    r.setNome(entidade.getNome());
    r.setNumero(entidade.getNumero());
    r.setEmail(entidade.getEmail());
    r.setProfissao(entidade.getProfissao());
    r.setDescricao(entidade.getDescricao());
    r.setCidade(entidade.getCidade());
    r.setEstado(entidade.getEstado());
    
    // Popula perfil se existir
    PerfilProfissional perfil = entidade.getPerfil();
    if (perfil != null) {
      ProfissionalResponse.PerfilResumo pr = new ProfissionalResponse.PerfilResumo();
      pr.setId(perfil.getId());
      pr.setFotoPerfil(perfil.getFotoPerfil());
      pr.setFotos(perfil.getFotos());
      pr.setCertificados(perfil.getCertificados());
      r.setPerfil(pr);
    }
    
    return r;
  }
}