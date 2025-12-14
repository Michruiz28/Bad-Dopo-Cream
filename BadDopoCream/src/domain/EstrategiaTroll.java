package domain;

/**
 * Estrategia simple para el Troll:
 * - Se mueve en línea recta según `ultimaDireccion`.
 * - Si al frente hay hielo, cambia a una dirección perpendicular disponible
 *   (vertical -> izquierda/derecha; horizontal -> arriba/abajo).
 * - Si no hay perpendiculares válidas, intenta invertir la dirección.
 */
public class EstrategiaTroll implements MovimientoEnemigoStrategy {

    @Override
    public void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException {
        String dir = enemigo.getUltimaDireccion();
        if (dir == null) dir = "ARRIBA";

        // Calcular posición al frente y comprobar si es hielo
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

        // Si hay hielo al frente (o no se puede avanzar), probar perpendiculares
        if ("ARRIBA".equals(dir) || "ABAJO".equals(dir)) {
            // estaba yendo vertical -> probar izquierda, luego derecha
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
            // estaba yendo horizontal -> probar arriba, luego abajo
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

        // Si no hay perpendiculares válidas, intentar invertir la dirección
        String invert = null;
        if ("ARRIBA".equals(dir)) invert = "ABAJO";
        else if ("ABAJO".equals(dir)) invert = "ARRIBA";
        else if ("IZQUIERDA".equals(dir)) invert = "DERECHA";
        else if ("DERECHA".equals(dir)) invert = "IZQUIERDA";

        if (invert != null && grafo.puedeMoverEn(enemigo.getFila(), enemigo.getColumna(), invert)) {
            enemigo.setUltimaDireccion(invert);
            grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), invert);
        }
        // Si no puede invertir, queda en su lugar este turno.
    }
}

