// // ====== Perfil Público ======
// // Função utilitária para ler parâmetros de querystring
// function getParam(nome) {
//   const url = new URL(window.location.href);
//   return url.searchParams.get(nome);
// }

// // Carrega dados do perfil público do profissional e preenche a página
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

//   // Header (nome, iniciais, profissão, localização)
//   document.getElementById('nomeProfissional').textContent = d.nome || 'Profissional';
//   const iniciais = (d.nome || 'P').split(' ').slice(0,2).map(p=>p[0]).join('').toUpperCase();
//   document.getElementById('iniciaisPerfil').textContent = iniciais;
//   document.getElementById('profissaoText').textContent = d.profissao || '—';
//   document.getElementById('localizacaoText').textContent = [d.cidade, d.estado].filter(Boolean).join(', ') || '—';

//   // Sobre (descrição)
//   document.getElementById('descricaoText').textContent = d.descricao || 'Sem descrição fornecida.';

//   // Contato
//   document.getElementById('emailProfissional').textContent = d.email || '—';
//   document.getElementById('numeroProfissional').textContent = d.numero || '—';
//   document.getElementById('cidadeEstadoProfissional').textContent = [d.cidade, d.estado].filter(Boolean).join(', ') || '—';

//   // Documento status + badge (verificação)
//   const verificado = !!(d.documento && d.documento.verificado);
//   if (typeof atualizarStatusVerificado === 'function') atualizarStatusVerificado(verificado);

//   // Portfólio (galeria de fotos)
//   const galeria = document.getElementById('galeriaFotos');
//   const semFotos = document.getElementById('semFotos');
//   galeria.innerHTML = '';
//   // parser robusto: alguns nomes de arquivo podem conter vírgulas (ex.: "port,port_...")
//   const parseFotos = (str) => {
//     if (!str) return [];
//     const tokens = str.split(',');
//     const out = [];
//     let cur = '';
//     for (const tRaw of tokens) {
//       const t = tRaw.trim();
//       if (!t) continue;
//       if (/^https?:\/\//i.test(t)) {
//         if (cur) out.push(cur);
//         cur = t;
//       } else {
//         // parte restante de uma URL que continha vírgula no caminho
//         cur = cur ? (cur + ',' + t) : t;
//       }
//     }
//     if (cur) out.push(cur);
//     return out;
//   };
//   const fotosArr = parseFotos(d.perfil?.fotos);
//   if (!fotosArr.length) {
//     semFotos?.classList.remove('d-none');
//   } else {
//     semFotos?.classList.add('d-none');
//     const norm = (u) => {
//       if (!u) return '';
//       if (/^https?:\/\//i.test(u)) return u;
//       if (u.startsWith('/uploads')) return (window.SD1_BASE_URL || '') + u;
//       return u;
//     };
//     galeria.innerHTML = fotosArr.map(u => `
//       <div class="col-6 col-md-4">
//         <div class="card h-100 shadow-sm">
//           <img src="${norm(u)}" class="card-img-top" style="object-fit:cover;height:160px"
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

// // Simulação de início de chat (não implementado)
// function iniciarChat() {
//   const profissionalId = document.getElementById('btnConversar')?.dataset.profissionalId;
//   if (!profissionalId) return alert('ID do profissional não encontrado.');
//   alert('Iniciar chat com o profissional ' + profissionalId + ' (endpoint ainda não implementado).');
// }

// // Inicializa carregamento ao abrir a página
// document.addEventListener('DOMContentLoaded', carregarPerfilPublico);

console.log('perfil publico JS deu alguma coisa');