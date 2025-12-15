package domain;

import java.util.ArrayList;
import java.util.Random;

public class Cereza extends FrutaEnMovimiento {
    public static final int GANANCIA_CEREZA = 150;
    private long ultimoTeletransporte;
    private Random random = new Random();
    private int fila;
    private int columna;
    private static final String imagen = "src/presentation/images/Cereza.png";
    private static final String codigo = "CF";

    public Cereza(int fila, int col) throws BadDopoException {
        super(fila, col);
        this.ultimoTeletransporte = System.currentTimeMillis();
    }

    @Override
    public String getCodigo(){
        return codigo;
    }

    @Override
    public void actualizar(long timpoActual) throws BadDopoException {
    }

    @Override
    public void aumentarPuntaje(int puntaje) {
    }

    @Override
    public int getGanancia() {
        return GANANCIA_CEREZA;
    }

    @Override
    public void actualizarImagen(String ultimaDireccion) {
    }

    @Override
    public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {
    }

    @Override
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {
    }

    public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperiorFilas, int limiteSuperiorColumnas) {
        int[] nuevaPosicion = new int[2];
        Random random = new Random();
        
        int nuevaFila = random.nextInt(limiteSuperiorFilas - limiteInferior + 1) + limiteInferior;
        int nuevaColumna = random.nextInt(limiteSuperiorColumnas - limiteInferior + 1) + limiteInferior;
        
        nuevaPosicion[0] = nuevaFila;
        nuevaPosicion[1] = nuevaColumna;
        
        return nuevaPosicion;
    }

    public int[] calcularPosicionAleatoria(ArrayList<int[]> posicionesDisponibles) {
        if (posicionesDisponibles == null || posicionesDisponibles.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        int indiceAleatorio = random.nextInt(posicionesDisponibles.size());
        int[] posicionElegida = posicionesDisponibles.get(indiceAleatorio);

        this.fila = posicionElegida[0];
        this.columna = posicionElegida[1];
        
        System.out.println("Nueva posición calculada: (" + fila + "," + columna + ")");
        
        return posicionElegida;
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
    }

    @Override
    public void moverConPosicion(int filaNueva, int columnaNueva){
        this.fila = filaNueva;
        this.columna = columnaNueva;
    }

    @Override
    public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}