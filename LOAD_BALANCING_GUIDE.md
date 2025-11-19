# Balanceamento de Carga - Documentação Técnica

## 📋 Resumo da Implementação

O Gestor agora implementa **balanceamento de carga com tolerância a falhas** para distribuir requisições entre **SD1** e **SD1Replica**.

### ✅ Componentes Implementados

#### 1. **ServiceRegistry** (`loadbalancer/ServiceRegistry.java`)
- Registro centralizado de instâncias
- Monitora o status (ativa/inativa) de cada instância
- Rastreia contagem de falhas

#### 2. **ServiceInstance** (`loadbalancer/ServiceInstance.java`)
- Representa cada instância do serviço
- Mantém estado (ativo/inativo) e histórico de falhas
- Marcar como ativo/inativo automaticamente

#### 3. **LoadBalancer** (`loadbalancer/LoadBalancer.java`)
- **Estratégia**: Round-robin entre instâncias ativas
- Seleciona a próxima instância em sequência
- Lança exceção se nenhuma instância estiver disponível

#### 4. **HealthCheckScheduler** (`loadbalancer/HealthCheckScheduler.java`)
- Verifica saúde das instâncias a cada **10 segundos**
- Timeout de health check: **3 segundos**
- Detecta:
  - ✓ Instâncias que voltam a responder → reativa
  - ✗ Instâncias com falha → desativa

#### 5. **LoadBalancedRestTemplate** (`loadbalancer/LoadBalancedRestTemplate.java`)
- Wrapper do RestTemplate com balanceamento automático
- Implementa **retry automático** (até 3 tentativas)
- Registra sucesso/falha no ServiceRegistry

#### 6. **StatusController** (ambas instâncias)
- Endpoint `/api/status` para health checks
- Retorna `{"status":"ok"}`

---

## 🔄 Fluxo de Requisição

### Requisição Normal

```
Cliente
   ↓
Gestor (8081)
   ├─ LoadBalancer seleciona próxima instância (round-robin)
   ├─ LoadBalancedRestTemplate faz a requisição
   ├─ SD1 (8082) ✓ responde → sucesso
   └─ Retorna resposta ao cliente
```

### Com Falha de SD1

```
Cliente
   ↓
Gestor (8081)
   ├─ LoadBalancer seleciona SD1
   ├─ Falha de conexão
   ├─ LoadBalancer tenta SD1Replica (8085) ✓
   └─ Requisição bem-sucedida na réplica
```

### Com Ambas Falhas

```
Cliente
   ↓
Gestor (8081)
   ├─ Tenta SD1 → falha
   ├─ Tenta SD1Replica → falha
   ├─ Tenta SD1 novamente → falha (3ª tentativa)
   └─ Retorna erro 503 (Service Unavailable)
```

---

## 📊 Health Check

Executado a cada 10 segundos:

```
[HealthChecker] Iniciando health check...
[HealthChecker] ✓ sd1 está saudável
[HealthChecker] ✓ sd1replica está saudável

========== SERVICE REGISTRY STATUS ==========
ServiceInstance{id='sd1', url='http://localhost:8082', active=true, failures=0}
ServiceInstance{id='sd1replica', url='http://localhost:8085', active=true, failures=0}
============================================
```

---

## ⚡ Cenários de Teste

### Teste 1: Round-Robin Normal
**Objetivo**: Verificar se requisições alternam entre SD1 e SD1Replica

1. Inicie ambos os serviços:
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

2. Faça requisições GET na API de Profissionais:
```bash
curl http://localhost:8081/api/profissionais
```

3. Observe os logs:
```
[LoadBalancer] Selecionou: sd1 (http://localhost:8082)
[LoadBalancedRest] ✓ Sucesso em sd1

[LoadBalancer] Selecionou: sd1replica (http://localhost:8085)
[LoadBalancedRest] ✓ Sucesso em sd1replica

[LoadBalancer] Selecionou: sd1 (http://localhost:8082)
...
```

### Teste 2: Falha de SD1
**Objetivo**: Verificar se o Gestor continua operando com SD1Replica

