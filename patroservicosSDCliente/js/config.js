// Configuração do cliente web (movido para pasta js)
// Aponte para o host/porta do Gestor (gateway) em produção ou local.
// Pode ser sobrescrito antes do carregamento definindo window.API_BASE no ambiente.
window.API_BASE = window.API_BASE || 'http://localhost:8081';

// Normalização: se o Gestor estiver acessível por 127.0.0.1 use localhost por consistência
if (window.API_BASE === 'http://127.0.0.1:8081') {
  window.API_BASE = 'http://localhost:8081';
}

// Base do serviço 1 para montar URLs absolutas de imagens servidas em /uploads/**
// Use a URL pública onde o SD1 está exposto. Pode ser sobrescrito por window.SD1_BASE_URL.
window.SD1_BASE_URL = window.SD1_BASE_URL || 'http://localhost:8082';

// Helper conveniente para construir URLs de API
window.apiUrl = function(path) {
  return (window.API_BASE || '') + path;
};

// Debug opcional: habilite setando window.CLIENT_DEBUG = true antes de carregar
if (window.CLIENT_DEBUG) console.info('API_BASE=', window.API_BASE, 'SD1_BASE_URL=', window.SD1_BASE_URL);
