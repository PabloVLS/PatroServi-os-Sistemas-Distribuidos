from patroservicos_sd3 import create_app
import os


app = create_app()


@app.route('/')
def root():
    return 'Serviço 3 (Flask + MongoDB) está rodando!'


if __name__ == '__main__':
    port = app.config.get('PORT', int(os.environ.get('PORT', 8084)))
    # Run without debug/reloader to avoid Windows reloader threading issues
    app.run(host='0.0.0.0', port=port, debug=False)
