package domain;

import java.util.ArrayList;

public class Helado extends Elemento implements Poder, RompeHielo {
    private String sabor;
    private int puntaje;
    private int fila;
    private int col;
    private int filaInicial;
    private int columnaInicial;
    private String ultimaDireccion;


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

    @Override
    public void mover(String direccion) throws BadDopoException {
        if (direccion == null ){
            throw new BadDopoException(BadDopoException.DIRECCION_INVALIDA);
        }

        this.ultimaDireccion = direccion;
        // Pendiente imagen
    }

    @Override
    public void romperHielo() throws BadDopoException {
        if (tablero == null || grafo == null) {
            throw new BadDopoException(BadDopoException.CONFIGURACION_INCOMPLETA);
        }
        int fila = posicion.get(0);
        int col = posicion.get(1);

        boolean seRompioAlgo = false;
        
        try {
            // Arriba
            if (tablero.romperHielo(fila - 1, col)) seRompioAlgo = true;
            // Abajo
            if (tablero.romperHielo(fila + 1, col)) seRompioAlgo = true;
            // Izquierda
            if (tablero.romperHielo(fila, col - 1)) seRompioAlgo = true;
            // Derecha
            if (tablero.romperHielo(fila, col + 1)) seRompioAlgo = true;
            
            // Si se rompió algo, reconstruir el grafo
            if (seRompioAlgo) {
                grafo.reconstruir();
            } else {
                throw new BadDopoException(BadDopoException.NO_HAY_HIELO_PARA_ROMPER);
            }
        } catch (BadDopoException e) {
            throw e;
        } catch (Exception e) {
            throw new BadDopoException(BadDopoException.ERROR_AL_ROMPER_HIELO);
        }
    }
    
    // Implementación de Poder
    @Override
    public void poder() throws BadDopoException {
        // El poder del helado es crear hielo
        crearHielo();
    }
    
    public void crearHielo() throws BadDopoException {
        if (tablero == null || grafo == null) {
            throw new BadDopoException(BadDopoException.CONFIGURACION_INCOMPLETA);
        }
        
        int fila = posicion.get(0);
        int col = posicion.get(1);
        
        Celda celdaActual = tablero.getCelda(fila, col);
        if (celdaActual == null) {
            throw new BadDopoException(BadDopoException.CELDA_INVALIDA);
        }
        
        if (celdaActual.getTipo() != TipoCelda.VACIA) {
            throw new BadDopoException(BadDopoException.NO_SE_PUEDE_CREAR_HIELO);
        }
        tablero.setCelda(fila, col, TipoCelda.HIELO);
        grafo.reconstruir(); 
    }
    
    public void cambiarSabor(String nuevoSabor) throws BadDopoException {
        if (nuevoSabor == null || nuevoSabor.trim().isEmpty()) {
            throw new BadDopoException(BadDopoException.SABOR_INVALIDO);
        }
        this.sabor = nuevoSabor;
    }
    
    public void comerFruta(Fruta fruta) throws BadDopoException {
        if (fruta == null) {
            throw new BadDopoException(BadDopoException.FRUTA_NULA);
        }
        if (fruta.getPosicion().get(0).equals(posicion.get(0)) && 
            fruta.getPosicion().get(1).equals(posicion.get(1))) {
            puntaje += fruta.getGANANCIA();
        } else {
            throw new BadDopoException(BadDopoException.FRUTA_FUERA_DE_ALCANCE);
        }
    }
    
//    public void tomarGanancia(Ganancia ganancia) throws BadDopoException {
//        if (ganancia == null) {
//            throw new BadDopoException(BadDopoException.GANANCIA_NULA);
//        }
//        if (ganancia.getPosicion().get(0).equals(posicion.get(0)) &&
//            ganancia.getPosicion().get(1).equals(posicion.get(1))) {
//            puntaje += ganancia.getValor();
//            ganancia.setRecolectada(true);
//        } else {
//            throw new BadDopoException(BadDopoException.GANANCIA_FUERA_DE_ALCANCE);
//        }
//    }
    
    public void escogerNivel(int nivel) throws BadDopoException {
        if (nivel < 1) {
            throw new BadDopoException(BadDopoException.NIVEL_INVALIDO);
        }
        this.puntaje = 0;
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
    
    public ArrayList<Integer> getPosicion() {
        return posicion;
    }
    
    public void setPosicion(int fila, int col) throws BadDopoException {
        if (tablero == null) {
            throw new BadDopoException(BadDopoException.TABLERO_NO_CONFIGURADO);
        }
        
        if (fila < 0 || fila >= tablero.getFilas() || col < 0 || col >= tablero.getColumnas()) {
            throw new BadDopoException(BadDopoException.POSICION_FUERA_DE_RANGO);
        }
        
        this.posicion.set(0, fila);
        this.posicion.set(1, col);
    }
    
    public int getFila() {
        return posicion.get(0);
    }
    
    public int getColumna() {
        return posicion.get(1);
    }
    
    public Tablero getTablero() {
        return tablero;
    }
    
    public GrafoTablero getGrafo() {
        return grafo;
    }

}