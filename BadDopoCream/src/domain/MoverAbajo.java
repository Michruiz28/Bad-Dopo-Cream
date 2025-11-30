package Domain;

public class MoverAbajo implements Comando
{
    private Helado helado;
    public MoverAbajo(Helado h) { this.helado = h; }
    public void ejecutar() throws BadDopoException  { helado.moverEnDireccion("ABAJO"); }
}