package com.example.patroservicosSD.servico1.services.impl;

import com.example.patroservicosSD.servico1.dto.ProfissionalCadastroRequest;
import com.example.patroservicosSD.servico1.dto.ProfissionalCadastroResponse;
import com.example.patroservicosSD.servico1.dto.ProfissionalPerfilRequest;
import com.example.patroservicosSD.servico1.dto.ProfissionalPerfilResponse;
import com.example.patroservicosSD.servico1.model.Documento;
import com.example.patroservicosSD.servico1.model.PerfilProfissional;
import com.example.patroservicosSD.servico1.model.Profissional;
import com.example.patroservicosSD.servico1.repositories.DocumentoRepository;
import com.example.patroservicosSD.servico1.repositories.PerfilProfissionalRepository;
import com.example.patroservicosSD.servico1.repositories.ProfissionalRepository;
import com.example.patroservicosSD.servico1.services.ProfissionalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProfissionalServiceImpl implements ProfissionalService {

  private final ProfissionalRepository repositorio;
  private final DocumentoRepository repositorioDocumento;
  private final PerfilProfissionalRepository repositorioPerfil;

  public ProfissionalServiceImpl(ProfissionalRepository repositorio,
                                 DocumentoRepository repositorioDocumento,
                                 PerfilProfissionalRepository repositorioPerfil) {
    this.repositorio = repositorio;
    this.repositorioDocumento = repositorioDocumento;
    this.repositorioPerfil = repositorioPerfil;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Profissional> listarTodos() {
    return repositorio.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Profissional buscarPorId(UUID id) {
    return repositorio.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + id));
  }

  public Profissional criarProfissional(Profissional profissional) {
    if (profissional.getEmail() != null && repositorio.existsByEmailIgnoreCase(profissional.getEmail())) {
      throw new IllegalArgumentException("Email já cadastrado");
    }
    if (profissional.getNumero() != null && repositorio.existsByNumero(profissional.getNumero())) {
      throw new IllegalArgumentException("Número de telefone já cadastrado");
    }
    profissional.setId(null);
    return repositorio.save(profissional);
  }

  @Override
  public Profissional atualizar(UUID id, Profissional dados) {
    Profissional existente = buscarPorId(id);

    if (dados.getNome() != null) existente.setNome(dados.getNome());

    if (dados.getNumero() != null && !dados.getNumero().equals(existente.getNumero())) {
      if (repositorio.existsByNumero(dados.getNumero())) {
        throw new IllegalArgumentException("Número de telefone já cadastrado");
      }
      existente.setNumero(dados.getNumero());
    }

    if (dados.getEmail() != null && (existente.getEmail() == null || !dados.getEmail().equalsIgnoreCase(existente.getEmail()))) {
      if (repositorio.existsByEmailIgnoreCase(dados.getEmail())) {
        throw new IllegalArgumentException("Email já cadastrado");
      }
      existente.setEmail(dados.getEmail());
    }

    if (dados.getProfissao() != null) existente.setProfissao(dados.getProfissao());
    if (dados.getDescricao() != null) existente.setDescricao(dados.getDescricao());
    if (dados.getCidade() != null) existente.setCidade(dados.getCidade());
    if (dados.getEstado() != null) existente.setEstado(dados.getEstado());

    return repositorio.save(existente);
  }

  @Override
  public void deletar(UUID id) {
    if (!repositorio.existsById(id)) {
      throw new EntityNotFoundException("Profissional não encontrado: " + id);
    }
    // Apaga dependências 1:1 primeiro (evita violação de FK)
    repositorioDocumento.deleteByProfissionalId(id);
    repositorioPerfil.deleteByProfissionalId(id);
    repositorio.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Profissional> buscarPorCidade(String cidade) {
    return repositorio.findByCidadeIgnoreCase(cidade);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Profissional> buscarPorProfissao(String profissao) {
    return repositorio.findByProfissaoIgnoreCase(profissao);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Profissional> buscarPorCidadeEProfissao(String cidade, String profissao) {
    return repositorio.findByCidadeIgnoreCaseAndProfissaoIgnoreCase(cidade, profissao);
  }

  @Override
  @Transactional
  public ProfissionalCadastroResponse criarCadastroCompleto(ProfissionalCadastroRequest req) {
    // validações básicas
    if (req.getEmail() != null && repositorio.existsByEmailIgnoreCase(req.getEmail()))
      throw new IllegalArgumentException("Email já cadastrado");
    if (req.getNumero() != null && repositorio.existsByNumero(req.getNumero()))
      throw new IllegalArgumentException("Número já cadastrado");
    if (req.getCpf() != null && repositorioDocumento.existsByCpf(req.getCpf()))
      throw new IllegalArgumentException("CPF já cadastrado");
    if (req.getRg() != null && repositorioDocumento.existsByRg(req.getRg()))
      throw new IllegalArgumentException("RG já cadastrado");

    // profissional
    Profissional profissional = new Profissional();
    profissional.setNome(req.getNome());
    profissional.setNumero(req.getNumero());
    profissional.setEmail(req.getEmail());
    profissional.setProfissao(req.getProfissao());
    profissional.setDescricao(req.getDescricao());
    profissional.setCidade(req.getCidade());
    profissional.setEstado(req.getEstado());
    profissional.setId(null);
    profissional = repositorio.save(profissional);

    // documentos
    Documento documento = new Documento();
    documento.setCpf(req.getCpf());
    documento.setRg(req.getRg());
    // ProfissionalCadastroRequest não define getFotoCpf(), manter nulo até que a informação esteja disponível
    documento.setFotoCpf(null);
    // ProfissionalCadastroRequest não define getFotoRg(), manter nulo até que a informação esteja disponível
    documento.setFotoRg(null);
    documento.setVerificado(false);
    documento.setProfissional(profissional);
    documento.setId(null);
    documento = repositorioDocumento.save(documento);

    // perfil
    PerfilProfissional perfil = new PerfilProfissional();
    perfil.setFotoPerfil(req.getFotoPerfil());
    perfil.setFotos(req.getFotos());
    perfil.setCertificados(req.getCertificados());
    perfil.setProfissional(profissional);
    perfil.setId(null);
    perfil = repositorioPerfil.save(perfil);

    ProfissionalCadastroResponse resp = new ProfissionalCadastroResponse();
    resp.setProfissionalId(profissional.getId());
    resp.setDocumentoId(documento.getId());
    resp.setPerfilId(perfil.getId());
    resp.setDocumentoVerificado(documento.getVerificado());
    return resp;
  }

  @Transactional(readOnly = true)
  @Override
  public ProfissionalPerfilResponse buscarPerfil(UUID profissionalId) {
    Profissional p = buscarPorId(profissionalId);

    Documento doc = repositorioDocumento.findByProfissionalId(profissionalId).orElse(null);
    PerfilProfissional perfil = repositorioPerfil.findByProfissionalId(profissionalId).orElse(null);

    ProfissionalPerfilResponse resp = new ProfissionalPerfilResponse();
    resp.setProfissionalId(p.getId());
    resp.setNome(p.getNome());
    resp.setNumero(p.getNumero());
    resp.setEmail(p.getEmail());
    resp.setProfissao(p.getProfissao());
    resp.setDescricao(p.getDescricao());
    resp.setCidade(p.getCidade());
    resp.setEstado(p.getEstado());

    if (doc != null) {
      ProfissionalPerfilResponse.DocumentoResumo d = new ProfissionalPerfilResponse.DocumentoResumo();
      d.setId(doc.getId());
      d.setCpf(doc.getCpf());
      d.setRg(doc.getRg());
      d.setFotoCpf(doc.getFotoCpf());
      d.setFotoRg(doc.getFotoRg());
      d.setVerificado(doc.getVerificado());
      resp.setDocumento(d);
    }

    if (perfil != null) {
      ProfissionalPerfilResponse.PerfilResumo pr = new ProfissionalPerfilResponse.PerfilResumo();
      pr.setId(perfil.getId());
      pr.setFotoPerfil(perfil.getFotoPerfil());
      pr.setFotos(perfil.getFotos());
      pr.setCertificados(perfil.getCertificados());
      resp.setPerfil(pr);
    }

    return resp;
  }

  @Override
  public ProfissionalPerfilResponse atualizarPerfil(UUID profissionalId, ProfissionalPerfilRequest req) {
    Profissional existente = buscarPorId(profissionalId);

    // profissional
    if (req.getNome() != null) existente.setNome(req.getNome());
    if (req.getNumero() != null && !req.getNumero().equals(existente.getNumero())) {
      if (repositorio.existsByNumero(req.getNumero())) throw new IllegalArgumentException("Número já cadastrado");
      existente.setNumero(req.getNumero());
    }
    if (req.getEmail() != null && (existente.getEmail() == null || !req.getEmail().equalsIgnoreCase(existente.getEmail()))) {
      if (repositorio.existsByEmailIgnoreCase(req.getEmail())) throw new IllegalArgumentException("Email já cadastrado");
      existente.setEmail(req.getEmail());
    }
    if (req.getProfissao() != null) existente.setProfissao(req.getProfissao());
    if (req.getDescricao() != null) existente.setDescricao(req.getDescricao());
    if (req.getCidade() != null) existente.setCidade(req.getCidade());
    if (req.getEstado() != null) existente.setEstado(req.getEstado());
    repositorio.save(existente);

    // documento (cria se não existir)
    Documento doc = repositorioDocumento.findByProfissionalId(profissionalId).orElseGet(() -> {
      Documento novo = new Documento();
      novo.setProfissional(existente);
      novo.setVerificado(false);
      return novo;
    });

    if (req.getCpf() != null && (doc.getCpf() == null || !req.getCpf().equals(doc.getCpf()))) {
      if (repositorioDocumento.existsByCpf(req.getCpf())) throw new IllegalArgumentException("CPF já cadastrado");
      doc.setCpf(req.getCpf());
    }
    if (req.getRg() != null && (doc.getRg() == null || !req.getRg().equals(doc.getRg()))) {
      if (repositorioDocumento.existsByRg(req.getRg())) throw new IllegalArgumentException("RG já cadastrado");
      doc.setRg(req.getRg());
    }
    if (req.getFotoCpf() != null) doc.setFotoCpf(req.getFotoCpf());
    if (req.getFotoRg() != null) doc.setFotoRg(req.getFotoRg());
    doc = repositorioDocumento.save(doc);

    // perfil (cria se não existir)
    PerfilProfissional perfil = repositorioPerfil.findByProfissionalId(profissionalId).orElseGet(() -> {
      PerfilProfissional novo = new PerfilProfissional();
      novo.setProfissional(existente);
      return novo;
    });
    if (req.getFotoPerfil() != null) perfil.setFotoPerfil(req.getFotoPerfil());
    if (req.getFotos() != null) perfil.setFotos(req.getFotos());
    if (req.getCertificados() != null) perfil.setCertificados(req.getCertificados());
    perfil = repositorioPerfil.save(perfil);

    return buscarPerfil(profissionalId);
  }

  @Override
  public ProfissionalCadastroResponse criarCadastroCompletoComImagens(ProfissionalCadastroRequest req, String pathCpf, String pathRg) {
    // reutiliza lógica do criarCadastroCompleto(req) mas substitui fotoCpf/fotoRg
    ProfissionalCadastroResponse base = criarCadastroCompleto(req);
    Documento doc = repositorioDocumento.findByProfissionalId(base.getProfissionalId()).orElse(null);
    if (doc != null) {
      if (pathCpf != null) doc.setFotoCpf(pathCpf);
      if (pathRg != null) doc.setFotoRg(pathRg);
      repositorioDocumento.save(doc);
    }
    // atualiza perfil (fotos já em req.getFotos())
    return base;
  }
}