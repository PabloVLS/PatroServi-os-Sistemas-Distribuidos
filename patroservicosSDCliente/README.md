# Cliente Web - PatroServiços

Projeto estático separado do Gestor. Consome as APIs expostas pelo gateway (Gestor) em `http://localhost:8081` por padrão.

## Estrutura
```
patroservicosSDCliente/
  index.html
  loginCliente.html
  loginProfissional.html
  profissional-cadastro.html
  profissional-meu-perfil.html
  perfil-profissional.html
  config.js
  js/
    home.js
    perfil-publico.js
    profissional-cadastro.js
```

## Configuração
Edite `js/config.js` para apontar `API_BASE` (gateway) e `SD1_BASE_URL` (serviço que serve /uploads/**).

```js
window.API_BASE = 'http://localhost:8081';
window.SD1_BASE_URL = 'http://localhost:8082';
```

## Servindo localmente
Abra o diretório em um servidor estático (exemplos PowerShell):

```powershell
# Opção 1 - Python (se instalado)
python -m http.server 5500

# Opção 2 - npx serve (Node)
npx serve -l 5500
```
Acesse: http://localhost:5500/index.html

## Sessão / Estado
- Usa sessionStorage para armazenar:
  - `usuarioProfissionalId`, `usuarioLogin`
  - `clienteUsuarioId`, `clienteLogin`, `clienteId`
- Logout = remover chaves e redirecionar para `index.html`.

## Próximos passos sugeridos
- Adicionar build simples (Vite ou similar) caso evolua para SPA.
- Centralizar componentes (navbar) via include ou JS.
- Implementar chat / favoritos / orçamento.
- Sanitizar imagens antigas com URLs duplicadas (script backend).
