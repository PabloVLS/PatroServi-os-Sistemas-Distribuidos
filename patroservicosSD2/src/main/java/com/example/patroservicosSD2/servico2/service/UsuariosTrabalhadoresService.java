package com.example.patroservicosSD2.servico2.service;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioCreateRequest;
import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioLoginRequest;
import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioLoginResponse;
import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioResponse;
import com.example.patroservicosSD2.servico2.model.UsuariosTrabalhadores;
import com.example.patroservicosSD2.servico2.repository.UsuariosTrabalhadoresRepository;

@Service
public class UsuariosTrabalhadoresService {

    private final UsuariosTrabalhadoresRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuariosTrabalhadoresService(UsuariosTrabalhadoresRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioResponse criarUsuario(UsuarioCreateRequest req) {
        // Verifica duplicidade por login
        repository.findByLogin(req.getLogin()).ifPresent(u -> {
            throw new DataIntegrityViolationException("login já existe");
        });

        UsuariosTrabalhadores novo = new UsuariosTrabalhadores();
        novo.setLogin(req.getLogin());
        String hash = passwordEncoder.encode(req.getSenha());
        novo.setSenhaHash(hash);
        // associa profissional se veio no payload
        if (req.getProfissionalId() != null) {
            novo.setProfissionalId(req.getProfissionalId());
        }
        UsuariosTrabalhadores salvo = repository.save(novo);

        return new UsuarioResponse(
            salvo.getId(),
            salvo.getLogin(),
            salvo.getCriadoEm() != null ? salvo.getCriadoEm().toString() : null,
            salvo.getAtualizadoEm() != null ? salvo.getAtualizadoEm().toString() : null,
            salvo.getProfissionalId()
        );
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obterPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id é obrigatório");
        }
        UsuariosTrabalhadores u = repository.findById(id).orElseThrow(() -> new NoSuchElementException("usuario não encontrado"));
        return new UsuarioResponse(
            u.getId(),
            u.getLogin(),
            u.getCriadoEm() != null ? u.getCriadoEm().toString() : null,
            u.getAtualizadoEm() != null ? u.getAtualizadoEm().toString() : null,
            u.getProfissionalId()
        );
    }

    @Transactional(readOnly = true)
    public UsuarioLoginResponse autenticar(UsuarioLoginRequest req) {
        if (req == null || req.getLogin() == null || req.getSenha() == null) {
            throw new IllegalArgumentException("login e senha são obrigatórios");
        }
        UsuariosTrabalhadores u = repository.findByLogin(req.getLogin())
            .orElseThrow(() -> new NoSuchElementException("usuario não encontrado"));
        if (!passwordEncoder.matches(req.getSenha(), u.getSenhaHash())) {
            throw new NoSuchElementException("credenciais inválidas");
        }
        return new UsuarioLoginResponse(u.getId(), u.getLogin(), u.getProfissionalId());
    }
}