1. Parar SD1 (Ctrl+C no terminal 1)
2. Fazer requisições:
```bash
curl http://localhost:8081/api/profissionais
```
3. Observe:
```
[LoadBalancer] Selecionou: sd1 (http://localhost:8082)
[LoadBalancedRest] ✗ Falha na tentativa 1: Connection refused
[LoadBalancer] Selecionou: sd1replica (http://localhost:8085)
[LoadBalancedRest] ✓ Sucesso em sd1replica
```

4. Após ~10 segundos, o health check detecta:
```
[HealthChecker] ✗ sd1 falhou (HttpClientErrorException)
========== SERVICE REGISTRY STATUS ==========
ServiceInstance{id='sd1', url='http://localhost:8082', active=false, failures=3}
ServiceInstance{id='sd1replica', url='http://localhost:8085', active=true, failures=0}
============================================
```

### Teste 3: Recuperação de SD1
**Objetivo**: Verificar se SD1 é reativada quando volta

1. Reiniciar SD1:
```bash
cd patroservicosSD1
./mvnw spring-boot:run
```

2. Após ~10 segundos, health check detecta:
```
[HealthChecker] ✓ sd1 está respondendo novamente!
========== SERVICE REGISTRY STATUS ==========
ServiceInstance{id='sd1', url='http://localhost:8082', active=true, failures=0}
ServiceInstance{id='sd1replica', url='http://localhost:8085', active=true, failures=0}
============================================
```

3. Round-robin resume normalmente:
```
[LoadBalancer] Selecionou: sd1replica (http://localhost:8085)
[LoadBalancer] Selecionou: sd1 (http://localhost:8082)
[LoadBalancer] Selecionou: sd1replica (http://localhost:8085)
```

---

## 🛠️ Configuração

### `patroservicosSDGestor/application.properties`

```properties
# URLs das instâncias
servicos.sd1.base-url=http://localhost:8082
servicos.sd1replica.base-url=http://localhost:8085
```

### Parâmetros Ajustáveis

| Parâmetro | Localização | Valor Padrão | Descrição |
|-----------|-------------|--------------|-----------|
| Health check interval | `HealthCheckScheduler.java` | 10 segundos | Frequência de verificação |
| Health check timeout | `HealthCheckScheduler.java` | 3 segundos | Timeout de cada health check |
| Max failures | `ServiceInstance.java` | 3 | Falhas consecutivas para desativar |
| Max retries | `LoadBalancedRestTemplate.java` | 3 | Tentativas máximas por requisição |

---

## 📊 Monitoramento

### Logs Importantes

```
[HealthChecker] - Health check e status de instâncias
[LoadBalancer] - Seleção de instâncias (round-robin)
[LoadBalancedRest] - Tentativas de requisição e retry
SERVICE REGISTRY STATUS - Status consolidado das instâncias
```

### Exemplo de Saída Completa

```
[OrquestracaoConfig] Inicializando balanceador de carga...
[HealthChecker] Instâncias inicializadas:

========== SERVICE REGISTRY STATUS ==========
ServiceInstance{id='sd1', url='http://localhost:8082', active=true, failures=0}
ServiceInstance{id='sd1replica', url='http://localhost:8085', active=true, failures=0}
============================================

[HealthChecker] Iniciando health check...
[HealthChecker] ✓ sd1 está saudável
[HealthChecker] ✓ sd1replica está saudável

[LoadBalancer] Selecionou: sd1 (http://localhost:8082)
[LoadBalancedRest] Tentativa 1 → sd1 [GET] /api/profissionais
[LoadBalancedRest] ✓ Sucesso em sd1
```

---

## ✨ Características

✅ **Round-robin**: Requisições distribuídas uniformemente  
✅ **Tolerância a falhas**: Detecta e contorna instâncias com problemas  
✅ **Retry automático**: Tenta outra instância automaticamente  
✅ **Health checks periódicos**: Detecta recuperação de instâncias  
✅ **Sem ponto único de falha**: Sistema continua operando com uma instância  
✅ **Logs detalhados**: Rastreamento completo do comportamento  

---

## 🚀 Próximos Passos (Opcional)

1. **Adicionar métricas**: Integrar Prometheus/Micrometer
2. **Alertas**: Notificar quando instâncias falham
3. **Circuit breaker**: Integrar com Hystrix/Resilience4j
4. **Load distribution ponderada**: Dar mais peso a instâncias mais rápidas
5. **Service discovery**: Autodescoberta de instâncias (Consul, Eureka)

