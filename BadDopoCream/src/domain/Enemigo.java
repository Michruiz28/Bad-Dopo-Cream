package domain;
import java.util.ArrayList;

public abstract class Enemigo implements Elemento{

    protected int velocidad;
    protected String ultimaDireccion;
    protected TipoComportamiento comportamiento;



    public Enemigo(int fila, int col, int velocidad, TipoComportamiento comportamiento) {
        super(fila,col);
        this.velocidad = velocidad;
        this.comportamiento = comportamiento;
        this.ultimaDireccion = "ARRIBA";
    }
    public abstract String decidirMovimiento(Tablero tablero, Helado jugador) throws BadDopoException;

    @Override
    public boolean esSolido(){
        return true;
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        if (direccion == null || direccion.isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        this.ultimaDireccion = direccion;

        switch (direccion.toUpperCase()) {
            case "ARRIBA"    -> setFila(getFila() - velocidad);
            case "ABAJO"     -> setFila(getFila() + velocidad);
            case "IZQUIERDA" -> setColumna(getColumna() - velocidad);
            case "DERECHA"   -> setColumna(getColumna() + velocidad);
            default -> throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }
    }
    /**
     * BFS interno para encontrar la mejor dirección hacia el objetivo.
     *
     * NO mueve al enemigo.
     * SOLO calcula la DIRECCIÓN recomendada.
     */
    protected String calcularDireccionHacia(Tablero tablero, Helado objetivo) {
        Nodo inicio = tablero.getNodo(getFila(), getColumna());
        Nodo fin    = tablero.getNodo(objetivo.getFila(), objetivo.getColumna());
        if (inicio == null || fin == null) return null;
        Queue<Nodo> cola = new LinkedList<>();
        Map<Nodo, Nodo> padre = new HashMap<>();
        cola.add(inicio);
        padre.put(inicio, null);
        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            if (actual == fin)
                break;
            for (Nodo vecino : actual.getVecinos()) {
                boolean transitable = vecino.getCelda().esTransitable();

                if (!padre.containsKey(vecino) && transitable) {
                    padre.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }
        if (!padre.containsKey(fin)) {
            return null; // no hay ruta posible
        }
        // Retroceder desde el objetivo hasta encontrar el primer paso
        Nodo paso = fin;
        while (padre.get(paso) != inicio) {
            paso = padre.get(paso);
        }
        return determinarDireccion(inicio, paso);
    }
    /**
     * Dado el nodo origen "inicio" y el primer paso "paso", se determina la dirección.
     */
    private String determinarDireccion(Nodo a, Nodo b) {
        int fa = a.getCelda().getFila();
        int ca = a.getCelda().getCol();
        int fb = b.getCelda().getFila();
        int cb = b.getCelda().getCol();
        if (fb == fa - 1) return "ARRIBA";
        if (fb == fa + 1) return "ABAJO";
        if (cb == ca - 1) return "IZQUIERDA";
        if (cb == ca + 1) return "DERECHA";
        return null;
    }
}
