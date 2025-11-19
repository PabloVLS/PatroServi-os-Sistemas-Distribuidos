package com.example.patroservicosSD.servico1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Exibe a pasta física "uploads" como recursos estáticos em /uploads/**.
 * Assim, URLs retornadas pelo FileStorageService (http://localhost:8082/uploads/arquivo.jpg)
 * são servidas corretamente pelo serviço SD1.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:" + uploadDir.toString() + "/")
        .setCachePeriod(3600);
  }
}
