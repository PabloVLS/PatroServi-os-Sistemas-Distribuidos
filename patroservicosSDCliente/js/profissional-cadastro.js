function api(p){return `${window.API_BASE}${p}`;}
async function enviarCadastroUpload(){
  const form=document.getElementById('formCadastroProfissional');
  const fd=new FormData();
  const dados={ nome:nome.value, numero:numero.value, email:email.value, profissao:profissao.value, descricao:descricao.value, cidade:cidade.value, estado:estado.value, cpf:cpf.value, rg:rg.value, certificados:certificados.value };
  fd.append('dados', new Blob([JSON.stringify(dados)], {type:'application/json'}));
  if(form.fotoCpf.files[0]) fd.append('fotoCpf', form.fotoCpf.files[0]);
  if(form.fotoRg.files[0]) fd.append('fotoRg', form.fotoRg.files[0]);
  if(form.fotoPerfil.files[0]) fd.append('fotoPerfil', form.fotoPerfil.files[0]);
  for(const f of form.portfolio.files) fd.append('portfolio', f);
  const resp=await fetch(api('/api/profissionais/cadastro-completo-upload'),{method:'POST',body:fd});
  if(!resp.ok){alert('Erro no cadastro');return;}
  const r=await resp.json();
  const login=document.getElementById('login').value; const senha=document.getElementById('senha').value;
  if(login && senha){
    try{
      const usuarioResp=await fetch(api('/api/usuarios'),{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({login,senha,profissionalId:r.profissionalId})});
      if(usuarioResp.status===201){const usuarioJson=await usuarioResp.json(); alert('Cadastro ok! Profissional ID: '+r.profissionalId+'\nUsuário ID: '+usuarioJson.id);} else if(usuarioResp.status===409){alert('Profissional criado, mas login já existe. Escolha outro login.');} else {alert('Profissional criado, problema ao criar usuário.');}
    }catch(e){console.error(e);alert('Profissional criado, erro ao criar usuário.');}
  }else{alert('Cadastro ok (profissional). Login/Senha não preenchidos.');}
  form.reset();
}
