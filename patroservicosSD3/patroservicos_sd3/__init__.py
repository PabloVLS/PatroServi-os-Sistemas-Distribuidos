from flask import Flask

from .config import Config
from .db import init_db


def create_app(config_object=None):
    app = Flask(__name__)
    # Importante: não habilitar CORS aqui.
    # O CORS do frontend é tratado pelo Gestor (gateway) em http://localhost:8081.
    # Se habilitar CORS no SD3 (com wildcard *), o Gestor repassará esse header
    # junto com o dele, causando duplicação de 'Access-Control-Allow-Origin'.

    app.config.from_object(config_object or Config)

    # initialize db and attach collections
    init_db(app)

    # register blueprints from routes package
    # import here to avoid circular imports at module import time
    from .routes.feedbackRoutes import bp as feedback_bp
    from .routes.perfil_clientesRoutes import bp as perfil_bp

    app.register_blueprint(feedback_bp)
    app.register_blueprint(perfil_bp)

    return app
