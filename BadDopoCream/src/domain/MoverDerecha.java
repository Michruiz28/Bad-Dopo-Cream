package Domain;

class MoverDerecha implements Comando {
    private Helado helado;
    public MoverDerecha(Helado h) { this.helado = h; }
    public void ejecutar() throws BadDopoException  { helado.moverEnDireccion("DERECHA"); }
}
