package com.example.patroservicosSD.servico1.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Serviço responsável por armazenar arquivos enviados (documentos e portfólio).
 *
 * - Cria a pasta "uploads" na raiz do serviço 1 (se não existir)
 * - Gera um nome único com prefixo (doc_cpf, doc_rg, port) + UUID
 * - Retorna uma URL ABSOLUTA (http://localhost:8082/uploads/...) para o frontend carregar via Gestor
 */
@Service
public class FileStorageService {
  private Path root;

  /** Diretório configurável para armazenar uploads (padrão: uploads) */
  @Value("${uploads.dir:uploads}")
  private String uploadsDir;

  /** URL base do serviço, utilizada para compor o link absoluto */
  @Value("${app.base-url:http://localhost:8082}")
  private String baseUrl;

  /** Salva o arquivo e devolve a URL pública acessível */
  public String salvar(MultipartFile file, String prefixo) {
    if (file == null || file.isEmpty()) return null;
    try {
      if (root == null) root = Paths.get(uploadsDir);
      if (!Files.exists(root)) Files.createDirectories(root);
      String originalTmp = file.getOriginalFilename();
      if (originalTmp == null || originalTmp.isBlank()) originalTmp = "arquivo";
      String nome = prefixo + "_" + UUID.randomUUID() + "_" + originalTmp.replaceAll("\\s+","_");
      Path destino = root.resolve(nome);
      Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
      return baseUrl + "/uploads/" + nome; // URL absoluta para evitar problemas via gateway
    } catch (IOException e) {
      throw new RuntimeException("Falha ao salvar arquivo: " + (file.getOriginalFilename() == null ? "arquivo" : file.getOriginalFilename()));
    }
  }
}