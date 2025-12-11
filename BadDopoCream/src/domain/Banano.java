package domain;
/**
 * Clase que representa un Banano en el juego Bad Dopo Cream
 * El banano es una fruta estática que permanece en su posición hasta ser recolectada
 */
public class Banano extends FrutaEstatica {
    public static final int GANANCIA_BANANO = 100;
    private static final String imagen = "src/presentation/images/Banano.png";
    public Banano (int fila, int columna) throws BadDopoException {
        super(fila,columna,GANANCIA_BANANO);
    }

    // Constructor auxiliar usado en tests o creación desde Celda
    public Banano(int fila, int columna, int ganancia, Celda celda) throws BadDopoException {
        super(fila, columna, ganancia);
        if (celda != null) {
            setCelda(celda);
        }
    }
    
    /** Devuelve la ruta de la imagen asociada al Banano */
    public static String getImagen() {
        return imagen;
    }
}
