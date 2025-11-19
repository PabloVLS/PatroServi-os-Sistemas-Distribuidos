package com.example.patroservicosSD.servico1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de status para health checks.
 * Utilizado pelo Gestor para monitorar a saúde do serviço.
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public String status() {
        return "{\"status\":\"ok\",\"service\":\"sd1\"}";
    }
}
