package domain;
import java.util.ArrayList;

public abstract class Enemigo implements Mover, RompeHielo{

    protected int fila;
    protected int columna;
    protected int velocidad;
    protected Direccion direccionActual;
    protected TipoComportamiento comportamiento;
    private ArrayList<Integer> posicion;
    private GrafoTablero grafo;
    protected String ultimaDireccion; 


    public Enemigo(int fila, int columna, int velocidad, TipoComportamiento comportamiento) {
        this.fila = fila;
        this.columna = columna;
        this.velocidad = velocidad;
        this.comportamiento = comportamiento;
        this.direccionActual = Direccion.ABAJO;
        this.ultimaDireccion = "ABAJO";
        this.posicion = new ArrayList<>();
        posicion.add(fila);
        posicion.add(columna);
    }
    
    @Override
    public void mover(Direccion direccion) throws BadDopoException {

        if (direccion == null) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        this.ultimaDireccion = direccion.name();
        moverEnDireccion(this.ultimaDireccion);
    }
    
    /**
     * Metodo moverEnDireccion  que permite mover el enemigo
     * en las cuatro direcciones, este metodo usa los metodos privados moverArriba,Abajo etc
     * @param direccion La direccion en la que se desea mover el enemigo(ARRIBA, ABAJO, DERECHA, IZQUIERDA)
     * @throws BadDopoException Si la configuracion es incompleta, la direccion es invalida o desconocida
     */
    public void moverEnDireccion(String direccion) throws BadDopoException {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }
        switch (direccion.toUpperCase()) {
            case "ARRIBA":
            case "UP":
                moverArriba();
                break;
            case "ABAJO":
            case "DOWN":
                moverAbajo();
                break;
            case "DERECHA":
            case "RIGHT":
                moverDerecha();
                break;
            case "IZQUIERDA":
            case "LEFT":
                moverIzquierda();
                break;
            default:
                throw new BadDopoException(BadDopoException.DIRECCION_DESCONOCIDA);
        }
    }
    
    private void moverArriba() throws BadDopoException {
        int filaNueva = posicion.get(0) - 1;
        int col = posicion.get(1);

        if (!validarMovimiento(filaNueva, col)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        posicion.set(0, filaNueva);
        this.fila = filaNueva;
    }
    
    private void moverAbajo() throws BadDopoException {
        int filaNueva = posicion.get(0) + 1;
        int col = posicion.get(1);

        if (!validarMovimiento(filaNueva, col)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        posicion.set(0, filaNueva);
        this.fila = filaNueva;
    }
    
    private void moverDerecha() throws BadDopoException {
        int fila = posicion.get(0);
        int colNueva = posicion.get(1) + 1;

        if (!validarMovimiento(fila, colNueva)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        posicion.set(1, colNueva);
        this.columna = colNueva;
    }
    
    private void moverIzquierda() throws BadDopoException {
        int fila = posicion.get(0);
        int colNueva = posicion.get(1) - 1;

        if (!validarMovimiento(fila, colNueva)) {
            throw new BadDopoException(BadDopoException.MOVIMIENTO_INVALIDO);
        }
        posicion.set(1, colNueva);
        this.columna = colNueva;
    }

    /**
     * Método que el enemigo ejecuta automáticamente en cada turno.
     * Debe ser implementado por Troll, Narval, Calamar, etc.
     */
    public abstract void realizarMovimiento(Nivel nivel) throws BadDopoException;
    
    @Override
    public abstract void romperHielo();

    public int getFila() { return fila; }
    public int getColumna() { return columna; }

}
