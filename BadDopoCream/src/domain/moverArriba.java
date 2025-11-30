package Domain;

class MoverArriba implements Comando
{
    private Helado helado;
    public MoverArriba(Helado h) { this.helado = h; }
    public void ejecutar() throws BadDopoException  { helado.moverEnDireccion("ARRIBA"); }
}