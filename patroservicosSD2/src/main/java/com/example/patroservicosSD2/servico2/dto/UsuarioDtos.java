package com.example.patroservicosSD2.servico2.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * DTOs para criação e resposta de usuário.
 */
public class UsuarioDtos {

    /**
     * Requisição de criação de usuário.
     */
    public static class UsuarioCreateRequest {
        @NotBlank(message = "login é obrigatório")
        private String login;
        @NotBlank(message = "senha é obrigatória")
        private String senha;
        // opcional: id do profissional recém criado (associação lógica)
        private UUID profissionalId;

        public UsuarioCreateRequest() {}
        public UsuarioCreateRequest(String login, String senha, UUID profissionalId) {
            this.login = login;
            this.senha = senha;
            this.profissionalId = profissionalId;
        }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        public UUID getProfissionalId() { return profissionalId; }
        public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }
    }

    /**
     * Resposta de usuário (sem expor hash de senha).
     */
    public static class UsuarioResponse {
        private Long id;
        private String login;
        private String criadoEm; // ISO-8601 string
        private String atualizadoEm; // ISO-8601 string
        private UUID profissionalId;

        public UsuarioResponse() {}
        public UsuarioResponse(Long id, String login, String criadoEm, String atualizadoEm, UUID profissionalId) {
            this.id = id;
            this.login = login;
            this.criadoEm = criadoEm;
            this.atualizadoEm = atualizadoEm;
            this.profissionalId = profissionalId;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getCriadoEm() { return criadoEm; }
        public void setCriadoEm(String criadoEm) { this.criadoEm = criadoEm; }
        public String getAtualizadoEm() { return atualizadoEm; }
        public void setAtualizadoEm(String atualizadoEm) { this.atualizadoEm = atualizadoEm; }
        public UUID getProfissionalId() { return profissionalId; }
        public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }
    }

    /**
     * Requisição de login de usuário (profissional).
     */
    public static class UsuarioLoginRequest {
        @NotBlank
        private String login;
        @NotBlank
        private String senha;

        public UsuarioLoginRequest() {}
        public UsuarioLoginRequest(String login, String senha) {
            this.login = login;
            this.senha = senha;
        }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    /**
     * Resposta de login: dados mínimos do usuário.
     */
    public static class UsuarioLoginResponse {
        private Long id;
        private String login;
        private UUID profissionalId;

        public UsuarioLoginResponse() {}
        public UsuarioLoginResponse(Long id, String login, UUID profissionalId) {
            this.id = id;
            this.login = login;
            this.profissionalId = profissionalId;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getLogin() { return login; }
        public void setLogin(String login) { this.login = login; }
        public UUID getProfissionalId() { return profissionalId; }
        public void setProfissionalId(UUID profissionalId) { this.profissionalId = profissionalId; }
    }
}
