package domain;

public class MaquinaVsMaquina {
    private String perfil1;
    private String perfil2;


    public MaquinaVsMaquina(String perfil1, String perfil2) {
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
    }

    public Helado crearHelado1(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorMaquina(fila, col, sabor, perfil1);
    }

    public Helado crearHelado2(int fila, int col, String sabor) throws BadDopoException {
        return new JugadorMaquina(fila, col, sabor, perfil2);
    }
}
