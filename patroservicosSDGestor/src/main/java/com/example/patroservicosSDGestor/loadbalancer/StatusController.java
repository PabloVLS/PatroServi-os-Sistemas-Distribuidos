package com.example.patroservicosSDGestor.loadbalancer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint de status que pode ser usado para health checks.
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public String status() {
        return "{\"status\":\"ok\"}";
    }
}
