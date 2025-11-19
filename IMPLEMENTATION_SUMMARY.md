# Resumo de Implementação: Balanceamento de Carga + Tolerância a Falhas

## 🎯 Objetivo Alcançado

✅ **Balanceamento de Carga**: Requisições distribuídas via round-robin entre SD1 e SD1Replica  
✅ **Tolerância a Falhas**: Detecção automática e contorno de instâncias inativas  
✅ **Health Checks**: Monitoramento periódico da saúde das instâncias  
✅ **Retry Automático**: Se uma instância falhar, tenta a próxima automaticamente  

---

## 📦 Arquivos Criados/Modificados

### Novos Arquivos (Balanceador de Carga)

1. **`loadbalancer/ServiceInstance.java`**
   - Representa uma instância de serviço
   - Rastreia estado (ativo/inativo) e falhas

2. **`loadbalancer/ServiceRegistry.java`**
   - Registro centralizado de instâncias
   - Gerencia adição/remoção de instâncias
   - Marca como ativa/inativa

3. **`loadbalancer/LoadBalancer.java`**
   - Implementa round-robin entre instâncias ativas
   - Seleciona próxima instância em sequência
   - Lança exceção se nenhuma ativa

4. **`loadbalancer/HealthCheckScheduler.java`**
   - Verifica saúde a cada 10 segundos
   - Detecta instâncias recuperadas
   - Marca falhas após 3 tentativas

5. **`loadbalancer/LoadBalancedRestTemplate.java`**
   - Wrapper do RestTemplate com balanceamento
   - Implementa retry automático (até 3 tentativas)
   - Registra sucesso/falha no registry

6. **`loadbalancer/StatusController.java`**
   - Endpoint `/api/status` para health checks
   - Responde com `{"status":"ok"}`

### Arquivos Modificados

1. **`config/OrquestracaoConfig.java`**
   - Injeta HealthCheckScheduler
   - Inicializa instâncias no @PostConstruct

2. **`gateway/ProfissionaisGatewayController.java`**
   - Substitui RestTemplate por LoadBalancedRestTemplate
   - Agora usa balanceamento automático em todas as rotas

3. **`src/main/resources/application.properties`**
   - Adicionada porta de SD1Replica (8085)

4. **`patroservicosSD1/src/main/java/.../StatusController.java`** (novo)
   - Endpoint de status para health check

5. **`patroservicosSD1Replica/src/main/java/.../StatusController.java`** (novo)
   - Endpoint de status para health check

---

## 🔄 Fluxo de Funcionamento

### Inicialização

```
1. Spring Boot inicia o Gestor
2. @PostConstruct em OrquestracaoConfig
3. HealthCheckScheduler.initializeInstances()
4. Registra: sd1 (8082) e sd1replica (8085)
5. Inicia verificação periódica a cada 10 segundos
```

### Durante a Operação

```
Cliente → GET /api/profissionais

Gestor:
1. ProfissionaisGatewayController recebe requisição
2. Chama LoadBalancedRestTemplate.getForEntity()
3. LoadBalancer seleciona próxima instância (round-robin)
4. Tenta requisição na instância selecionada
5. Se sucesso: retorna resposta + registra sucesso
6. Se falha: tenta próxima instância (retry até 3 vezes)
7. Se todas falham: retorna erro 503
```

### Health Check (a cada 10 segundos)

```
HealthCheckScheduler.performHealthCheck()
├─ Para cada instância:
│  └─ GET http://localhost:8082/api/status
│     ├─ Se OK: recordSuccess() → marca ativa
│     └─ Se falha: recordFailure() → pode marcar inativa
└─ Imprime SERVICE REGISTRY STATUS
```

---

## 🛡️ Tolerância a Falhas

### Cenário 1: SD1 Falha

```
1. Requisição chega ao Gestor
2. LoadBalancer seleciona SD1
3. Conexão recusada → recordFailure()
4. Tenta SD1Replica → sucesso ✓
5. Cliente recebe resposta normalmente

6. Health check detecta SD1 down
7. Marca SD1 como inativo
8. Próximas requisições usam só SD1Replica
```

