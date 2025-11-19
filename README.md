# PatroServiçosSD

Projeto para a disciplina de Sistemas Distribuídos — prova/trabalho prático.  
Monorepo com micro-serviços mínimos para demonstrar: separação de responsabilidades, balanceamento (round‑robin), tolerância a falhas e comunicação entre serviços.

Resumo rápido
- Objetivo: app simples com profissionais, clientes, login, chat e feedbacks. Demonstra balanceamento entre duas instâncias do Serviço 1.
- Arquitetura: Gestor (gateway) → Serviços (Serviço 1, Serviço 2, Serviço 3)
- Linguagens / Stack:
  - Java + Spring Boot (Serviço 1, Serviço 2, Gestor)
  - Python + FastAPI (Serviço 3)
  - H2 (desenvolvimento rápido) / Postgres (opcional para produção)
  - MongoDB (serviço 3, opcional para demo local; pode começar sem)
- Front-end: HTML/CSS/JS servidos pelo Gestor (static/ ou templates/).

Estrutura sugerida (monorepo)
- servico1-spring/      — profissionais, documentos, perfil_profissional, clientes (Postgres/H2)
- servico2-spring/      — autenticação (usuarios_clientes, usuarios_trabalhadores)
- servico3-python/      — chat e feedbacks (MongoDB)
- gestor-spring/        — gateway / orquestrador / serve HTML/CSS/JS
- client/ (opcional)    — páginas estáticas se preferir manter fora do Gestor

O que cada componente faz
- Serviço 1: dados de domínio (profissionais, clientes, documentos, perfil), expõe REST e /actuator/health.
- Serviço 2: autenticação (registro/login), emite token simples (para prova pode ser token em memória) e valida token.
- Serviço 3: chat e feedback (coleções Mongo), rotas básicas.
- Gestor: serve as páginas estáticas, faz proxy/orquestração, balanceia chamadas para Serviço 1 (round-robin), executa health-check das instâncias e encaminha chamadas para S2/S3.

Como rodar (modo rápido, sem Docker)
1. Preparação (Java)
- Java 17+ instalado
- Maven instalado

2. Rodar Serviço 1 (duas instâncias)
- Terminal 1:
  - cd servico1-spring
  - mvn spring-boot:run
  - (ou) mvn spring-boot:run -Dspring-boot.run.arguments="--PORT=8081"
- Terminal 2 (segunda instância na porta 8082):
  - cd servico1-spring
  - mvn spring-boot:run -Dspring-boot.run.arguments="--PORT=8082 --INSTANCE_ID=svc1-8082"

3. Rodar Serviço 2 (auth)
- cd servico2-spring
- mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8090"

4. Rodar Serviço 3 (FastAPI)
- cd servico3-python
- python -m venv venv
- Windows: venv\Scripts\activate  Linux/Mac: source venv/bin/activate
- pip install -r requirements.txt
- uvicorn app.main:app --reload --port 8000

5. Rodar Gestor (serve front + proxy)
- cd gestor-spring
- mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=3000"

Observações sobre bancos
- Para desenvolvimento o projeto usa H2 (in-memory) por facilidade. Se quiser Postgres:
  - injetar URL: `--spring.datasource.url=jdbc:postgresql://localhost:5432/servico1` e setar user/password.
- Serviço 3 pode usar MongoDB; para demo você pode manter um armazenamento simples em memória, se preferir (menos dependências).

Principais endpoints (resumo)
- Gestor (ponto único que o front chama)
  - GET  /api/profissionais              → balanceia entre instâncias do Serviço 1
  - GET  /api/profissionais/{id}        → S1
  - POST /api/cadastro-profissional-completo → orquestra S1 (dados) + S2 (credencial)
  - POST /api/auth/cliente/register     → proxy → S2
  - POST /api/auth/cliente/login        → proxy → S2
  - POST /api/auth/profissional/register/login → proxy → S2
  - GET/POST /api/chats                 → proxy → S3
  - GET/POST /api/feedbacks             → proxy → S3

- Serviço 1 (exemplos)
  - GET  /api/v1/profissionais
  - POST /api/v1/profissionais
  - GET  /actuator/health

- Serviço 2 (exemplos)
  - POST /api/v1/auth/cliente/register
  - POST /api/v1/auth/cliente/login
  - GET  /api/v1/auth/validate

- Serviço 3 (exemplos)
  - POST /api/v1/chats
  - POST /api/v1/feedbacks

Como demonstrar balanceamento e tolerância a falhas (para a apresentação)
1. Com tudo rodando, abra o front (p. ex. http://localhost:3000/cadastro-profissional.html ou /index.html).
2. No console do navegador faça fetch('/api/profissionais') repetidas vezes; observe no response header `X-Instance-Id` alternando entre svc1-8081 e svc1-8082.
3. Pare uma instância do Serviço 1 (Ctrl+C). Chame novamente `/api/profissionais` — Gestor deve pular a instância caída e responder usando a outra.
4. Reinicie a instância parada; Gestor (com health-check) deve recolocá-la na lista.

Decisões simplificadoras (para terminar rápido)
- Unificar o formulário de cadastro do profissional: coleta perfil + login/senha e Gestor orquestra S1 + S2.
- Token simples em memória no Serviço 2 (UUID) em vez de JWT.
- Documentos/fotos como campos de texto ou upload simples (opcional).
- Front estático servido pelo Gestor (pasta `static/`), sem SSR necessário.
- Validations básicas só (não implemente recuperação de senha).

Dicas e boas práticas (para a entrega)
- Não exponha diretamente portas de S1/S2/S3 ao front — o front deve falar apenas com o Gestor.
- Mantenha logs simples no Gestor indicando qual instância foi usada (útil para demo).
- Seed no Serviço 1 para já ter 2 profissionais ao iniciar (facilita demonstração).
- Teste o fluxo completo antes da apresentação (registro → login → chat/feedback).

Arquivo de configuração importante
- Mantenha no Gestor um arquivo com as URLs das instâncias do Serviço 1:
  - ex.: `servico1.instances = http://localhost:8081,http://localhost:8082`
  - e as bases do S2 e S3: `servico2.base = http://localhost:8090`, `servico3.base = http://localhost:8000`

Contato / Autor
- Autor: PabloVLS (aluno)
- Projeto feito para fins de avaliação escolar — código simples e didático.

Licença
- MIT (ou outra licença conforme sua preferência).

---

Se quiser, eu já gero para você:
- README pronto num arquivo `README.md` (este texto).
- Lista enxuta de endpoints com exemplos de payloads.
- Esqueleto do endpoint orquestrador no Gestor (sem implementação completa) para você colar no projeto.
Diz o que prefere que eu faça em seguida.
