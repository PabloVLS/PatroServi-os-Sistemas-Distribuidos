// function getParam(nome) {
//   const url = new URL(window.location.href);
//   return url.searchParams.get(nome);
// }

// async function carregarPerfil() {
//   const id = getParam('id');
//   if (!id) return;
//   document.getElementById('profissionalId').value = id;
//   const resp = await fetch(`/api/profissionais/${id}/perfil`);
//   if (!resp.ok) return;
//   const d = await resp.json();

//   document.getElementById('nome').value = d.nome || '';
//   document.getElementById('numero').value = d.numero || '';
//   document.getElementById('email').value = d.email || '';
//   document.getElementById('profissao').value = d.profissao || '';
//   document.getElementById('cidade').value = d.cidade || '';
//   document.getElementById('estado').value = d.estado || '';
//   document.getElementById('descricao').value = d.descricao || '';

//   if (d.documento) {
//     document.getElementById('cpf').value = d.documento.cpf || '';
//     document.getElementById('rg').value = d.documento.rg || '';
//     document.getElementById('fotoCpf').value = d.documento.fotoCpf || '';
//     document.getElementById('fotoRg').value = d.documento.fotoRg || '';
//     document.getElementById('verificado').value = d.documento.verificado ? 'Sim' : 'Não';
//   } else {
//     document.getElementById('verificado').value = 'Não';
//   }

//   if (d.perfil) {
//     document.getElementById('fotos').value = d.perfil.fotos || '';
//     document.getElementById('certificados').value = d.perfil.certificados || '';
//   }
// }

// async function salvarPerfil(e) {
//   e.preventDefault();
//   const id = document.getElementById('profissionalId').value;
//   const body = {
//     nome: document.getElementById('nome').value,
//     numero: document.getElementById('numero').value,
//     email: document.getElementById('email').value,
//     profissao: document.getElementById('profissao').value,
//     descricao: document.getElementById('descricao').value,
//     cidade: document.getElementById('cidade').value,
//     estado: document.getElementById('estado').value,
//     cpf: document.getElementById('cpf').value,
//     rg: document.getElementById('rg').value,
//     fotoCpf: document.getElementById('fotoCpf').value,
//     fotoRg: document.getElementById('fotoRg').value,
//     fotos: document.getElementById('fotos').value,
//     certificados: document.getElementById('certificados').value
//   };
//   const resp = await fetch(`/api/profissionais/${id}/perfil`, {
//     method: 'PUT',
//     headers: { 'Content-Type': 'application/json' },
//     body: JSON.stringify(body)
//   });
//   const erro = document.getElementById('erroPerfil');
//   if (!resp.ok) {
//     const err = await resp.json().catch(()=>({}));
//     erro.textContent = err.mensagem || 'Erro ao salvar';
//     return;
//   }
//   erro.textContent = '';
//   await carregarPerfil();
//   alert('Perfil atualizado!');
// }

// document.addEventListener('DOMContentLoaded', carregarPerfil);

console.log('Perfil Profissional JS carregado');