package com.example.patroservicosSD2.servico2.dto;

import java.util.UUID;

/**
 * DTOs para criação e autenticação de usuários clientes (credenciais de Cliente).
 */
public class UsuarioClienteDtos {

    public static class CriarRequest {
        private String login; // geralmente o email do cliente
        private String senha; // OBS: armazenada em texto simples conforme entidade atual
        private UUID clienteId; // vínculo lógico com Cliente do Serviço 1

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        public UUID getClienteId() { return clienteId; }
        public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    }

    public static class Response {
        private UUID id;
        private String login;
        private UUID clienteId;

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public UUID getClienteId() { return clienteId; }
        public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    }

    public static class LoginRequest {
        private String login;
        private String senha;

        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    public static class LoginResponse {
        private boolean autenticado;
        private UUID usuarioId;
        private UUID clienteId;
        private String login;

        public boolean isAutenticado() { return autenticado; }
        public void setAutenticado(boolean autenticado) { this.autenticado = autenticado; }
        public UUID getUsuarioId() { return usuarioId; }
        public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
        public UUID getClienteId() { return clienteId; }
        public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
    }
}
