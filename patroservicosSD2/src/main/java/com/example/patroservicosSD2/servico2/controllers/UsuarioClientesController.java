package com.example.patroservicosSD2.servico2.controllers;

import com.example.patroservicosSD2.servico2.dto.UsuarioClienteDtos;
import com.example.patroservicosSD2.servico2.services.UsuarioClientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Endpoints REST para credenciais de clientes.
 */
@RestController
@RequestMapping("/api/usuarios-clientes")
public class UsuarioClientesController {

    private final UsuarioClientesService service;

    public UsuarioClientesController(UsuarioClientesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioClienteDtos.Response> criar(@RequestBody UsuarioClienteDtos.CriarRequest req) {
        UsuarioClienteDtos.Response resp = service.criar(req);
        return ResponseEntity.status(201).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioClienteDtos.Response> obter(@PathVariable UUID id) {
        return ResponseEntity.ok(service.obter(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioClienteDtos.LoginResponse> login(@RequestBody UsuarioClienteDtos.LoginRequest req) {
        UsuarioClienteDtos.LoginResponse resp = service.autenticar(req);
        return ResponseEntity.ok(resp);
    }
}
