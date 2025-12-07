package domain;
public class BaldosaCaliente extends Obstaculo {
    public BaldosaCaliente(int fila, int col) throws BadDopoException {
        super(fila, col);
    }
    @Override
    public boolean esSolido() {
        return false; // se puede caminar encima
    }
    @Override
    public boolean esPeligroso() {
        return true; // daña al helado
    }
}
