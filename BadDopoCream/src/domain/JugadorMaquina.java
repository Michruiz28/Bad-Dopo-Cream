package domain;

public class JugadorMaquina extends Helado {

    private String estrategia;

    public JugadorMaquina(int fila, int col, String sabor, String estrategia)
            throws BadDopoException {
        super(fila, col, sabor);

        // Permitir que la máquina se cree sin estrategia explícita.
        // Si no se proporciona, asignamos una estrategia por defecto 'RANDOM'
        // para que no se lance la excepción y el helado pueda existir.
        if (estrategia == null || estrategia.trim().isEmpty()) {
            this.estrategia = "RANDOM";
        } else {
            this.estrategia = estrategia;
        }
    }

    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

}
