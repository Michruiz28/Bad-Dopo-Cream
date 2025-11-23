package domain;

/**
 * Clase que representa al jugador en el juego Bad Dopo Cream
 * El jugador tiene composición con Helado
 */
public class Jugador {

    private String nombre;
    private Helado helado; 
    public Jugador(){
        this.helado = null ;
    }
    
    /**
     * Permite al jugador escoger el sabor de su helado
     * @param sabor Sabor del helado (Vainilla, Chocolate, Fresa, etc.)
     * @throws BadDopoException Si el sabor es inválido
     */
    public void escogerSabor(String sabor) throws BadDopoException {
        this.helado = new Helado(sabor);
    }
    public Helado getHelado() {
        return helado;
    }
    
    public void setHelado(Helado helado) {
        this.helado = helado;
    }
}
