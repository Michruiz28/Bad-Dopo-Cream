package domain;

public class Fogata extends Obstaculo {
    private boolean encendida;
    public Fogata(int fila, int col) throws BadDopoException {
        super(fila, col);
        this.encendida = true;
    }
    public void encender() { encendida = true; }
    public void apagar() { encendida = false; }
    public boolean estaEncendida() { return encendida; }
    @Override
    public boolean esSolido() {
        return false; // se puede caminar encima
    }
    @Override
    public boolean esPeligroso() {
        return encendida;
    }
}
