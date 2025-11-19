package com.example.patroservicosSD2.servico2.services;

import com.example.patroservicosSD2.servico2.dto.UsuarioClienteDtos;
import com.example.patroservicosSD2.servico2.model.UsuarioClientes;
import com.example.patroservicosSD2.servico2.repositories.UsuarioClientesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UsuarioClientesService {

    private final UsuarioClientesRepository repo;

    public UsuarioClientesService(UsuarioClientesRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public UsuarioClienteDtos.Response criar(UsuarioClienteDtos.CriarRequest req) {
        UsuarioClientes u = new UsuarioClientes();
        u.setLogin(req.getLogin());
        u.setSenha(req.getSenha()); // AVISO: em produção utilize hash (BCrypt/Argon2)
        u.setClienteId(req.getClienteId());
        UsuarioClientes salvo = repo.save(u);

        UsuarioClienteDtos.Response resp = new UsuarioClienteDtos.Response();
        resp.setId(salvo.getId());
        resp.setLogin(salvo.getLogin());
        resp.setClienteId(salvo.getClienteId());
        return resp;
    }

    @Transactional(readOnly = true)
    public UsuarioClienteDtos.Response obter(UUID id) {
        if (id == null) throw new IllegalArgumentException("UUID id não pode ser nulo");
        UsuarioClientes u = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário cliente não encontrado"));
        UsuarioClienteDtos.Response resp = new UsuarioClienteDtos.Response();
        resp.setId(u.getId());
        resp.setLogin(u.getLogin());
        resp.setClienteId(u.getClienteId());
        return resp;
    }

    @Transactional(readOnly = true)
    public UsuarioClienteDtos.LoginResponse autenticar(UsuarioClienteDtos.LoginRequest req) {
        UsuarioClienteDtos.LoginResponse resp = new UsuarioClienteDtos.LoginResponse();
        repo.findByLogin(req.getLogin())
            .filter(u -> u.getSenha().equals(req.getSenha())) // AVISO: comparar hash em produção
            .ifPresent(u -> {
                resp.setAutenticado(true);
                resp.setUsuarioId(u.getId());
                resp.setClienteId(u.getClienteId());
                resp.setLogin(u.getLogin());
            });
        return resp;
    }
}
