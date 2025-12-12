package domain;

public class Helado extends Elemento implements Poder {
    private String sabor;
    private int puntaje;
    private int fila;
    private int col;
    private int filaInicial;
    private int columnaInicial;
    private String imagenActual;
    private String ultimaDireccion;
    private static String imagenAbajo;
    private static String imagenDerecha;
    private static String imagenIzquierda;
    private static String imagenArriba;


    public Helado(int fila, int col, String sabor) throws BadDopoException {
        super(fila, col);
        if (fila == 0 || col == 0 || fila == 1 ||col == 1 || fila == 16 ||col == 16 || fila == 17 ||col == 17 ){
            throw new BadDopoException(BadDopoException.POSICION_INVALIDA);
        }
        if (sabor == null || sabor.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.SABOR_INVALIDO);
        }
        this.sabor = sabor;
        this.puntaje = 0;
        this.filaInicial = fila;
        this.columnaInicial = col;
        definirImagenes(sabor);
        definirImagenActual(sabor);
    }

    public void definirImagenes(String sabor) throws BadDopoException {
        if (sabor.equals("VH")) {
            imagenAbajo = "src/presentation/images/VainillaAbajo.png";
            imagenDerecha = "src/presentation/images/VainillaDerecha.png";
            imagenIzquierda = "src/presentation/images/VainillaIzquierda.png";
            imagenArriba = "src/presentation/images/VainillaDetras.png";
        } else if (sabor.equals("CH")) {
            imagenAbajo = "src/presentation/images/ChocolateAbajo.png";
            imagenDerecha = "src/presentation/images/ChocolateDerecha.png";
            imagenIzquierda = "src/presentation/images/ChocolateIzquierda.png";
            imagenArriba = "src/presentation/images/ChocolateDetras.png";
        } else if (sabor.equals("F")){
            imagenAbajo = "src/presentation/images/FresaAbajo.png";
            imagenDerecha = "src/presentation/images/FresaDerecha.png";
            imagenIzquierda = "src/presentation/images/FresaIzquierda.png";
            imagenArriba = "src/presentation/images/FresaDetras.png";
        }
    }

    public void definirImagenActual(String sabor) throws BadDopoException {
        if (sabor.equals("VH")) {
            imagenActual = "src/presentation/images/VainillaAbajo.png";
        } else if (sabor.equals("CH")) {
            imagenActual = "src/presentation/images/ChocolateAbajo.png";
        } else if (sabor.equals("F")){
            imagenActual = "src/presentation/images/FresaAbajo.png";
        }
    }

    public void setPosicionInicial() throws BadDopoException {
        this.fila = filaInicial;
        this.col = columnaInicial;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.col = columna;
    }
    public String getUltimaDireccion() {
        return ultimaDireccion;
    }

    @Override
    public void mover(String direccion) throws BadDopoException {
        if (direccion == null ){
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        this.ultimaDireccion = direccion;
        actualizarImagen(ultimaDireccion);
    }

    @Override
    public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {
        celdaARomper.setElementoConTipo("V", creador);
    }


    @Override
    public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {
        celdaACrear.setElementoConTipo("H", creador);
    }

    @Override
    public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
        return new int[0];
    }

    @Override
    public boolean esTransitable() {
        return false;
    }


    public void aumentarPuntaje(int puntaje) {
        this.puntaje += puntaje;
    }
    
    public int getGanancia(){
        return puntaje;
    }

    @Override
    public void actualizarImagen(String ultimaDireccion) {
        if (ultimaDireccion.equals("DERECHA")) {
            imagenActual = imagenDerecha;
        } else if (ultimaDireccion.equals("IZQUIERDA")) {
            imagenActual = imagenIzquierda;
        } else if (ultimaDireccion.equals("ABAJO")) {
            imagenActual = imagenAbajo;
        }  else if (ultimaDireccion.equals("ARRIBA")) {
            imagenActual = imagenArriba;
        }
    }

    public String getSabor() {
        return sabor;
    }
    
    public void setSabor(String sabor) throws BadDopoException {
        if (sabor == null || sabor.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.SABOR_INVALIDO);
        }
        this.sabor = sabor;
    }
    
    public int getPuntaje() {
        return puntaje;
    }
    
    public void setPuntaje(int puntaje) throws BadDopoException {
        if (puntaje < 0) {
            throw new BadDopoException(BadDopoException.PUNTAJE_INVALIDO);
        }
        this.puntaje = puntaje;
    }
}