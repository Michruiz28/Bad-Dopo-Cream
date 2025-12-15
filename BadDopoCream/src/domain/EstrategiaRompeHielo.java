package domain;

/**
 * Estrategia rompeHielo
 */
public class EstrategiaRompeHielo implements MovimientoEnemigoStrategy {
    @Override
    public void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException {
        String direccion = vista.calcularDireccionHaciaObjetivo(
            enemigo.getFila(), enemigo.getColumna(),
            jugador.getFila(), jugador.getColumna(),
            true
        );
        if (direccion == null) return;

        enemigo.setUltimaDireccion(direccion);

        int f = enemigo.getFila();
        int c = enemigo.getColumna();

        if (!enemigo.isPersigueJugador() && enemigo.canRomperBloques() && (f == jugador.getFila() || c == jugador.getColumna())) {
            int curF = f;
            int curC = c;
            while (true) {
                int[] next = grafo.calcularNuevaPosicion(curF, curC, direccion);
                if (!grafo.esPosicionValida(next[0], next[1])) break;
                Nodo nodoSiguiente = grafo.getNodo(next[0], next[1]);
                if (nodoSiguiente == null) break;
                Celda celdaSiguiente = nodoSiguiente.getCelda();
                if (grafo.esHielo(next[0], next[1])) {
                    grafo.romperHielo(next[0], next[1], direccion, enemigo);
                    if (grafo.solicitarMovimientoHacia(curF, curC, direccion)) {
                        curF = next[0];
                        curC = next[1];
                    } else {
                        break;
                    }
                } else if (celdaSiguiente.esTransitable()) {
                    if (grafo.solicitarMovimientoHacia(curF, curC, direccion)) {
                        curF = next[0];
                        curC = next[1];
                    } else {
                        break;
                    }
                } else {
                    break;
                }
                if (curF == jugador.getFila() && curC == jugador.getColumna()) break;
            }
            return;
        }
        int[] siguiente = grafo.calcularNuevaPosicion(f, c, direccion);
        if (grafo.esPosicionValida(siguiente[0], siguiente[1]) && grafo.esHielo(siguiente[0], siguiente[1])) {
            if (enemigo.rompeUnBloquePorVez()) {
                grafo.romperHielo(siguiente[0], siguiente[1], direccion, enemigo);
                return;
            }
        }
        grafo.solicitarMovimientoHacia(f, c, direccion);
    }
}
