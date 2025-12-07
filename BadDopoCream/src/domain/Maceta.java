 
package domain;
import java.util.Random;
import java.util.List;
import java.util.Random;

public class Maceta extends Enemigo  {
    private Random random = new Random();
    public Maceta(int fila, int col) {
        super(fila, col, 1, TipoComportamiento.PERSEGUIDOR);
    }

    @Override
    public String decidirMovimiento(Tablero tablero, Helado jugador) throws BadDopoException {
        Nodo nodoActual = tablero.getNodo(getFila(), getColumna());
        if (nodoActual == null) return ultimaDireccion;
        // Todas las direcciones posibles
        List<String> direcciones = new ArrayList<>(List.of("ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"));
        // Probamos direcciones hasta encontrar una válida
        while (!direcciones.isEmpty()) {
            int index = random.nextInt(direcciones.size());
            String direccion = direcciones.get(index);
            // Coordenadas candidatas
            int nf = getFila();
            int nc = getColumna();
            switch (direccion) {
                case "ARRIBA"    -> nf--;
                case "ABAJO"     -> nf++;
                case "IZQUIERDA" -> nc--;
                case "DERECHA"   -> nc++;
            }
            Nodo nodoDestino = tablero.getNodo(nf, nc);
            // Caso válido: el nodo existe y es transitable
            if (nodoDestino != null && nodoDestino.getCelda().esTransitable()) {
                ultimaDireccion = direccion;
                return direccion;
            }
            // Si no sirve, la eliminamos y probamos otra
            direcciones.remove(index);
        }
        // Si ninguna dirección fue válida → no se mueve
        return ultimaDireccion;
    }

}
