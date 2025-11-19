package com.example.patroservicosSD.servico1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuração estática alternativa para recursos.
 * OBS: Já temos WebConfig fazendo o mesmo mapeamento; manter apenas uma classe
 * para evitar duplicidade. Esta pode ser removida se WebConfig permanecer.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get("uploads");
    String location = "file:" + uploadDir.toAbsolutePath().toString() + "/";
    registry.addResourceHandler("/uploads/**").addResourceLocations(location);
  }
}