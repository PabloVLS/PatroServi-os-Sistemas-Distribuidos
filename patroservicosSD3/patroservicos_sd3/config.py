import os


class Config:
    """Configuration for the Flask app, loaded from environment with sensible defaults."""
    MONGO_URI = os.environ.get('MONGO_URI', 'mongodb://localhost:27017')
    MONGO_DB = os.environ.get('MONGO_DB', 'patro_servicos3')
    PORT = int(os.environ.get('PORT', 8084))

