from flask import Blueprint, current_app, request, jsonify
from bson.objectid import ObjectId
from ..models import parse_objectid
from ..models.feedback import build_feedback
from datetime import datetime

bp = Blueprint('feedbacks', __name__)


def enrich_feedback(feedback_doc):
    """
    Enriquece um feedback com dados do cliente (nome, foto_perfil).
    Se perfil do cliente não existir, mantém só o cliente_id.
    """
    if 'cliente_id' in feedback_doc:
        cliente_id = feedback_doc['cliente_id']
        perfil = current_app.perfil_cliente_col.find_one({'cliente_id': str(cliente_id)})
        if perfil:
            feedback_doc['cliente_nome'] = perfil.get('nome', f'Cliente {cliente_id}')
            feedback_doc['cliente_foto'] = perfil.get('foto_perfil')
    return feedback_doc


@bp.route('/api/feedbacks', methods=['POST'])
def create_feedback():
    payload = request.get_json(force=True)
    try:
        feedback = build_feedback(payload)
    except ValueError as e:
        return jsonify({'error': str(e)}), 400

    res = current_app.feedbacks_col.insert_one(feedback)
    feedback['_id'] = res.inserted_id
    feedback = enrich_feedback(feedback)
    return jsonify(parse_objectid(feedback)), 201


@bp.route('/api/feedbacks/<fid>', methods=['GET'])
def get_feedback(fid):
    try:
        doc = current_app.feedbacks_col.find_one({'_id': ObjectId(fid)})
    except Exception:
        return jsonify({'error': 'id inválido'}), 400
    if not doc:
        return jsonify({'error': 'feedback não encontrado'}), 404
    doc = enrich_feedback(doc)
    return jsonify(parse_objectid(doc))


@bp.route('/api/feedbacks/profissional/<profissional_id>', methods=['GET'])
def list_feedbacks_by_profissional(profissional_id):
    docs = current_app.feedbacks_col.find({'profissional_id': str(profissional_id)}).sort('data_feedback', -1)
    out = [enrich_feedback(parse_objectid(d)) for d in docs]
    return jsonify(out)