### Cenário 2: SD1 Volta

```
1. SD1 reiniciado
2. Health check tenta GET /api/status
3. Sucesso! → recordSuccess()
4. Marca SD1 como ativo novamente
5. Round-robin resume alternando entre as duas
```

### Cenário 3: Ambas Falham

```
1. Requisição tenta SD1 → falha
2. Requisição tenta SD1Replica → falha
3. Requisição tenta SD1 novamente → falha (3ª tentativa)
4. Lança RuntimeException: "Todas as instâncias falharam"
5. Cliente recebe erro 503
```

---

## 📊 Round-Robin

Usa `AtomicInteger` com modulo:

```
Requisição 1: counter = 0 % 2 = 0 → SD1
Requisição 2: counter = 1 % 2 = 1 → SD1Replica
Requisição 3: counter = 2 % 2 = 0 → SD1
Requisição 4: counter = 3 % 2 = 1 → SD1Replica
...
```

Sempre alterna entre as instâncias ativas.

---

## ⚙️ Configuração Necessária

### `patroservicosSDGestor/application.properties`

```properties
servicos.sd1.base-url=http://localhost:8082
servicos.sd1replica.base-url=http://localhost:8085
```

Já está configurado! ✅

---

## 🚀 Como Testar

### 1. Compilar e Empacotar
```bash
cd patroservicosSDGestor
./mvnw clean install
```

### 2. Iniciar Serviços

**Terminal 1:**
```bash
cd patroservicosSD1
./mvnw spring-boot:run
```

**Terminal 2:**
```bash
cd patroservicosSD1Replica
./mvnw spring-boot:run
```

**Terminal 3:**
```bash
cd patroservicosSDGestor
./mvnw spring-boot:run
```

### 3. Testar Requisições

```bash
# Múltiplas requisições (observe os logs)
for i in {1..6}; do
  echo "Requisição $i:"
  curl http://localhost:8081/api/profissionais
  sleep 1
done
```

Você verá alternância entre SD1 e SD1Replica nos logs.

### 4. Testar Tolerância a Falhas

```bash
# Parar SD1 (Ctrl+C no Terminal 1)
# Fazer requisição:
curl http://localhost:8081/api/profissionais

# Observar que usa SD1Replica automaticamente
# Depois reiniciar SD1 e ver recuperação
```

---

## 📈 Melhorias Futuras

1. **Métricas**: Prometheus/Micrometer para monitorar requisições
2. **Circuit breaker**: Hystrix para isolamento de falhas
3. **Load balancing inteligente**: Pesar instâncias por latência
4. **Service discovery**: Encontrar instâncias automaticamente
5. **Cache**: Replicar dados entre instâncias
6. **Alertas**: Notificar administrador em caso de falha

---

## 📝 Logs Importantes

Procure por estes patterns nos logs:

- `[LoadBalancer]` - Seleção de instâncias
- `[HealthChecker]` - Verificação de saúde
- `[LoadBalancedRest]` - Tentativas de requisição
- `SERVICE REGISTRY STATUS` - Status consolidado
- `✓` - Sucesso
- `✗` - Falha

---

## ✅ Checklist de Implementação

- [x] ServiceRegistry criado
- [x] ServiceInstance criado
- [x] LoadBalancer com round-robin
- [x] HealthCheckScheduler implementado
- [x] LoadBalancedRestTemplate com retry
- [x] ProfissionaisGatewayController atualizado
- [x] StatusController em SD1
- [x] StatusController em SD1Replica
- [x] OrquestracaoConfig inicializa balanceador
- [x] application.properties atualizado
- [x] Documentação completa

---

## 🎓 Conceitos Aplicados

- **Padrão: Service Locator** - LoadBalancer localiza instâncias
- **Padrão: Circuit Breaker** - Marca instâncias como inativas
- **Padrão: Health Check** - Monitora saúde das instâncias
- **Padrão: Retry** - Tenta novamente em outra instância
- **Concorrência**: AtomicInteger para round-robin thread-safe
- **Scheduled Tasks**: @Scheduled para verificações periódicas

