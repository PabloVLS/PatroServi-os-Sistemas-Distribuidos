# patroservicosSD3

Serviço 3 (Python + Flask + MongoDB) para gerenciar Feedbacks e PerfilCliente.

Dependências

- Flask
- pymongo
- flask-cors

Instalação (virtualenv recomendado)

```powershell
cd patroservicosSD3
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

Configuração

Defina variáveis de ambiente se necessário (opcionais):

- MONGO_URI - string de conexão MongoDB (padrão: mongodb://localhost:27017)
- MONGO_DB - nome do banco (padrão: patro_servicos3)
- PORT - porta onde o serviço rodará (padrão: 8084)

Rodando localmente

```powershell
# com o venv ativado
python app.py
```

Endpoints principais

- POST /api/feedbacks  -> criar feedback
- GET /api/feedbacks/<id> -> obter feedback por id
- GET /api/feedbacks/profissional/<profissional_id> -> listar feedbacks de um profissional

- POST /api/perfil-clientes -> criar/atualizar perfil do cliente (upsert)
- GET /api/perfil-clientes/<cliente_id> -> obter perfil do cliente
- PUT /api/perfil-clientes/<cliente_id> -> atualizar perfil do cliente

Exemplos curl

```bash
curl -X POST http://localhost:8084/api/feedbacks -H 'Content-Type: application/json' -d '{"profissional_id":"abc","cliente_id":"c1","nota":5,"comentario":"Ótimo serviço"}'

curl -X POST http://localhost:8084/api/perfil-clientes -H 'Content-Type: application/json' -d '{"cliente_id":"c1","foto_perfil":"/uploads/c1.jpg","descricao":"Cliente exemplo","preferencias":{"categoria":"eletrico"}}'
```
