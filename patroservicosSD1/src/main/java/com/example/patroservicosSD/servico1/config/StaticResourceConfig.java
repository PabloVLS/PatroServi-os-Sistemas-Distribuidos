/*
 * ========================================================================
 * ARQUIVO NÃO UTILIZADO - CÓDIGO COMENTADO PARA MANUTENÇÃO
 * ========================================================================
 * MOTIVO: Duplicação de funcionalidade com WebConfig
 *
 * StaticResourceConfig duplica a configuração de mapeamento de recursos
 * estáticos presente em WebConfig.java. Como ambas as classes implementam
 * WebMvcConfigurer e registram handlers para "/uploads/**", esta configuração
 * redundante foi comentada. WebConfig permanece ativo para servir os uploads.
 *
 * DATA: 2024
 * ========================================================================
 */

/*
package com.example.patroservicosSD.servico1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

// Configuração estática alternativa para recursos.
// OBS: Já temos WebConfig fazendo o mesmo mapeamento; manter apenas uma classe
// para evitar duplicidade. Esta pode ser removida se WebConfig permanecer.
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
  @Value("${uploads.dir:uploads}")
  private String uploadsDir;
  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get(uploadsDir);
    String location = "file:" + uploadDir.toAbsolutePath().toString() + "/";
    registry.addResourceHandler("/uploads/**").addResourceLocations(location);
  }
}
*/