# Pente Fino - Código Não Utilizado do Projeto

**Data:** 14 de Novembro de 2025  
**Escopo:** Revisão de código morto, não-utilizado, obsoleto ou duplicado em todo o projeto patroservicosSD.

---

## 1. ARQUIVOS DE TESTE VAZIOS (COMENTADOS)

✅ **Status:** JÁ COMENTADOS

- `patroservicosSD1/src/test/java/com/example/patroservicosSD/PatroservicosSdApplicationTests.java`
  - **Motivo:** Teste placeholder vazio gerado automaticamente pelo Spring Boot. Nunca implementado.
  
- `patroservicosSDGestor/src/test/java/com/example/patroservicosSDGestor/PatroservicosSdGestorApplicationTests.java`
  - **Motivo:** Teste placeholder vazio gerado automaticamente pelo Spring Boot. Nunca implementado.
  
- `patroservicosSD2/src/test/java/com/example/patroservicosSD2/PatroservicosSd2ApplicationTests.java`
  - **Motivo:** Teste placeholder vazio gerado automaticamente pelo Spring Boot. Nunca implementado.

---

## 2. ARQUIVOS JAVASCRIPT INTEIRAMENTE COMENTADOS OU OBSOLETOS (RECOMENDADO COMENTAR)

⚠️ **Status:** PENDENTES DE COMENTÁRIO

### 2.1 `patroservicosSDGestor/src/main/resources/static/js/home.js`
- **Status:** ✅ JÁ COMENTADO
- **Conteúdo:** Todas as funções estão precedidas por `//` (comentadas).
- **Motivo:** Arquivo antigo/descontinuado. Funcionalidades de listagem de profissionais parecem ter sido movidas para outro lugar.
- **Ação:** Mantido comentado para referência histórica.

### 2.2 `patroservicosSD1/src/main/resources/static/js/home.js`
- **Conteúdo:** Similarmente comentado (espelho do acima).
- **Motivo:** Mesmo que 2.1.
- **Recomendação:** Comentar também com bloco `/* ARQUIVO NÃO UTILIZADO */`.

### 2.3 `patroservicosSDCliente/js/home.js`
- **Conteúdo:** Contém funções, porém muitas estão desativadas ou não integradas.
- **Motivo:** Cliente frontend (HTML static) pode não estar sendo usado; verificar se integrado com Gestor.
- **Recomendação:** Revisar se cliente é ativo ou descontinuado.

### 2.4 Múltiplos `perfil-profissional.js` comentados
- Locais:
  - `patroservicosSDGestor/src/main/resources/static/js/perfil-profissional.js`
  - `patroservicosSD1/src/main/resources/static/js/perfil-profissional.js`
- **Motivo:** Totalmente comentados (precedidos por `//`). Funcionalidades reimplementadas em outro lugar.
- **Recomendação:** Comentar com bloco explicativo.

### 2.5 Múltiplos `perfil-publico.js` comentados
- Locais:
  - `patroservicosSD1/src/main/resources/static/js/perfil-publico.js`
  - `patroservicosSDGestor/src/main/resources/static/js/perfil-publico.js`
- **Motivo:** Parcialmente comentados. Funções como `carregarPerfilPublico()`, `iniciarChat()`, `atualizarStatusVerificado()` estão comentadas.
- **Recomendação:** Revisar: se as funções não são chamadas, comentar todo o arquivo; se são chamadas, deixar.

---

## 3. MÉTODOS/FUNÇÕES NÃO UTILIZADOS EM JAVASCRIPT

⚠️ **Status:** DETECTADOS, NÃO COMENTADOS

### 3.1 Em `patroservicosSD1/src/main/resources/static/js/perfil-profissional.js`
```javascript
function getParam(nome) { ... }
async function carregarPerfil() { ... }
async function salvarPerfil() { ... }
```
- **Motivo:** Todas comentadas. Parecem ser versão antiga de funcionalidade de edição de perfil.
- **Recomendação:** Se nunca foram usadas, comentar arquivo inteiro.

### 3.2 Em `patroservicosSDGestor/src/main/resources/static/js/perfil-profissional.js`
- Idem acima.

### 3.3 Em `patroservicosSDCliente/js/perfil-publico.js`
```javascript
function iniciarChat() { ... }  // alert('Iniciar chat...')
function setRating(rating) { ... }
function highlightStars(rating) { ... }
```
- **Motivo:** Funções existem mas parecem ser stubs (apenas alertas, sem implementação real).
- **Recomendação:** Se não são chamadas, comentar. Se são, deixar como stub.

### 3.4 Em `patroservicosSD1/src/main/resources/static/index.html` e `patroservicosSDCliente/perfil-cliente.html`
```javascript
function enviarMensagem() { alert(...); }
function solicitarServico() { alert(...); }
function compartilharPerfil() { alert(...) ou navigator.share(...); }
```
- **Motivo:** Stubs (apenas alertas, sem lógica real).
- **Status:** Botões estão no HTML, mas não há backend para esses endpoints.

