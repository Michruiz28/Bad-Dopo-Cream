package domain;

public abstract class CreadorElemento {

    public Elemento creadorElemento(int fila, int col, String tipo) throws BadDopoException{
            if (tipo.equals("T") || tipo.equals("NE") || tipo.equals("C") || tipo.equals("M") ||) {
                return crearEnemigo(fila, col, tipo);
            } else if (tipo.equals("H") || tipo.equals("BO") || tipo.equals("FO") || tipo.equals("B")) {
                return crearObstaculo(fila, col, tipo);
            } else if (tipo.equals("BF") || tipo.equals("CF") || tipo.equals("P") || tipo.equals("CAF") || tipo.equals("U")) {
                return crearFruta(fila, col, tipo);
            } else if (tipo.equals("CH") || tipo.equals("F") || tipo.equals("VH")){
                return crearHelado(fila, col, tipo);
            } else if (tipo.equals("V")){
                return creadorNieve(fila, col);
            } else {
                throw new BadDopoException(BadDopoException.TIPO_NO_IDENTIFICADO);
            }
    }

    private Nieve creadorNieve(int fila, int col) throws BadDopoException {
        return new Nieve(fila, col);
    }

    public Enemigo crearEnemigo(int fila, int col, String tipo) {
        if (tipo.equals("T")) {
            return new Troll(fila, col);
        } else if (tipo.equals("NE")){
            return new Narval(fila, col);
        } else if (tipo.equals("C")){
            return new Calamar(fila, col);
        } else if (tipo.equals("M")){
            return new Maceta(fila, col);
        }
        return null;
    }

    public Obstaculo crearObstaculo(int fila, int col, String tipo) throws BadDopoException {
        if (tipo.equals("H")) {
            return new Hielo(fila, col);
        } else if (tipo.equals("BO")) {
            return new BaldosaCaliente(fila, col);
        } else if (tipo.equals("FO")) {
            return new Fogata(fila, col);
        } else {
            return null;
        }
    }

    public Helado crearHelado(int fila, int col, String sabor) throws BadDopoException {
        return new Helado(fila, col, sabor);
    }

    public Fruta crearFruta(int fila, int col, String tipo) throws BadDopoException {
        if (tipo.equals("BF")) {
            return new Platano(fila, col);
        } else if (tipo.equals("CF")) {
            return new Cereza(fila, col);
        } else if (tipo.equals("P")) {
            return new Piña(fila, col);
        } else if (tipo.equals("CF")) {
            return new Cactus(fila, col);
        }
        return null;
    }
}
