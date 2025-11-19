// async function enviarCadastro(e) {
//   e.preventDefault();
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
//   const resp = await fetch('/api/profissionais/cadastro-completo', {
//     method:'POST',
//     headers:{'Content-Type':'application/json'},
//     body: JSON.stringify(body)
//   });
//   const erroDiv = document.getElementById('erroCadastro');
//   const resultadoDiv = document.getElementById('resultadoCadastro');
//   if (!resp.ok) {
//     const err = await resp.json().catch(()=>({}));
//     erroDiv.textContent = err.mensagem || 'Erro no cadastro';
//     resultadoDiv.textContent = '';
//     return;
//   }
//   erroDiv.textContent = '';
//   const dados = await resp.json();
//   resultadoDiv.innerHTML =
//     `<div class="alert alert-success">
//        Cadastro realizado! Profissional ID: ${dados.profissionalId}<br>
//        Documento ID: ${dados.documentoId}<br>
//        Perfil ID: ${dados.perfilId}
//      </div>`;
//   e.target.reset();
// }

// async function enviarCadastroUpload() {
//   const form = document.getElementById('formCadastroProfissional');
//   const fd = new FormData();
//   // objeto JSON com dados
//   const dados = {
//     nome: document.getElementById('nome').value,
//     numero: document.getElementById('numero').value,
//     email: document.getElementById('email').value,
//     profissao: document.getElementById('profissao').value,
//     descricao: document.getElementById('descricao').value,
//     cidade: document.getElementById('cidade').value,
//     estado: document.getElementById('estado').value,
//     cpf: document.getElementById('cpf').value,
//     rg: document.getElementById('rg').value,
//     certificados: document.getElementById('certificados').value
//   };
//   fd.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }));
//   if (form.fotoCpf.files[0]) fd.append('fotoCpf', form.fotoCpf.files[0]);
//   if (form.fotoRg.files[0]) fd.append('fotoRg', form.fotoRg.files[0]);
//   for (const f of form.portfolio.files) fd.append('portfolio', f);

//   const resp = await fetch('/api/profissionais/cadastro-completo-upload', { method:'POST', body: fd });
//   if (!resp.ok) {
//     alert('Erro no cadastro');
//     return;
//   }
//   const r = await resp.json();
//   alert('Cadastro ok, ID: ' + r.profissionalId);
//   form.reset();
// }