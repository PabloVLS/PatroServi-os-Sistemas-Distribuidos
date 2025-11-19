// const apiClientes = '/api/clientes';
// const tbodyClientes = document.getElementById('tbodyClientes');
// const filtroNome = document.getElementById('filtroNome');

// async function carregarClientes() {
//   const nome = filtroNome.value.trim();
//   const url = nome ? `${apiClientes}/pesquisa?nome=${encodeURIComponent(nome)}` : apiClientes;
//   const resp = await fetch(url);
//   const dados = await resp.json();
//   tbodyClientes.innerHTML = dados.map(c => `
//     <tr>
//       <td>${c.nome||''}</td>
//       <td>${c.email||''}</td>
//       <td>${c.numero||''}</td>
//       <td>${c.dataCadastro?.substring(0,19)||''}</td>
//       <td class="text-end">
//         <button class="btn btn-sm btn-outline-primary me-1" onclick="editarCliente('${c.id}')">Editar</button>
//         <button class="btn btn-sm btn-outline-danger" onclick="excluirCliente('${c.id}')">Del</button>
//       </td>
//     </tr>`).join('');
// }

// filtroNome?.addEventListener('input', () => carregarClientes());

// function novoCliente() {
//   document.getElementById('clienteId').value = '';
//   document.getElementById('clienteNome').value = '';
//   document.getElementById('clienteEmail').value = '';
//   document.getElementById('clienteNumero').value = '';
//   document.getElementById('tituloModalCliente').textContent = 'Novo Cliente';
//   document.getElementById('erroCliente').textContent = '';
// }

// async function editarCliente(id) {
//   const resp = await fetch(`${apiClientes}/${id}`);
//   const c = await resp.json();
//   document.getElementById('clienteId').value = c.id;
//   document.getElementById('clienteNome').value = c.nome||'';
//   document.getElementById('clienteEmail').value = c.email||'';
//   document.getElementById('clienteNumero').value = c.numero||'';
//   document.getElementById('tituloModalCliente').textContent = 'Editar Cliente';
//   new bootstrap.Modal('#modalCliente').show();
// }

// async function salvarCliente(e) {
//   e.preventDefault();
//   const id = document.getElementById('clienteId').value;
//   const body = {
//     nome: document.getElementById('clienteNome').value,
//     email: document.getElementById('clienteEmail').value,
//     numero: document.getElementById('clienteNumero').value
//   };
//   const metodo = id ? 'PUT' : 'POST';
//   const url = id ? `${apiClientes}/${id}` : apiClientes;
//   const resp = await fetch(url, {
//     method: metodo,
//     headers: { 'Content-Type':'application/json' },
//     body: JSON.stringify(body)
//   });
//   if (!resp.ok) {
//     const err = await resp.json().catch(()=>({}));
//     document.getElementById('erroCliente').textContent = err.mensagem||'Erro';
//     return;
//   }
//   bootstrap.Modal.getInstance(document.getElementById('modalCliente')).hide();
//   carregarClientes();
// }

// async function excluirCliente(id) {
//   if (!confirm('Excluir cliente?')) return;
//   await fetch(`${apiClientes}/${id}`, { method:'DELETE' });
//   carregarClientes();
// }

// document.addEventListener('DOMContentLoaded', carregarClientes);