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

    // Control de velocidad por ticks
    private int contadorTicks = 0;
    // Mayor = más lento (movimiento normal)
    private int intervaloMovimiento = 12;

    // Control específico para ruptura de hielo (cuando está frente a un bloque)
    private int contadorRuptura = 0;
    // Menor = rompe más rápido un bloque
    private int intervaloRuptura = 15;

    @Override
    public void ejecutarComportamiento(GrafoTablero grafo, VistaTablero vista, Helado jugador) throws BadDopoException {
        if (vista == null || grafo == null || jugador == null) return;

        // Calcular dirección considerando hielo (para poder pararse y romper)
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
            // Usar contador de ruptura (más rápido)
            contadorRuptura++;
            if (contadorRuptura < intervaloRuptura) return;
            contadorRuptura = 0;
        } else {
            // Movimiento normal, más lento
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
        // Marcar la celda para que no pueda reconstruirse el hielo
        try {
            celdaARomper.setPermiteReconstruccion(false);
        } catch (Exception e) {
            // No crítico: seguir adelante si no se puede marcar
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

    // El comportamiento delegado a la estrategia (EstrategiaRompeHielo)
}
