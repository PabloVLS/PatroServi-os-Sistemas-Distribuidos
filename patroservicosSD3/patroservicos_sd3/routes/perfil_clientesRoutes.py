from flask import Blueprint, current_app, request, jsonify
from ..models import parse_objectid
from ..models.perfil_clientes import build_perfil, build_update

bp = Blueprint('perfil_clientes', __name__)


@bp.route('/api/perfil-cliente/<cliente_id>', methods=['GET'])
def get_perfil_cliente(cliente_id):
    doc = current_app.perfil_cliente_col.find_one({'cliente_id': str(cliente_id)})
    if not doc:
        return jsonify({'error': 'perfil não encontrado'}), 404
    return jsonify(parse_objectid(doc))


@bp.route('/api/perfil-cliente', methods=['POST'])
def create_perfil_cliente():
    payload = request.get_json(force=True)
    try:
        doc = build_perfil(payload)
    except ValueError as e:
        return jsonify({'error': str(e)}), 400

    cliente_id = doc['cliente_id']
    current_app.perfil_cliente_col.update_one({'cliente_id': cliente_id}, {'$set': doc}, upsert=True)
    ret = current_app.perfil_cliente_col.find_one({'cliente_id': cliente_id})
    return jsonify(parse_objectid(ret)), 201


@bp.route('/api/perfil-cliente/<cliente_id>', methods=['PUT'])
def update_perfil_cliente(cliente_id):
    payload = request.get_json(force=True)
    update = build_update(payload)
    if not update:
        return jsonify({'error': 'nenhum campo para atualizar'}), 400
    res = current_app.perfil_cliente_col.update_one({'cliente_id': str(cliente_id)}, {'$set': update})
    if res.matched_count == 0:
        return jsonify({'error': 'perfil não encontrado'}), 404
    ret = current_app.perfil_cliente_col.find_one({'cliente_id': str(cliente_id)})
    return jsonify(parse_objectid(ret))
