from pymongo import MongoClient


def init_db(app):
    """Initialize MongoDB client and attach common attributes to the app.

    Sets:
      - app.mongo_client
      - app.db
      - app.feedbacks_col
      - app.perfil_cliente_col
    """
    client = MongoClient(app.config['MONGO_URI'])
    app.mongo_client = client
    db = client[app.config['MONGO_DB']]
    app.db = db
    app.feedbacks_col = db['feedbacks']
    app.perfil_cliente_col = db['perfil_clientes']

