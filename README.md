# 🚀 PatroServiçosSD

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-✅-6DB33F) ![Python](https://img.shields.io/badge/Python-3.10-yellow) ![MongoDB](https://img.shields.io/badge/MongoDB-✓-47A248) ![License MIT](https://img.shields.io/badge/License-MIT-lightgrey)

Uma implementação didática para a disciplina de Sistemas Distribuídos — monorepo com micro-serviços que demonstram separação de responsabilidades, orquestração, balanceamento (round‑robin) e tolerância a falhas.

---

## 📌 Índice
- [Sobre](#-sobre)  
- [Visão rápida](#-visão-rápida)  
- [Arquitetura](#-arquitetura)  
- [Estrutura do repositório](#-estrutura-do-repositório)  
- [Tecnologias](#-tecnologias)  
- [Como executar (modo rápido)](#-como-executar-modo-rápido)  
- [Endpoints principais](#-endpoints-principais)  
- [Demonstração: balanceamento e tolerância](#-demonstração-balanceamento-e-tolerância)  
- [Decisões simplificadoras](#-decisões-simplificadoras)  
- [Contribuição](#-contribuição)  
- [Licença](#-licença)  
- [Autor](#-autor)

---

## 📝 Sobre
PatroServiçosSD é um projeto de exemplo que simula um sistema de prestação de serviços com três backends separados e um Gestor que atua como gateway/orquestrador. O objetivo é demonstrar conceitos de sistemas distribuídos (multisserviços, balanceamento, tolerância a falhas e orquestração entre serviços).

---

## ⚡ Visão rápida
- Front (HTML/CSS/JS) servido pelo **Gestor**.
- Gestor: gateway + balanceador + orquestrador (sem banco).
- Serviço 1: dados do domínio (Profissionais, Clientes, Documentos, Perfil) — Postgres/H2.
- Serviço 2: autenticação (logins/credenciais) — Postgres/H2.
- Serviço 3: interações (chat, feedback) — MongoDB/FastAPI.

---

## 🏗️ Arquitetura (resumo)
```mermaid
flowchart LR
  Browser -->|HTTP| Gestor[Gestor (Spring Boot) - Front + Proxy]
  Gestor -->|round-robin| S1[Serviço 1 (Spring) - Profissionais/Clientes]
  Gestor --> S2[Serviço 2 (Spring) - Auth]
  Gestor --> S3[Serviço 3 (FastAPI) - Chat/Feedback]
  S1 --> DB1[(Postgres/H2)]
  S2 --> DB2[(Postgres/H2)]
  S3 --> DB3[(MongoDB)]
```
> Observação: IDs entre serviços são UUIDs lógicos — não há FK física entre bancos.

---

## 📁 Estrutura sugerida do repositório
```
/
├─ gestor-spring/        # Gateway + front (templates / static)
├─ servico1-spring/      # Profissionais, Clientes, Documentos, Perfil
├─ servico2-spring/      # Autenticação (usuarios_clientes, usuarios_trabalhadores)
├─ servico3-python/      # Chat e feedbacks (FastAPI + Mongo)
└─ README.md
```

---

## 🛠️ Tecnologias
- Backend: Java 17, Spring Boot (Serviço 1, Serviço 2, Gestor)
- Backend (S3): Python 3.10, FastAPI
- DBs: H2 (dev), Postgres (prod), MongoDB (serviço 3)
- Front: HTML/CSS/JS (Bootstrap 5), estático servido pelo Gestor
- Build: Maven

---

## ▶️ Como executar (modo rápido, sem Docker)
Pré-requisitos: Java 17+, Maven, Python 3.10+ (opcional, para S3).

1. Serviço 1 — instância 1 (porta 8081)
```bash
cd servico1-spring
mvn spring-boot:run -Dspring-boot.run.arguments="--PORT=8081"
```

2. Serviço 1 — instância 2 (porta 8082)
```bash
cd servico1-spring
mvn spring-boot:run -Dspring-boot.run.arguments="--PORT=8082 --INSTANCE_ID=svc1-8082"
```

3. Serviço 2 — autenticação (porta 8090)
```bash
cd servico2-spring
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8090"
```

4. Serviço 3 — FastAPI (porta 8000)
```bash
cd servico3-python
python -m venv venv
# ativar venv...
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

5. Gestor — front + proxy (porta 3000)
```bash
cd gestor-spring
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=3000"
```

Abra o front: http://localhost:3000

---

## 🔌 Endpoints principais (resumo)
Obs: o browser/JS deve conversar apenas com o Gestor (ex.: `http://localhost:3000/api/...`).

- Gestor (gateway)
  - GET  /api/profissionais                      → lista (balanceado S1)
  - GET  /api/profissionais/{id}                → details (S1)
  - POST /api/cadastro-profissional-completo    → orquestra S1 + S2
  - POST /api/auth/cliente/register/login       → proxy → S2
  - POST /api/auth/profissional/register/login  → proxy → S2
  - GET/POST /api/chats                         → proxy → S3
  - GET/POST /api/feedbacks                     → proxy → S3

- Serviço 1 (exemplo)
  - GET  /api/v1/profissionais
  - POST /api/v1/profissionais
  - GET  /actuator/health

- Serviço 2 (exemplo)
  - POST /api/v1/auth/cliente/register
  - POST /api/v1/auth/cliente/login
  - GET  /api/v1/auth/validate

- Serviço 3 (exemplo)
  - POST /api/v1/chats
  - POST /api/v1/feedbacks

---

## 🎯 Demonstração: balanceamento & tolerância (roteiro curto)
1. Suba as duas instâncias do Serviço 1 e o Gestor.
2. Chame repetidamente:
```bash
curl -i http://localhost:3000/api/profissionais
```
3. Observe no cabeçalho de resposta o `X-Instance-Id` alternando entre `svc1-8081` e `svc1-8082`.
4. Pare uma instância (Ctrl+C). O Gestor deve detectar erro (timeout) e usar a outra instância.
5. Reinicie a instância; o health-check reabilita ela.

---

## 💡 Decisões simplificadoras (para entrega rápida)
- Formulário do profissional único: coleta perfil + login/senha → Gestor orquestra S1 + S2.
- Token simples em memória (UUID) no S2 para validação (em vez de JWT).
- Uploads e documentos podem ser Strings/paths (simplifica armazenamento).
- Front servido pelo Gestor (evita chamadas diretas ao S1/S2/S3).

---

## 🤝 Contribuição
- Issues são bem-vindas para bugs e melhorias.
- Para pequenas correções, abra um PR com testes simples e descrição clara do que foi alterado.

---

## 🧾 Licença
MIT — veja o arquivo LICENSE para detalhes.

---

## 👤 Autor
PabloVLS — criado para fins acadêmicos / avaliação.

---

Se quiser, eu posso:
- adicionar badges dinâmicos (build, coverage) — se você ligar CI;
- gerar uma versão em inglês;
- ou atualizar o README com exemplos concretos de payloads/curl para cada endpoint.
Me diz qual das opções prefere que eu faça em seguida.  
