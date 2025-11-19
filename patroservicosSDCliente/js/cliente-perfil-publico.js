// Função auxiliar para pegar parâmetros da URL
function getParam(nome) {
  return new URL(window.location.href).searchParams.get(nome);
}

// Carregar perfil público do cliente
document.addEventListener('DOMContentLoaded', function() {
  carregarPerfilPublicoCliente();
});

async function carregarPerfilPublicoCliente() {
  const clienteId = getParam('id');
  const erroDiv = document.getElementById('erroCliente');
  
  if (!clienteId) {
    erroDiv.textContent = 'ID do cliente não informado.';
    erroDiv.classList.remove('d-none');
    return;
  }

  try {
    // PRIMEIRO: Carregar dados básicos do cliente do SD1
    let perfilSD1 = null;
    try {
      const respSD1 = await fetch((window.API_BASE || '') + `/api/clientes/${clienteId}`);
      if (respSD1.ok) {
        perfilSD1 = await respSD1.json();
      }
    } catch (err) {
      console.warn('Não foi possível carregar dados do SD1:', err);
    }

    // SEGUNDO: Carregar perfil estendido do cliente do SD3 (foto, descrição)
    let perfilSD3 = null;
    try {
      const respSD3 = await fetch((window.API_BASE || '') + `/api/perfil-cliente/${clienteId}`);
      if (respSD3.ok) {
        perfilSD3 = await respSD3.json();
      }
    } catch (err) {
      console.warn('Não foi possível carregar dados do SD3:', err);
    }

    // Mesclar dados: SD1 tem prioridade para nome/email/telefone, SD3 tem foto/descrição
    const nome = (perfilSD3 && perfilSD3.nome) || (perfilSD1 && perfilSD1.nome) || 'Cliente';
    const email = (perfilSD3 && perfilSD3.email) || (perfilSD1 && perfilSD1.email) || '—';
    const telefone = (perfilSD3 && perfilSD3.numero) || (perfilSD1 && perfilSD1.numero) || '—';
    const descricao = (perfilSD3 && perfilSD3.descricao) || '';
    const fotoBase64 = perfilSD3 && perfilSD3.foto_perfil;

    // Atualizar nome
    document.getElementById('nomeCliente').textContent = nome;

    // Atualizar avatar
    const avatarDiv = document.getElementById('avatarPlaceholder');
    if (fotoBase64) {
      avatarDiv.innerHTML = `<img src="${fotoBase64}" alt="Foto de perfil">`;
    } else {
      const iniciais = nome.split(' ').slice(0, 2).map(p => p[0]).join('').toUpperCase();
      avatarDiv.innerHTML = iniciais || '<i class="bi bi-person-fill"></i>';
    }

    // Atualizar email e telefone
    document.getElementById('emailCliente').textContent = email;
    document.getElementById('telefoneCliente').textContent = telefone;

    // Atualizar descrição
    if (descricao) {
      document.getElementById('descricaoCompletoCliente').textContent = descricao;
      document.getElementById('descricaoCliente').textContent = descricao.substring(0, 60) + (descricao.length > 60 ? '...' : '');
    } else {
      document.getElementById('cardDescricaoCliente').classList.add('d-none');
      document.getElementById('descricaoCliente').textContent = '—';
    }

    // Se nenhum dado foi encontrado
    if (!perfilSD1 && !perfilSD3) {
      erroDiv.textContent = 'Cliente não encontrado.';
      erroDiv.classList.remove('d-none');
      return;
    }

    erroDiv.classList.add('d-none');

  } catch (err) {
    console.error('Erro ao carregar perfil:', err);
    erroDiv.textContent = `Erro ao carregar perfil: ${err.message}`;
    erroDiv.classList.remove('d-none');
  }
}
