// Carregar perfil do cliente logado ao iniciar
document.addEventListener('DOMContentLoaded', function() {
  carregarPerfilCliente();
  
  // Registrar submit do formulário
  const form = document.getElementById('formPerfilCliente');
  if (form) {
    form.addEventListener('submit', salvarPerfilCliente);
  }
});

// Carregar dados do perfil do cliente
async function carregarPerfilCliente() {
  const clienteId = sessionStorage.getItem('clienteId');
  if (!clienteId) {
    mostrarAlerta('Você não está logado. Faça login para editar seu perfil.', 'erro');
    setTimeout(() => window.location.href = 'loginCliente.html', 2000);
    return;
  }

  document.getElementById('clienteId').value = clienteId;
  
  try {
    // PRIMEIRO: Tentar carregar perfil do cliente do SD1 (via Gestor) para pré-preencher dados
    let perfilSD1 = null;
    try {
      const respSD1 = await fetch((window.API_BASE || '') + `/api/clientes/${clienteId}`);
      if (respSD1.ok) {
        perfilSD1 = await respSD1.json();
        // Preencher com dados do SD1 se existirem
        if (perfilSD1.nome) document.getElementById('nome').value = perfilSD1.nome;
        if (perfilSD1.email) document.getElementById('email').value = perfilSD1.email;
        if (perfilSD1.numero) document.getElementById('telefone').value = perfilSD1.numero;
        // Armazenar nome do cliente no sessionStorage para exibição na navbar
        if (perfilSD1.nome) sessionStorage.setItem('clienteNome', perfilSD1.nome);
      }
    } catch (err) {
      console.warn('Não foi possível carregar dados do SD1:', err);
    }

    // SEGUNDO: Carregar perfil do cliente em SD3 via Gestor (dados adicionais como foto e descrição)
    const resp = await fetch((window.API_BASE || '') + `/api/perfil-cliente/${clienteId}`);
    if (!resp.ok) {
      // Perfil não existe ainda (novo cliente) — deixar campos pré-preenchidos do SD1
      mostrarAlerta('Perfil novo - preencha seus dados adicionais.', 'info');
      return;
    }
    
    const data = await resp.json();
    // Preencher campos com dados existentes do SD3 (sobrescreve SD1 se necessário)
    if (data.nome) document.getElementById('nome').value = data.nome;
    if (data.email) document.getElementById('email').value = data.email;
    if (data.numero) document.getElementById('telefone').value = data.numero;
    if (data.descricao) document.getElementById('descricao').value = data.descricao;
    if (data.foto_perfil) {
      document.getElementById('avatarPlaceholder').innerHTML = 
        `<img src="${data.foto_perfil}" alt="Foto de perfil">`;
    }
    // Armazenar nome e foto no sessionStorage para exibição na navbar
    if (data.nome) sessionStorage.setItem('clienteNome', data.nome);
    if (data.foto_perfil) sessionStorage.setItem('clienteFoto', data.foto_perfil);
  } catch (err) {
    console.error('Erro ao carregar perfil:', err);
    mostrarAlerta('Erro ao carregar perfil. Tente novamente.', 'erro');
  }
}

// Preview de foto antes de salvar
function previewFoto(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function(e) {
      document.getElementById('avatarPlaceholder').innerHTML = 
        `<img src="${e.target.result}" alt="Preview">`;
    };
    reader.readAsDataURL(file);
  }
}

// Salvar perfil do cliente
async function salvarPerfilCliente(event) {
  event.preventDefault();
  
  const clienteId = document.getElementById('clienteId').value;
  if (!clienteId) {
    mostrarAlerta('Cliente não identificado.', 'erro');
    return;
  }
  
  const nome = document.getElementById('nome').value?.trim();
  const email = document.getElementById('email').value?.trim();
  const telefone = document.getElementById('telefone').value?.trim();
  const descricao = document.getElementById('descricao').value?.trim();
  
  if (!nome || !email) {
    mostrarAlerta('Nome e email são obrigatórios.', 'erro');
    return;
  }
  
  // Preparar payload com foto em base64 (se houver arquivo novo)
  let fotoBase64 = '';
  const fileInput = document.getElementById('fotoPerfil');
  if (fileInput.files.length > 0) {
    const file = fileInput.files[0];
    fotoBase64 = await fileToBase64(file);
  }
  
  const payload = {
    cliente_id: clienteId,
    nome: nome,
    email: email,
    numero: telefone,
    descricao: descricao,
    foto_perfil: fotoBase64
  };
  
  const btn = event.target.querySelector('button[type="submit"]');
  btn.disabled = true;
  btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Salvando...';
  
  try {
    // Enviar para Gestor com header X-Cliente-Id
    const resp = await fetch((window.API_BASE || '') + '/api/perfil-cliente', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Cliente-Id': clienteId
      },
      body: JSON.stringify(payload)
    });
    
    if (!resp.ok) {
      const error = await resp.json();
      mostrarAlerta(`Erro ao salvar: ${error.error || resp.status}`, 'erro');
      return;
    }
    
    mostrarAlerta('Perfil salvo com sucesso!', 'sucesso');
    // Limpar input de arquivo
    fileInput.value = '';
    
  } catch (err) {
    console.error('Erro salvando perfil:', err);
    mostrarAlerta(`Erro: ${err.message}`, 'erro');
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="bi bi-check me-1"></i>Salvar Alterações';
  }
}

// Converter arquivo para base64
function fileToBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}

// Mostrar alerta (sucesso ou erro)
function mostrarAlerta(mensagem, tipo) {
  const erroDiv = document.getElementById('erroPerfil');
  const sucessoDiv = document.getElementById('sucessoPerfil');
  
  // Limpar alertas anteriores
  erroDiv.classList.add('d-none');
  sucessoDiv.classList.add('d-none');
  
  if (tipo === 'erro') {
    erroDiv.textContent = mensagem;
    erroDiv.classList.remove('d-none');
  } else if (tipo === 'sucesso') {
    sucessoDiv.textContent = mensagem;
    sucessoDiv.classList.remove('d-none');
  } else if (tipo === 'info') {
    // info usa sucesso (cor azul) ou criar novo
    sucessoDiv.textContent = mensagem;
    sucessoDiv.classList.remove('d-none');
  }
  
  // Auto-limpar após 5s
  setTimeout(() => {
    erroDiv.classList.add('d-none');
    sucessoDiv.classList.add('d-none');
  }, 5000);
}
