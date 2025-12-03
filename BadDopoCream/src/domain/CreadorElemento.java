package domain;

public abstract class CreadorElemento {
    public abstract Helado crearHelado1(int fila, int col, String sabor) throws BadDopoException;
    public abstract Helado crearHelado2(int fila, int col, String sabor) throws BadDopoException;

    public Enemigo crearEnemigo(int fila, int col, String tipo) {
        if (tipo.equals("Troll")) {
            return new Troll(fila, col);
        } else if (tipo.equals("Narval")){
            return new Narval(fila, col);
        } else if (tipo.equals("Calamar")){
            return new Calamar(fila, col);
        } else if (tipo.equals("Maceta")){
            return new Maceta(fila, col);
        }
        return null;
    }
}
