package domain;
//Hacer estática
//métodos estáticos
//Creador.método
public abstract class CreadorElemento {

    public Elemento creadorElemento(int fila, int col, String tipo) throws BadDopoException{
            if (tipo == null) throw new BadDopoException(BadDopoException.TIPO_NO_IDENTIFICADO);
            if (tipo.equals("T") || tipo.equals("NE") || tipo.equals("C") || tipo.equals("M")) {
                return crearEnemigo(fila, col, tipo);
            } else if (tipo.equals("H") || tipo.equals("BO") || tipo.equals("FO") || tipo.equals("B")) {
                return crearObstaculo(fila, col, tipo);
            } else if (tipo.equals("BF") || tipo.equals("CF") || tipo.equals("P") || tipo.equals("CAF") || tipo.equals("U")) {
                return crearFruta(fila, col, tipo);
            } else if (tipo.equals("CH") || tipo.equals("F") || tipo.equals("VH")){
                return crearHelado(fila, col, tipo);
            } else if (tipo.equals("V")){
                return crearNieve(fila, col);
            } else if(tipo.equals(null)){
                return crearNieve(fila, col);
            }else {
                throw new BadDopoException(BadDopoException.TIPO_NO_IDENTIFICADO);
            }
    }

    private Nieve crearNieve(int fila, int col) throws BadDopoException {
        return new Nieve(fila, col);
    }

    public Enemigo crearEnemigo(int fila, int col, String tipo) {
        if (tipo.equals("T")) {
            return new Troll(fila, col);
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
        } else if (tipo.equals("BO0") || tipo.equals("B")) {
            return new Borde(fila, col);
        } else {
            return null;
        }
    }

    public Helado crearHelado(int fila, int col, String sabor) throws BadDopoException {
        String saborReal = mapearCodigoASabor(sabor);
        return new Helado(fila, col, saborReal);
    }
    protected String mapearCodigoASabor(String codigo) {
        if (codigo == null) return "Vainilla";
        if (codigo.equals("CH")) return "Chocolate";
        if (codigo.equals("F")) return "Fresa";
        if (codigo.equals("VH")) return "Vainilla";
        return "Vainilla";
    }

    public Fruta crearFruta(int fila, int col, String tipo) throws BadDopoException {
        if (tipo.equals("BF")) {
            return new Banano(fila, col);
        } else if (tipo.equals("CF")) {
            return new Cereza(fila, col);
        } else if (tipo.equals("P")) {
            return new Pina(fila, col);
        } else if (tipo.equals("CAF")) {
            return new Cactus(fila, col);
        } else if (tipo.equals("U")) {
            return new Uva(fila, col);
        }
        return null;
    }

    public abstract Helado crearHelado2(int fila, int col, String sabor) throws BadDopoException;
}
