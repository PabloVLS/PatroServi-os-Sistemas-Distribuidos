package com.example.patroservicosSD.servico1.mappers;

import com.example.patroservicosSD.servico1.dto.ClienteRequest;
import com.example.patroservicosSD.servico1.dto.ClienteResponse;
import com.example.patroservicosSD.servico1.model.Cliente;

public class ClienteMapper {
  public static Cliente paraEntidade(ClienteRequest req) {
    Cliente c = new Cliente();
    c.setNome(req.getNome());
    c.setEmail(req.getEmail());
    c.setNumero(req.getNumero());
    return c;
  }

  public static void atualizarEntidade(Cliente entidade, ClienteRequest req) {
    if (req.getNome() != null) entidade.setNome(req.getNome());
    if (req.getEmail() != null) entidade.setEmail(req.getEmail());
    if (req.getNumero() != null) entidade.setNumero(req.getNumero());
  }

  public static ClienteResponse paraResposta(Cliente entidade) {
    ClienteResponse r = new ClienteResponse();
    r.setId(entidade.getId());
    r.setNome(entidade.getNome());
    r.setEmail(entidade.getEmail());
    r.setNumero(entidade.getNumero());
    r.setDataCadastro(entidade.getDataCadastro());
    return r;
  }
}