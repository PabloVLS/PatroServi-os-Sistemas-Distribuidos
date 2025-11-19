package com.example.patroservicosSD.servico1.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> naoEncontrado(EntityNotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("erro", "não_encontrado");
    body.put("mensagem", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> argumentoInvalido(IllegalArgumentException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("erro", "argumento_invalido");
    body.put("mensagem", ex.getMessage());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("erro", "validacao");
    body.put("mensagem", "Dados inválidos");
    return ResponseEntity.badRequest().body(body);
  }
}