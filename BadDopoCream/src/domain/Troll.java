package domain;
public class Troll extends Enemigo implements Mover {
	
    public Troll(int fila, int columna, Tablero tablero) {
        super(fila, columna, 10, TipoComportamiento.PATRULLERO, tablero);
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        super.mover(direccion);
    }
    
    @Override
    public void romperHielo(){
        //revisar si este Enemigo puede romper los bloques porque no recordamos
    }
    
    public int getFila() { return fila; }
    public int getColumna() { return columna; }

}