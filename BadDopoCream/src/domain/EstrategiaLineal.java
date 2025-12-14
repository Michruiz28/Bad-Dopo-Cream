package domain;

public class EstrategiaLineal implements MovimientoEnemigoStrategy {
    @Override
    public void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException {
        String dir = enemigo.getUltimaDireccion();
        if (dir == null) dir = "ARRIBA";

        // Intentar mover en la dirección actual
        try {
            int[] dest = vista.calcularNuevaPosicion(enemigo.getFila(), enemigo.getColumna(), dir);
            if (vista.esPosicionValida(dest[0], dest[1]) && vista.esTransitable(dest[0], dest[1])) {
                enemigo.setUltimaDireccion(dir);
                grafo.solicitarMovimiento(enemigo.getFila(), enemigo.getColumna(), dir);
                return;
            }
        } catch (BadDopoException ex) {
            // ignorar y probar invertir
        }

        // Invertir dirección
        String invertida = invertirDireccion(dir);
        try {
            int[] dest2 = vista.calcularNuevaPosicion(enemigo.getFila(), enemigo.getColumna(), invertida);
            if (vista.esPosicionValida(dest2[0], dest2[1]) && vista.esTransitable(dest2[0], dest2[1])) {
                enemigo.setUltimaDireccion(invertida);
                grafo.solicitarMovimiento(enemigo.getFila(), enemigo.getColumna(), invertida);
                return;
            }
        } catch (BadDopoException ex) {
        }

        // No puede moverse
    }

    private String invertirDireccion(String dir) {
        return switch (dir) {
            case "ARRIBA" -> "ABAJO";
            case "ABAJO" -> "ARRIBA";
            case "IZQUIERDA" -> "DERECHA";
            case "DERECHA" -> "IZQUIERDA";
            default -> dir;
        };
    }
}
