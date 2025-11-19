package com.example.patroservicosSD.servico1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

  @GetMapping("/home")
  public String home() { return "redirect:/"; }

  @GetMapping("/profissional/cadastro")
  public String cadastroProfissional() { return "profissional-cadastro"; }

  @GetMapping("/profissional/perfil-publico")
  public String perfilPublico() { return "perfil-profissional"; }
}