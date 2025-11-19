package com.example.patroservicosSDGestor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Serve arquivos de uploads compartilhados em /uploads/** via Gestor.
 * Define a localização por meio da propriedade uploads.dir.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
  @Value("${uploads.dir:uploads}")
  private String uploadsDir;

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get(uploadsDir);
    String location = "file:" + uploadDir.toAbsolutePath().toString() + "/";
    registry.addResourceHandler("/uploads/**").addResourceLocations(location).setCachePeriod(3600);
  }
}
