package domain;

/**
 * Clase abstracta que representa a un jugador en Bad Dopo Cream.
 * Un jugador siempre tiene un nombre y un helado asociado.
 * Puede ser controlado por un humano o por una máquina.
 */
public abstract class Jugador {

    protected String nombre;
    protected Helado helado;
    protected int puntaje;

    /**
     * Constructor del jugador
     * @param nombre Nombre del jugador
     * @param helado Helado asociado
     * @throws BadDopoException Si los datos son inválidos
     */
    public Jugador(String nombre, Helado helado) throws BadDopoException {
        if (nombre == null || nombre.isBlank()) {
            throw new BadDopoException("El nombre del jugador no puede estar vacío");
        }
        if (helado == null) {
            throw new BadDopoException("El jugador debe tener un helado asociado");
        }

        this.nombre = nombre;
        this.helado = helado;
        this.puntaje = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public Helado getHelado() {
        return helado;
    }

    public void setHelado(Helado helado) {
        this.helado = helado;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void sumarPuntos(int puntos) {
        this.puntaje += puntos;
    }

    /**
     * Método general que define cómo realiza su movimiento un jugador.
     * En humanos GUI da la dirección.
     * En maquina usa una estrategia.
     */
    public abstract void realizarMovimiento(Nivel nivel) throws BadDopoException;
}
