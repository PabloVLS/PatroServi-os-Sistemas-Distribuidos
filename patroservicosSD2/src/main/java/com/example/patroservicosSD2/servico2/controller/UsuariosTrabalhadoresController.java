package com.example.patroservicosSD2.servico2.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioCreateRequest;
import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioLoginRequest;
import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioLoginResponse;
import com.example.patroservicosSD2.servico2.dto.UsuarioDtos.UsuarioResponse;
import com.example.patroservicosSD2.servico2.service.UsuariosTrabalhadoresService;

import jakarta.validation.Valid;

/**
 * API REST de Usuários do SD2.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosTrabalhadoresController {

    private final UsuariosTrabalhadoresService service;

    public UsuariosTrabalhadoresController(UsuariosTrabalhadoresService service) {
        this.service = service;
    }

    /**
     * Cria um novo usuário com login e senha (hash armazenado em banco).
     */
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody UsuarioCreateRequest req) {
        try {
            UsuarioResponse resp = service.criarUsuario(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (DataIntegrityViolationException e) {
            // Login duplicado -> 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body("login já existe");
        }
    }

    /**
     * Autentica um usuário profissional.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioLoginRequest req) {
        try {
            UsuarioLoginResponse resp = service.autenticar(req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (java.util.NoSuchElementException e) {
            // Credenciais inválidas ou usuário não encontrado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login ou senha inválidos");
        }
    }

    /**
     * Retorna um usuário por id (sem expor hash de senha).
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obter(@PathVariable("id") Long id) {
        try {
            UsuarioResponse resp = service.obterPorId(id);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("usuario não encontrado");
        }
    }
}
