package domain;

/**
 * Clase abstracta que representa a un jugador en Bad Dopo Cream.
 * Un jugador siempre tiene un nombre y un helado asociado.
 * Puede ser controlado por un humano o por una máquina.
 */
public abstract class Jugador {

    protected int fila;
    protected int col;
    protected String sabor;
    protected int puntaje;

    /**
     * Constructor del jugador
     * @param fila número fila
     * @param col número columna
     * @param sabor Helado asociado
     * @throws BadDopoException Si los datos son inválidos
     */
    public Jugador(int fila, int col, String sabor) throws BadDopoException {
        if (fila == 0 || fila == 1 || fila == 16 || fila == 17 || col == 0 || col == 1 || col == 16 || col == 17) {
            throw new BadDopoException("El jugador debe tener un posición válida");
        }

        this.fila = fila;
        this.col = col;
        this.sabor = sabor;
        this.puntaje = 0;
    }

    public int getFila() {
        return fila;
    }

    public int getCol() {
        return col;
    }

    public String getSabor() {
        return sabor;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }
    public void setFila(int fila) {
        this.fila = fila;
    }
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
    public int getPuntaje() {
        return puntaje;
    }

    public void sumarPuntos(int puntos) {
        this.puntaje += puntos;
    }

    /**
     * Método general que define cómo realiza su movimiento un jugador.
     * En humanos GUI da la dirección.
     * En maquina usa una estrategia.
     */
    public abstract void realizarMovimiento(Nivel nivel) throws BadDopoException;
}
