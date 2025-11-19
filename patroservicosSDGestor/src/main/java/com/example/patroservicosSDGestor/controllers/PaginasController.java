package com.example.patroservicosSDGestor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de PÁGINAS (views) do Gestor.
 * - Raiz (/) usa a página estática em static/index.html
 * - Demais rotas retornam templates Thymeleaf migrados para o Gestor
 */
@Controller
public class PaginasController {

    /** Encaminha para o arquivo estático index.html */
    @GetMapping("/")
    public String root() { return "forward:/index.html"; }

    /** Alias que redireciona para a raiz */
    @GetMapping("/home")
    public String home() { return "redirect:/"; }

    /** Tela de cadastro de profissional (arquivo estático) */
    @GetMapping("/profissional/cadastro")
    public String cadastroProfissional() { return "forward:/profissional-cadastro.html"; }

    /** Página pública de perfil de profissional (arquivo estático) */
    @GetMapping("/profissional/perfil-publico")
    public String perfilPublico() { return "forward:/perfil-profissional.html"; }

    /** Página de edição do próprio perfil (arquivo estático) */
    @GetMapping("/profissional/meu-perfil")
    public String meuPerfil() { return "forward:/profissional-meu-perfil.html"; }

    /** Tela de login de cliente (arquivo estático) */
    @GetMapping("/login")
    public String loginCliente() { return "forward:/loginCliente.html"; }

    /** Tela de login de profissional (arquivo estático) */
    @GetMapping("/profissional/login")
    public String loginProfissional() { return "forward:/loginProfissional.html"; }
}
