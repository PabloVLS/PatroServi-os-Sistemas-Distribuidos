// function getParam(nome) {
//   const url = new URL(window.location.href);
//   return url.searchParams.get(nome);
// }

// async function carregarPerfilPublico() {
//   const id = getParam('id');
//   const erroDiv = document.getElementById('erroPublico');
//   const erroTxt = document.getElementById('erroTexto');
//   if (!id) {
//     if (erroDiv && erroTxt) { erroTxt.textContent = 'ID do profissional não informado.'; erroDiv.classList.remove('d-none'); }
//     return;
//   }
//   const resp = await fetch(`/api/profissionais/${id}/perfil`);
//   if (!resp.ok) {
//     if (erroDiv && erroTxt) { erroTxt.textContent = 'Perfil não encontrado.'; erroDiv.classList.remove('d-none'); }
//     return;
//   }
//   const d = await resp.json();

//   // Header
//   document.getElementById('nomeProfissional').textContent = d.nome || 'Profissional';
//   const iniciais = (d.nome || 'P').split(' ').slice(0,2).map(p=>p[0]).join('').toUpperCase();
//   document.getElementById('iniciaisPerfil').textContent = iniciais;
//   document.getElementById('profissaoText').textContent = d.profissao || '—';
//   document.getElementById('localizacaoText').textContent = [d.cidade, d.estado].filter(Boolean).join(', ') || '—';

//   // Sobre
//   document.getElementById('descricaoText').textContent = d.descricao || 'Sem descrição fornecida.';

//   // Contato
//   document.getElementById('emailProfissional').textContent = d.email || '—';
//   document.getElementById('numeroProfissional').textContent = d.numero || '—';
//   document.getElementById('cidadeEstadoProfissional').textContent = [d.cidade, d.estado].filter(Boolean).join(', ') || '—';

//   // Documento status + badge
//   const verificado = !!(d.documento && d.documento.verificado);
//   if (typeof atualizarStatusVerificado === 'function') atualizarStatusVerificado(verificado);

//   // Portfólio
//   const galeria = document.getElementById('galeriaFotos');
//   const semFotos = document.getElementById('semFotos');
//   galeria.innerHTML = '';
//   const fotosArr = d.perfil?.fotos ? d.perfil.fotos.split(',').map(s=>s.trim()).filter(Boolean) : [];
//   if (!fotosArr.length) {
//     semFotos?.classList.remove('d-none');
//   } else {
//     semFotos?.classList.add('d-none');
//     galeria.innerHTML = fotosArr.map(url => `
//       <div class="col-6 col-md-4">
//         <div class="card h-100 shadow-sm">
//           <img src="${url}" class="card-img-top" style="object-fit:cover;height:160px"
//                onerror="this.src='https://via.placeholder.com/300?text=Foto'">
//         </div>
//       </div>`).join('');
//   }

//   // Certificados
//   const listaCert = document.getElementById('listaCertificados');
//   const semCert = document.getElementById('semCertificados');
//   listaCert.innerHTML = '';
//   const certArr = d.perfil?.certificados ? d.perfil.certificados.split(',').map(s=>s.trim()).filter(Boolean) : [];
//   if (!certArr.length) {
//     semCert?.classList.remove('d-none');
//   } else {
//     semCert?.classList.add('d-none');
//     listaCert.innerHTML = certArr.map(c => `<div class="list-group-item">${c}</div>`).join('');
//   }

//   // Botão conversar guarda ID (endpoint futuro)
//   const btn = document.getElementById('btnConversar');
//   if (btn) btn.dataset.profissionalId = d.profissionalId;
// }

// function iniciarChat() {
//   const profissionalId = document.getElementById('btnConversar')?.dataset.profissionalId;
//   if (!profissionalId) return alert('ID do profissional não encontrado.');
//   alert('Iniciar chat com o profissional ' + profissionalId + ' (endpoint ainda não implementado).');
// }

// document.addEventListener('DOMContentLoaded', carregarPerfilPublico);