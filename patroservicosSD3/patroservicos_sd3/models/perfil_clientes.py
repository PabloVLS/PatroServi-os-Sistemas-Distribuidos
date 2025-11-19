def build_perfil(payload: dict) -> dict:
	if not isinstance(payload, dict):
		raise ValueError('payload inválido')
	if 'cliente_id' not in payload:
		raise ValueError('cliente_id é obrigatório')
	cliente_id = str(payload.get('cliente_id'))
	doc = {
		'cliente_id': cliente_id,
		'nome': payload.get('nome', ''),
		'email': payload.get('email', ''),
		'numero': payload.get('numero', ''),
		'descricao': payload.get('descricao', ''),
		'foto_perfil': payload.get('foto_perfil', ''),
		'preferencias': payload.get('preferencias', {})
	}
	return doc


def build_update(payload: dict) -> dict:
	if not isinstance(payload, dict):
		raise ValueError('payload inválido')
	update = {}
	for k in ['nome', 'email', 'numero', 'descricao', 'foto_perfil', 'preferencias']:
		if k in payload:
			update[k] = payload[k]
	return update

