# Configuração de Uploads Centralizada

## 📋 Resumo das Correções

Todos os serviços (SD1, SD1Replica e Gestor) foram configurados para usar a mesma pasta centralizada de uploads.

### ✅ Alterações Realizadas

#### 1. **patroservicosSD1** - `application.properties`
```
uploads.dir=C:/Users/Pichau/Desktop/Outros/Aula 1 - Sistemas Distribuidos/TrabalhoPatroServicos/uploads
```

#### 2. **patroservicosSD1Replica** - `application.properties`
```
uploads.dir=C:/Users/Pichau/Desktop/Outros/Aula 1 - Sistemas Distribuidos/TrabalhoPatroServicos/uploads
```

#### 3. **patroservicosSDGestor** - `application.properties`
```
uploads.dir=C:/Users/Pichau/Desktop/Outros/Aula 1 - Sistemas Distribuidos/TrabalhoPatroServicos/uploads
```

---

## 🎯 Como Funciona Agora

### Fluxo de Upload

1. **Cliente** faz upload via Frontend (profissional-cadastro.html)
   - Envia arquivo para Gestor (`POST /api/uploads?prefixo=doc_cpf`)

2. **Gestor** recebe e repassa para SD1
   - Roteia para `http://localhost:8082/api/uploads?prefixo=doc_cpf`
   - SD1 salva na pasta centralizada

3. **SD1 ou SD1Replica** armazena
   - FileStorageService salva em: `C:/Users/Pichau/Desktop/Outros/Aula 1 - Sistemas Distribuidos/TrabalhoPatroServicos/uploads`
   - Retorna URL absoluta: `http://localhost:8081/uploads/doc_cpf_UUID_nome.jpg`

4. **Gestor serve o arquivo**
   - StaticResourceConfig mapeia `/uploads/**` para a pasta centralizada
   - Frontend acessa via: `http://localhost:8081/uploads/...`

---

## 📁 Estrutura de Pastas

```
TrabalhoPatroServicos/
├── uploads/                           ← PASTA CENTRALIZADA (compartilhada)
│   ├── doc_cpf_UUID_nome.jfif
│   ├── doc_rg_UUID_nome.jfif
│   ├── port_UUID_nome.jfif
│   └── ...
├── patroservicosSD1/
│   ├── src/
│   ├── target/
│   └── (uploads/ local será ignorado)
├── patroservicosSD1Replica/
│   ├── src/
│   ├── target/
│   └── (uploads/ local será ignorado)
├── patroservicosSDGestor/
├── patroservicosSD2/
├── patroservicosSD3/
└── patroservicosSDCliente/
```

---

## ⚙️ Configuração Técnica

### Propriedades Spring (application.properties)

| Serviço | Propriedade | Valor |
|---------|-------------|-------|
| SD1 | `uploads.dir` | `C:/Users/.../TrabalhoPatroServicos/uploads` |
| SD1 Replica | `uploads.dir` | `C:/Users/.../TrabalhoPatroServicos/uploads` |
| Gestor | `uploads.dir` | `C:/Users/.../TrabalhoPatroServicos/uploads` |

### Classes Java que Usam essa Configuração

**SD1:**
- `FileStorageService.java` - Salva arquivos com `@Value("${uploads.dir:uploads}")`
- `UploadController.java` - Endpoint `/api/uploads` que chama FileStorageService

**SD1Replica:**
- Mesma estrutura do SD1

**Gestor:**
- `StaticResourceConfig.java` - Mapeia `/uploads/**` para a pasta
- `UploadsGatewayController.java` - Roteia requisições para SD1

---

## 🔧 Passos Finais

### 1. Limpar Pastas Locais (Opcional)
Se houver pasta `uploads/` dentro de `patroservicosSD1/` ou `patroservicosSD1Replica/`:
```bash
# Pode ser deletada, pois não será mais usada
rm -r patroservicosSD1/uploads/
rm -r patroservicosSD1Replica/uploads/
```

### 2. Garantir Que a Pasta Centralizada Existe
```bash
mkdir -p "C:\Users\Pichau\Desktop\Outros\Aula 1 - Sistemas Distribuidos\TrabalhoPatroServicos\uploads"
```

### 3. Reiniciar os Serviços
```bash
# Terminal 1 - SD1
cd patroservicosSD1
./mvnw spring-boot:run

# Terminal 2 - SD1Replica
cd patroservicosSD1Replica
./mvnw spring-boot:run

# Terminal 3 - Gestor
cd patroservicosSDGestor
./mvnw spring-boot:run
```

### 4. Testar Upload
- Acesse: `http://localhost:8081/`
- Faça login como profissional
- Upload de fotos (CPF, RG, Portfólio)
- Verifique se os arquivos aparecem em: `C:\Users\Pichau\Desktop\Outros\Aula 1 - Sistemas Distribuidos\TrabalhoPatroServicos\uploads\`

---

## ✨ Benefícios

✅ **Pasta única**: Todas os arquivos em um lugar centralizado  
✅ **SD1 e SD1Replica sincronizadas**: Apontam para a mesma pasta  
✅ **Gestor distribui**: Pode servir arquivos via gateway  
✅ **Redundância**: Se SD1 ficar down, SD1Replica acessa os mesmos uploads  
✅ **Backup fácil**: Uma pasta para fazer backup  

---

## 📝 Notas Importantes

- O caminho usa `/` (forward slash) no `application.properties` do Spring Boot, mesmo no Windows
- A pasta **deve existir** antes de rodar o serviço (SpringBoot cria se não existir, mas é bom garantir)
- Se mudar para outro computador, ajuste o caminho em **todas as 3 properties files**

