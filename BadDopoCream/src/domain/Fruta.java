package domain;


/**
 * Clase que representa una fruta en el juego Bad Dopo Cream
 * Las frutas deben ser recolectadas por el helado para ganar puntos
 */
public abstract class Fruta extends Elemento{

    protected final int ganancia;
    protected boolean recolectada = false;

    public Fruta(int fila, int columna, int ganancia) throws BadDopoException {
        super(fila,columna);
        if (ganancia <= 0 ){
            throw new BadDopoException(BadDopoException.GANANCIA_INVALIDA);
        }
        this.ganancia = ganancia;
    }

    /**@param fila
     * @param col
     * @throws BadDopoException
     */
    public void actualizar() throws BadDopoException{}

    public int getGanancia() {
        return ganancia;
    }

    /**
     *
     * @param celda
     * @throws BadDopoException
     */
    @Override
    public void mover (String direccion) throws BadDopoException{
        throw new BadDopoException(BadDopoException.FRUTA_NO_SE_MUEVE);
    }
    public boolean estaRecolectada(){
        return recolectada;
    }

    public void recolectar(){
        this.recolectada = true;
    }
}