function getParam(nome){return new URL(window.location.href).searchParams.get(nome);} 

async function carregarPerfilPublico(){
  const id=getParam('id');
  const erroDiv=document.getElementById('erroPublico');
  const erroTxt=document.getElementById('erroTexto');
  if(!id){if(erroDiv&&erroTxt){erroTxt.textContent='ID do profissional não informado.';erroDiv.classList.remove('d-none');}return;}
  const resp=await fetch((window.API_BASE||'')+`/api/profissionais/${id}/perfil`);
  if(!resp.ok){if(erroDiv&&erroTxt){erroTxt.textContent='Perfil não encontrado.';erroDiv.classList.remove('d-none');}return;}
  const d=await resp.json();
  document.getElementById('nomeProfissional').textContent=d.nome||'Profissional';
  
  // Avatar com foto de perfil ou iniciais
  const fotoPerfil = d.perfil?.fotoPerfil;
  const iniciais=(d.nome||'P').split(' ').slice(0,2).map(p=>p[0]).join('').toUpperCase();
  const avatarDiv = document.getElementById('iniciaisPerfil');
  if(fotoPerfil) {
    avatarDiv.innerHTML = `<img src="${fotoPerfil}" alt="${d.nome}" style="width:100%;height:100%;object-fit:cover;border-radius:50%;">`;
  } else {
    avatarDiv.textContent = iniciais;
  }
  
  document.getElementById('profissaoText').textContent=d.profissao||'—';
  document.getElementById('localizacaoText').textContent=[d.cidade,d.estado].filter(Boolean).join(', ')||'—';
  document.getElementById('descricaoText').textContent=d.descricao||'Sem descrição fornecida.';
  document.getElementById('emailProfissional').textContent=d.email||'—';
  document.getElementById('numeroProfissional').textContent=d.numero||'—';
  document.getElementById('cidadeEstadoProfissional').textContent=[d.cidade,d.estado].filter(Boolean).join(', ')||'—';
  const verificado=!!(d.documento&&d.documento.verificado); if(typeof atualizarStatusVerificado==='function') atualizarStatusVerificado(verificado);
  const galeria=document.getElementById('galeriaFotos'); const semFotos=document.getElementById('semFotos'); galeria.innerHTML='';
  const parseFotos=str=>{ if(!str)return[]; const tokens=str.split(','); const out=[]; let cur=''; for(const tRaw of tokens){const t=tRaw.trim(); if(!t)continue; if(/^https?:\/\//i.test(t)){ if(cur) out.push(cur); cur=t; } else { cur=cur?cur+','+t:t; }} if(cur) out.push(cur); return out; };
  const fotosArr=parseFotos(d.perfil?.fotos);
  if(!fotosArr.length){semFotos?.classList.remove('d-none');} else { semFotos?.classList.add('d-none'); const norm=u=>{ if(!u)return''; if(/^https?:\/\//i.test(u))return u; if(u.startsWith('/uploads')) return (window.SD1_BASE_URL||'')+u; return u; }; galeria.innerHTML=fotosArr.map(u=>`<div class="col-6 col-md-4"><div class="card h-100 shadow-sm"><img src="${norm(u)}" class="card-img-top" style="object-fit:cover;height:160px" onerror="this.src='https://via.placeholder.com/300?text=Foto'"></div></div>`).join(''); }
  const listaCert=document.getElementById('listaCertificados'); const semCert=document.getElementById('semCertificados'); listaCert.innerHTML='';
  const certArr=d.perfil?.certificados?d.perfil.certificados.split(',').map(s=>s.trim()).filter(Boolean):[];
  if(!certArr.length){semCert?.classList.remove('d-none');} else { semCert?.classList.add('d-none'); listaCert.innerHTML=certArr.map(c=>`<div class="list-group-item">${c}</div>`).join(''); }
  const btn=document.getElementById('btnConversar'); if(btn) btn.dataset.profissionalId=d.profissionalId;
  
  // NOVO: Carregar feedbacks do profissional
  await carregarFeedbacksProfissional(d.profissionalId);
  
  // NOVO: Validar cliente logado e mostrar/ocultar form de feedback
  const clienteId=sessionStorage.getItem('clienteId');
  const clienteLogin=sessionStorage.getItem('clienteLogin');
  const formFeedback=document.getElementById('formFeedback');
  if(formFeedback){
    if(clienteId||clienteLogin){
      formFeedback.style.display='block';
      formFeedback.dataset.profissionalId=d.profissionalId;
      formFeedback.dataset.clienteId=clienteId;
    } else {
      formFeedback.style.display='none';
      const semLogar=document.createElement('div');
      semLogar.className='alert alert-info';
      semLogar.innerHTML='<i class="bi bi-info-circle me-2"></i>Faça <a href="loginCliente.html">login como cliente</a> para deixar uma avaliação.';
      formFeedback.parentElement?.insertBefore(semLogar,formFeedback);
    }
  }
}
// NOVO: Carregar feedbacks do profissional
async function carregarFeedbacksProfissional(profissionalId){
  try{const resp=await fetch((window.API_BASE||'')+`/api/feedbacks/profissional/${profissionalId}`);
  if(!resp.ok){console.warn('Erro ao carregar feedbacks:',resp.status);return;}
  const feedbacks=await resp.json();
  renderizarFeedbacks(feedbacks);}catch(err){console.error('Erro carregando feedbacks:',err);}}

// NOVO: Renderizar feedbacks na página
function renderizarFeedbacks(feedbacks){
  const container=document.getElementById('listaFeedbacks');
  const emptyState=document.getElementById('semFeedbacks');
  if(!container)return;
  if(!feedbacks||feedbacks.length===0){container.innerHTML='';if(emptyState)emptyState.classList.remove('d-none');atualizarEstatisticasAvaliacao([]);return;}
  if(emptyState)emptyState.classList.add('d-none');
  
  // Atualiza estatísticas de avaliação
  atualizarEstatisticasAvaliacao(feedbacks);
  
  container.innerHTML=feedbacks.map(fb=>{
    const stars='<i class="bi bi-star-fill"></i>'.repeat(fb.nota||0)+'<i class="bi bi-star"></i>'.repeat(5-(fb.nota||0));
    const dataFmt=new Date(fb.data_feedback).toLocaleDateString('pt-BR');
    
    // Usar cliente_nome se disponível (preenchido do perfil), senão fallback
    const nomeCliente = fb.cliente_nome || `Cliente #${fb.cliente_id}`;
    const clienteId = fb.cliente_id;
    
    // Avatar: usar cliente_foto se disponível (base64), senão iniciais
    let avatarHtml;
    if(fb.cliente_foto) {
      avatarHtml = `<img src="${fb.cliente_foto}" alt="Foto" style="width:40px;height:40px;border-radius:50%;object-fit:cover;margin-right:12px;">`;
    } else {
      const initials=(nomeCliente||'C').split(' ').slice(0,2).map(p=>p[0]).join('').toUpperCase();
      avatarHtml = `<div class="avatar-sm" style="background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);width:40px;height:40px;border-radius:50%;display:flex;align-items:center;justify-content:center;color:white;font-weight:600;font-size:0.9rem;margin-right:12px;">${initials}</div>`;
    }
    
    // Nome clicável que redireciona para perfil público do cliente
    const nomeLink = `<a href="./cliente-perfil-publico.html?id=${clienteId}" style="cursor:pointer;text-decoration:none;color:inherit;font-weight:600;" class="cliente-link">${nomeCliente}</a>`;
    
    return`<div class="feedback-card card border-0 mb-3"><div class="card-body"><div class="d-flex align-items-start gap-3"><div>${avatarHtml}</div><div class="flex-grow-1"><div class="d-flex justify-content-between align-items-start mb-2"><div><h6 class="mb-1">${nomeLink}</h6><div class="rating-stars small">${stars}</div></div><small class="text-muted">${dataFmt}</small></div>${fb.comentario?`<p class="mb-0 text-dark">${fb.comentario}</p>`:''}</div></div></div></div>`;}).join('');}

// NOVO: Atualizar estatísticas de avaliação
function atualizarEstatisticasAvaliacao(feedbacks){
  if(!feedbacks||feedbacks.length===0){
    // Sem feedbacks: mostrar valores padrão
    document.getElementById('mediaAvaliacao').textContent='0.0';
    document.getElementById('totalAvaliacoes').textContent='0';
    for(let i=1;i<=5;i++){
      document.getElementById(`bar${i}`).style.width='0%';
      document.getElementById(`count${i}`).textContent='0';
    }
    return;
  }
  
  // Contar avaliações por estrela
  const counts={1:0,2:0,3:0,4:0,5:0};
  let somaNotas=0;
  
  feedbacks.forEach(fb=>{
    const nota=parseInt(fb.nota)||0;
    if(nota>=1&&nota<=5){
      counts[nota]++;
      somaNotas+=nota;
    }
  });
  
  const total=feedbacks.length;
  const media=(somaNotas/total).toFixed(1);
  
  // Atualizar média
  document.getElementById('mediaAvaliacao').textContent=media;
  document.getElementById('totalAvaliacoes').textContent=total;
  
  // Atualizar barras e contadores
  for(let i=5;i>=1;i--){
    const pct=total>0?(counts[i]/total)*100:0;
    document.getElementById(`bar${i}`).style.width=pct+'%';
    document.getElementById(`count${i}`).textContent=counts[i];
  }
  
  // Atualizar estrelas de visualização da média no topo
  atualizarEsrelasMedia(media);
}

// Função para atualizar as 5 estrelas do display de média
function atualizarEsrelasMedia(media){
  const mediaDiv=document.querySelector('.display-6.fw-bold.text-primary').parentElement;
  if(!mediaDiv)return;
  
  const starsDiv=mediaDiv.querySelector('.rating-stars');
  if(!starsDiv)return;
  
  const notaNum=parseFloat(media);
  let starsHtml='';
  
  for(let i=1;i<=5;i++){
    if(i<=Math.floor(notaNum)){
      starsHtml+='<i class="bi bi-star-fill"></i>';
    } else if(i-notaNum<1&&i-notaNum>0){
      starsHtml+='<i class="bi bi-star-half"></i>';
    } else {
      starsHtml+='<i class="bi bi-star"></i>';
    }
  }
  
  starsDiv.innerHTML=starsHtml;
}
async function enviarFeedback(event){
  event.preventDefault();
  const form=event.target;
  const profissionalId=form.dataset.profissionalId;
  const clienteId=form.dataset.clienteId||sessionStorage.getItem('clienteId');
  const nota=parseInt(document.getElementById('ratingValue')?.value||0);
  const comentario=document.getElementById('comentario')?.value||'';
  if(!clienteId){alert('Erro: cliente não identificado. Faça login novamente.');return;}
  if(nota===0){alert('Por favor, selecione uma avaliação com estrelas.');return;}
  if(!comentario.trim()){alert('Por favor, escreva um comentário.');return;}
  const payload={profissional_id:profissionalId,cliente_id:clienteId,nota:nota,comentario:comentario};
  try{
    const resp=await fetch((window.API_BASE||'')+'/api/feedbacks',{method:'POST',headers:{'Content-Type':'application/json','X-Cliente-Id':clienteId},body:JSON.stringify(payload)});
    if(!resp.ok){const error=await resp.json();alert('Erro ao enviar feedback: '+(error.error||resp.status));return;}
    alert('Avaliação enviada com sucesso! Obrigado pelo feedback.');
    form.reset();
    setRating(0);
    await carregarFeedbacksProfissional(profissionalId);
  }catch(err){console.error('Erro enviando feedback:',err);alert('Erro ao enviar feedback: '+err.message);}}

function iniciarChat(){const profissionalId=document.getElementById('btnConversar')?.dataset.profissionalId; if(!profissionalId) return alert('ID do profissional não encontrado.'); alert('Iniciar chat com o profissional '+profissionalId+' (endpoint não implementado).'); }

// Rating system
let currentRating=0;
function setRating(rating){currentRating=rating;const ratingValue=document.getElementById('ratingValue');if(ratingValue)ratingValue.value=rating;highlightStars(rating);}
function highlightStars(rating){
  // Seleciona todas as estrelas pelo atributo data-rating (sempre retorna todos)
  const ratingInput = document.getElementById('ratingInput');
  if(!ratingInput) return;
  const stars = ratingInput.querySelectorAll('i[data-rating]');
  stars.forEach((star)=>{
    const starRating = parseInt(star.getAttribute('data-rating')) || 0;
    if(starRating <= rating){
      star.className = 'bi bi-star-fill';
    } else {
      star.className = 'bi bi-star';
    }
  });
}

document.addEventListener('DOMContentLoaded',function(){
  carregarPerfilPublico();

  // Inicializa sistema de estrelas de forma idempotente (evita múltiplos listeners)
  const ratingInput = document.getElementById('ratingInput');
  if (ratingInput && !ratingInput.dataset.bound) {
    const stars = ratingInput.querySelectorAll('i[data-rating]');
    stars.forEach(star => {
      // Hover: apenas preview (não altera currentRating)
      star.addEventListener('pointerenter', function() {
        const rating = parseInt(this.getAttribute('data-rating')) || 0;
        highlightStars(rating);
      });

      // Click: definir avaliação (previne propagação dupla)
      star.addEventListener('click', function(e) {
        e.stopPropagation();
        const rating = parseInt(this.getAttribute('data-rating')) || 0;
        setRating(rating);
      });
    });

    // Ao sair do bloco de estrelas, restaura para a avaliação atual
    ratingInput.addEventListener('pointerleave', function() {
      highlightStars(currentRating);
    });

    // Marca como inicializado para não reatachar listeners
    ratingInput.dataset.bound = '1';
  }

  const formFeedback = document.getElementById('formFeedback');
  if (formFeedback && !formFeedback.dataset.bound) {
    formFeedback.addEventListener('submit', enviarFeedback);
    formFeedback.dataset.bound = '1';
  }
});