---

## 4. BLOCOS HTML COMENTADOS (ESTRUTURAS ANTIGAS)

⚠️ **Status:** DETECTADOS, NÃO COMENTADOS

### 4.1 Em `patroservicosSD1/src/main/resources/templates/perfil-profissional.html`
- Seção comentada: `<!-- Ações Rápidas ... -->`
- Seção comentada: `<!-- Mensagem de Erro ... -->`
- Seção comentada: `<!-- Bootstrap JS ... -->`
- **Motivo:** Código HTML antigo/prototipado, não integrado.
- **Recomendação:** Comentar blocos com explicação.

### 4.2 Em `patroservicosSDGestor/src/main/resources/templates/perfil-profissional.html`
- Idem acima.

### 4.3 Em `patroservicosSDCliente/perfil-cliente.html`
- Funções vazias/incompletas: `enviarMensagem()`, `solicitarServico()`.
- **Recomendação:** Revisar se frontend cliente é ativo; se não, considerar mover para `/disabled` ou comentar.

---

## 5. MÉTODOS JAVA NÃO UTILIZADOS

⚠️ **Status:** DETECTADOS, NÃO COMENTADOS

### 5.1 Em `ClienteServiceImpl.java` (patroservicosSD1)
```java
public void deletar(UUID id) { ... }
public Cliente buscarPorEmail(String email) { ... }
public List<Cliente> buscarPorNome(String nome) { ... }
```
- **Motivo:** Métodos existem no contrato `ClienteService`, mas podem não ser chamados em nenhum controller.
- **Recomendação:** Fazer busca de referências no projeto; se não forem usados, comentar.

### 5.2 Em `UsuariosTrabalhadoresService.java` (patroservicosSD2)
```java
public UsuarioResponse obterPorId(Long id) { ... }
```
- **Motivo:** Método existe, mas pode não estar exposto em nenhum endpoint.
- **Recomendação:** Verificar se é chamado; se não, comentar.

### 5.3 Em `ProfissionalServiceImpl.java` (patroservicosSD1)
```java
public Profissional criarProfissional(Profissional profissional) { ... }
```
- **Motivo:** Método `public` sem `@Override`, pode ser método privado ou não utilizado.
- **Recomendação:** Revisar; se não é chamado, marcar como `private` ou comentar.

---

## 6. ARQUIVOS/PASTAS DUPLICADAS OU REDUNDANTES

⚠️ **Status:** DETECTADOS, NÃO COMENTADOS

### 6.1 Classes de balanceamento duplicadas
- `LoadBalancer.java` e `BalanceadorCarga.java` no mesmo pacote `loadbalancer`.
- **Motivo:** Aparentemente, houve renomeação ou coexistência de versões antiga/nova.
- **Recomendação:** Consolidar em UMA classe; comentar a versão antiga.

---

## 7. IMPORTS NÃO UTILIZADOS (DETECTADOS EM VÁRIOS ARQUIVOS)

⚠️ **Status:** RECOMENDAÇÃO

- Múltiplos arquivos Java têm imports não usados (ex.: `import java.net.URI;` comentado em `LoadBalancedRestTemplate`).
- **Recomendação:** Rodar IDE "Organize Imports" em todos os arquivos Java para limpeza automática.

---

## 8. RESUMO DE AÇÕES RECOMENDADAS

| Item | Status | Ação Recomendada |
|------|--------|------------------|
| Arquivos de teste vazios (3 arquivos) | ✅ Comentados | Nenhuma |
| `patroservicosSDGestor/js/home.js` | ✅ Comentado | Nenhuma |
| Outros `*.js` comentados | ⚠️ Pendente | Comentar com bloco `/* MOTIVO */` |
| HTML comentados (templates) | ⚠️ Pendente | Comentar seções ou remover |
| Métodos Java não utilizados | ⚠️ Pendente | Fazer grep de referências; se não usados, comentar ou marcar `@Deprecated` |
| Imports não usados | ⚠️ Pendente | Executar "Organize Imports" na IDE |
| Classes duplicadas (`LoadBalancer` / `BalanceadorCarga`) | ⚠️ Pendente | Consolidar e comentar versão antiga |

---

## 9. PRÓXIMOS PASSOS

1. **Revisar e comentar** arquivos JavaScript inteiramente obsoletos (home.js, perfil-profissional.js, etc.).
2. **Consolidar** classes de balanceamento duplicadas.
3. **Fazer grep** em cada método não-usado para confirmar zero referências.
4. **Rodar "Organize Imports"** em toda a base de código Java.
5. **Validar** se frontend cliente é ativo; se descontinuado, agrupar em pasta `/deprecated` ou comentar.

---

**Relatório gerado automaticamente pelo pente-fino do projeto.**  
**Padrão de comentário usado:** `/* ARQUIVO NÃO UTILIZADO - COMENTADO */` seguido de justificativa detalhada.
