package Domain;

public class MoverIzquierda implements Comando
{
    private Helado helado;
    public MoverIzquierda(Helado h) { this.helado = h; }
    public void ejecutar() throws BadDopoException { helado.moverEnDireccion("IZQUIERDA"); }
}