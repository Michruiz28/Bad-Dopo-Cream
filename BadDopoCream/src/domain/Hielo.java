package domain;

public class Hielo extends Obstaculo {
    public Hielo(int fila, int col) throws BadDopoException {
        super(fila, col);
    }
    @Override
    public boolean esRompible() {
        return true;
    }
}
