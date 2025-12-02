package domain;
import java.util.Random;
public class Troll extends Enemigo implements Mover {
    private Random random = new Random();
    
    public Troll(int fila, int columna, Tablero tablero) {
        super(fila, columna, 10, TipoComportamiento.LINEAL, tablero);
        this.ultimaDireccion = "DERECHA";
    }

    @Override
    public void mover(Direccion direccion) throws BadDopoException {
        if (direccion == null) {
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        this.ultimaDireccion = direccion.name();
        moverEnDireccion(this.ultimaDireccion);
    }
    
    @Override
    public void romperHielo(){}
    @Override
    public void realizarMovimiento(Nivel nivel) throws BadDopoException {

        try {
            moverEnDireccion(ultimaDireccion);
        } catch (BadDopoException e) {
            // Cambiar dirección al chocar
            ultimaDireccion = cambiarDireccion(ultimaDireccion);
            moverEnDireccion(ultimaDireccion);
        }
    }
    
    private String cambiarDireccion(String dir) {
        return switch (dir) {
            case "ARRIBA" -> "ABAJO";
            case "ABAJO" -> "ARRIBA";
            case "IZQUIERDA" -> "DERECHA";
            default -> "IZQUIERDA";
        };
    }
 
    
    public int getFila() { return fila; }
    public int getColumna() { return columna; }

}