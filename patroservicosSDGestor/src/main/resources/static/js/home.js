/*
 * ARQUIVO NÃO UTILIZADO - COMENTADO
 * 
 * Motivo: Todas as funções deste arquivo estão comentadas (precedidas por //).
 * Este é aparentemente um arquivo antigo ou descontinuado. As funcionalidades
 * de listagem de profissionais e filtros provavelmente foram movidas ou reimplementadas
 * em outro arquivo ou componente. Manter para referência histórica.
 */
/*
// // ====== Funções da Home (Listagem de Profissionais) ======
// // scrollToLista: rola suavemente até o cabeçalho da lista
// function scrollToLista() {
//   document.getElementById('listaHeader')?.scrollIntoView({behavior:'smooth'});
// }

// // carregarProfissionais: busca profissionais via gateway e monta os cards
// async function carregarProfissionais() {
//   const listaDiv = document.getElementById('listaProfissionais');
//   const vazio = document.getElementById('semProfissionais');
//   const contador = document.getElementById('contadorProfissionais');
//   const prof = document.getElementById('filtroProfissao').value.trim();
//   const cid = document.getElementById('filtroCidade').value.trim();

//   // Montagem da URL com filtros opcionais
//   let url = '/api/profissionais';
//   const params = [];
//   if (prof) params.push('profissao=' + encodeURIComponent(prof));
//   if (cid) params.push('cidade=' + encodeURIComponent(cid));
//   if (params.length) url += '?' + params.join('&');

//   // Estado de carregamento
//   listaDiv.innerHTML = `
//     <div class="col-12 text-center py-5">
//       <div class="spinner-border text-primary"></div>
//       <p class="mt-3 text-muted">Carregando profissionais...</p>
//     </div>`;
//   vazio.classList.add('d-none');
//   contador.textContent = '...';

//   try {
//     const resp = await fetch(url);
//     if (!resp.ok) throw new Error('Falha');
//     const dados = await resp.json();

//     // Nenhum resultado
//     if (!Array.isArray(dados) || !dados.length) {
//       listaDiv.innerHTML = '';
//       vazio.classList.remove('d-none');
//       contador.textContent = '0';
//       return;
//     }

//     contador.textContent = dados.length.toString();

//     // Criação dos cards
//     listaDiv.innerHTML = dados.map(d => {
//       const iniciais = (d.nome || 'P').split(' ').slice(0,2).map(p=>p[0]).join('').toUpperCase();
//       const local = [d.cidade, d.estado].filter(Boolean).join('/') || '—';
//       const desc = (d.descricao || 'Profissional qualificado.');
//       const descShort = desc.length > 90 ? desc.substring(0,87) + '...' : desc;
//       return `
//         <div class="col-12 col-sm-6 col-md-4 col-lg-3">
//           <div class="card h-100 shadow-sm border-0"> 
//             <div class="card-body d-flex flex-column p-3">
//               <div class="d-flex align-items-center mb-3">
//                 <div class="rounded-circle me-3 text-white d-flex align-items-center justify-content-center" 
//                      style="width:56px;height:56px;background:linear-gradient(135deg,#667eea,#764ba2);font-weight:600;">
//                   ${iniciais}
//                 </div>
//                 <div class="flex-grow-1">
//                   <div class="fw-semibold text-truncate" title="${d.nome || ''}">${d.nome || '—'}</div>
//                   <div class="small text-muted text-truncate"><i class="bi bi-briefcase me-1"></i>${d.profissao || '—'}</div>
//                 </div>
//               </div>
//               <p class="small text-muted flex-grow-1">${descShort}</p>
//               <div class="d-flex justify-content-between align-items-center mt-auto">
//                 <span class="badge bg-light text-dark"><i class="bi bi-geo-alt me-1"></i>${local}</span>
//                 <a class="btn btn-sm btn-primary" href="/profissional/perfil-publico?id=${d.id}">
//                   <i class="bi bi-eye"></i>
//                 </a>
//               </div>
//             </div>
//           </div>
//         </div>`;
//     }).join('');
//   } catch (e) {
//     console.error(e);
//     listaDiv.innerHTML = `
//       <div class="col-12 text-center py-5">
//         <i class="bi bi-exclamation-triangle text-danger display-5 d-block mb-3"></i>
//         <h5 class="text-danger">Erro ao carregar profissionais</h5>
//         <button class="btn btn-primary mt-2" onclick="carregarProfissionais()">
//           <i class="bi bi-arrow-clockwise me-1"></i>Tentar novamente
//         </button>
//       </div>`;
//     contador.textContent = '!';
//   }
// }

// // resetFiltros: limpa campos e recarrega a listagem
// function resetFiltros() {
//   document.getElementById('filtroProfissao').value='';
//   document.getElementById('filtroCidade').value='';
//   carregarProfissionais();
// }

// // Inicializa busca ao carregar a página
// document.addEventListener('DOMContentLoaded', () => {
//   carregarProfissionais();
// });

console.log('Home JS deu alguma coisa');
*/