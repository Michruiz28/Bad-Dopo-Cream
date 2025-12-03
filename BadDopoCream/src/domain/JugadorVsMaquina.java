package domain;

public class JugadorVsMaquina extends CreadorElemento {
    private String perfil;

    public JugadorVsMaquina(String perfil) {
        this.perfil = perfil;
    }

    public Helado crearHelado1(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorHumano(fila, col, sabor);
    }

    public Helado crearHelado2(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorMaquina(fila, col, sabor, perfil);
    }
}
