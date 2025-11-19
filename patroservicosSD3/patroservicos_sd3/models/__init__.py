"""Models package for patroservicos_sd3.

Provides helpers and builders for the application domain.
"""

from .feedback import build_feedback
from .perfil_clientes import build_perfil, build_update


def parse_objectid(doc):
    """Convert a pymongo document to JSON-serializable dict with `id` field."""
    if not doc:
        return None
    doc = dict(doc)
    doc['id'] = str(doc.get('_id'))
    doc.pop('_id', None)
    return doc

__all__ = ['parse_objectid', 'build_feedback', 'build_perfil', 'build_update']
