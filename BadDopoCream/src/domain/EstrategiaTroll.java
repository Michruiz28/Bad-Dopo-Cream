package domain;


public class EstrategiaTroll implements MovimientoEnemigoStrategy {

    /**
     * Ejecuta un turno para el troll: intenta continuar recto, luego probar
     * perpendiculares y, finalmente, invertir la dirección si no hay
     * alternativas.
     *
     * @param enemigo enemigo que ejecuta la estrategia
     * @param vista    no usado por esta estrategia (pero se incluye por la interfaz)
     * @param jugador  referencia al jugador (no usado directamente)
     * @param grafo    grafo del tablero para verificar movimientos y solicitar acciones
     * @throws BadDopoException si ocurre un error al solicitar movimientos
     */
    @Override
    public void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException {
        String dir = enemigo.getUltimaDireccion();
        if (dir == null) dir = "ARRIBA";
        int[] ahead = grafo.calcularNuevaPosicion(enemigo.getFila(), enemigo.getColumna(), dir);
        boolean aheadIsIce = false;
        if (grafo.esPosicionValida(ahead[0], ahead[1])) {
            aheadIsIce = grafo.esHielo(ahead[0], ahead[1]);
        }

        // Si al frente no hay hielo y la casilla es transitable, seguir recto
        if (!aheadIsIce && grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), dir)) {
            enemigo.setUltimaDireccion(dir);
            grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), dir);
            return;
        }
        if ("ARRIBA".equals(dir) || "ABAJO".equals(dir)) {
            if (grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), "IZQUIERDA")) {
                enemigo.setUltimaDireccion("IZQUIERDA");
                grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), "IZQUIERDA");
                return;
            }
            if (grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), "DERECHA")) {
                enemigo.setUltimaDireccion("DERECHA");
                grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), "DERECHA");
                return;
            }
        } else {
            if (grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), "ARRIBA")) {
                enemigo.setUltimaDireccion("ARRIBA");
                grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), "ARRIBA");
                return;
            }
            if (grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), "ABAJO")) {
                enemigo.setUltimaDireccion("ABAJO");
                grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), "ABAJO");
                return;
            }
        }
        String invert = null;
        if ("ARRIBA".equals(dir)) invert = "ABAJO";
        else if ("ABAJO".equals(dir)) invert = "ARRIBA";
        else if ("IZQUIERDA".equals(dir)) invert = "DERECHA";
        else if ("DERECHA".equals(dir)) invert = "IZQUIERDA";

        if (invert != null && grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), invert)) {
            enemigo.setUltimaDireccion(invert);
            grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), invert);
        }
    }
}

