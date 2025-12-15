package domain;

public class Calamar extends Enemigo {
    private String imagenAbajo;
    private String imagenDerecha;
    private String imagenIzquierda;
    private String imagenArriba;
    private String imagenActual;

    public Calamar(int fila, int col) {
        super(fila, col, 1);
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = true;
        this.puedeRomperBloques = true;
        this.rompeUnBloquePorVez = true;
        this.imagenAbajo = "src/presentation/images/CalamarAbajo.png";
        this.imagenDerecha = "src/presentation/images/CalamarDerecha.png";
        this.imagenIzquierda = "src/presentation/images/CalamarIzquierda.png";
        this.imagenArriba = "src/presentation/images/CalamarDetras.png";
        this.imagenActual = this.imagenAbajo;
        setMovimientoStrategy(new EstrategiaRompeHielo());
    }

    private int contadorTicks = 0;
    private int intervaloMovimiento = 50;
    private int contadorRuptura = 0;
    private int intervaloRuptura = 50;

    @Override
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        if (vista == null || grafo == null || jugador == null) return;
        String direccion = vista.calcularDireccionHaciaObjetivo(
                this.getFila(), this.getColumna(),
                jugador.getFila(), jugador.getColumna(),
                true
        );
        if (direccion == null) return;

        int[] siguiente = grafo.calcularNuevaPosicion(this.getFila(), this.getColumna(), direccion);
        boolean frenteEsHielo = false;
        if (siguiente != null && grafo.esPosicionValida(siguiente[0], siguiente[1])) {
            frenteEsHielo = grafo.esHielo(siguiente[0], siguiente[1]);
        }

        if (frenteEsHielo) {
            contadorRuptura++;
            if (contadorRuptura < intervaloRuptura) return;
            contadorRuptura = 0;
        } else {
            contadorTicks++;
            if (contadorTicks < intervaloMovimiento) return;
            contadorTicks = 0;
        }

        if (estrategiaMovimiento != null) {
            estrategiaMovimiento.ejecutarTurno(this, vista, jugador, grafo);
        }
    }

    @Override
    public void aumentarPuntaje(int puntaje) {

    }

    @Override
    public int getGanancia() {
        return 0;
    }

    @Override
    public void actualizarImagen(String ultimaDireccion) {
        if (ultimaDireccion == null) return;
        switch (ultimaDireccion) {
            case "DERECHA": imagenActual = imagenDerecha; break;
            case "IZQUIERDA": imagenActual = imagenIzquierda; break;
            case "ABAJO": imagenActual = imagenAbajo; break;
            case "ARRIBA": imagenActual = imagenArriba; break;
            default: break;
        }
    }

    @Override
    public String codigoTipo() { return "C"; }

    @Override
    public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {
        if (celdaARomper == null) return;
        celdaARomper.setElementoConTipo("V", creador);
        try {
            celdaARomper.setPermiteReconstruccion(false);
        } catch (Exception e) {
        }
    }

    @Override
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {

    }

    @Override
    public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
        return new int[0];
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

}
