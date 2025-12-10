package domain;

public class Troll extends Enemigo  {
    
    public Troll(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.LINEAL);
        this.ultimaDireccion = "ARRIBA";
        this.persigueJugador = false;
        this.puedeRomperBloques = false;
        this.rompeUnBloquePorVez = false;
    }

    /**
     * El Troll se mueve en línea recta.
     * Si encuentra obstáculo, invierte la dirección.
     */
    @Override
    public String decidirProximaMovida(VistaTablero vista, Helado jugador) throws BadDopoException {
        String dir = getUltimaDireccion();
        if (dir == null) dir = "ARRIBA";

        // Intentar mover en la dirección actual
        if (puedeMoverEn(vista, getFila(), getColumna(), dir)) {
            setUltimaDireccion(dir);
            return dir;
        }

        // Si no puede, invertir dirección
        dir = invertirDireccion(dir);
        if (puedeMoverEn(vista, getFila(), getColumna(), dir)) {
            setUltimaDireccion(dir);
            return dir;
        }

        // Si tampoco puede en la invertida, mantener última dirección válida
        return getUltimaDireccion();
    }

    /**
     * Verifica si el enemigo puede moverse en una dirección usando la vista del tablero.
     */
    private boolean puedeMoverEn(VistaTablero vista, int fila, int columna, String direccion) throws BadDopoException {
        int[] dest = vista.calcularNuevaPosicion(fila, columna, direccion);
        return vista.esPosicionValida(dest[0], dest[1]) && vista.esTransitable(dest[0], dest[1]);
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