from datetime import datetime


def build_feedback(payload: dict) -> dict:
	"""Validate payload and return a feedback document ready to insert.

	Raises ValueError when required fields are missing or invalid.
	"""
	if not isinstance(payload, dict):
		raise ValueError('payload inválido')

	required = ['profissional_id', 'cliente_id', 'nota']
	for r in required:
		if r not in payload:
			raise ValueError(f'campo {r} é obrigatório')

	try:
		nota = int(payload.get('nota'))
	except Exception:
		raise ValueError('campo nota deve ser um número inteiro')

	feedback = {
		'profissional_id': str(payload.get('profissional_id')),
		'cliente_id': str(payload.get('cliente_id')),
		'nota': nota,
		'comentario': payload.get('comentario', ''),
		'data_feedback': payload.get('data_feedback') or datetime.utcnow().isoformat()
	}
	return feedback

