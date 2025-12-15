package domain;

/**
 * Patrón de diseño de comportamiento CreadorElemento
 */
public class CreadorElemento {
    /**
     * Crea un elemento del tablero a partir de su código tipo.
     *
     * @param fila  fila donde se ubicará el elemento
     * @param col   columna donde se ubicará el elemento
     * @param tipo  código que identifica el tipo de elemento (p. ej. "T", "CF", "P", "H", "V", ...)
     * @return la instancia de {@link Elemento} correspondiente al tipo
     * @throws BadDopoException si el tipo es null o no está reconocido
     */
    public static Elemento creadorElemento(int fila, int col, String tipo) throws BadDopoException{
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

    private static Nieve crearNieve(int fila, int col) throws BadDopoException {
        return new Nieve(fila, col);
    }

    /**
     * Fabrica un enemigo concreto según el código de tipo.
     *
     * @param fila  fila inicial del enemigo
     * @param col   columna inicial del enemigo
     * @param tipo  código del enemigo ("T"=Troll, "C"=Calamar, "M"=Maceta)
     * @return la instancia de {@link Enemigo} correspondiente, o null si el tipo no coincide
     */
    public static Enemigo crearEnemigo(int fila, int col, String tipo) {
        if (tipo.equals("T")) {
            return new Troll(fila, col);
        } else if (tipo.equals("C")){
            return new Calamar(fila, col);
        } else if (tipo.equals("M")){
            return new Maceta(fila, col);
        }
        return null;
    }

    /**
     * Crea un obstáculo a partir de su código.
     *
     * @param fila fila donde se ubicará el obstáculo
     * @param col  columna donde se ubicará el obstáculo
     * @param tipo código del obstáculo ("H"=Hielo, "BO"=BaldosaCaliente, "FO"=Fogata, "B"=Borde)
     * @return la instancia de {@link Obstaculo}
     * @throws BadDopoException si la creación del obstáculo falla por parámetros inválidos
     */
    public static Obstaculo crearObstaculo(int fila, int col, String tipo) throws BadDopoException {
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

    /**
     * Crea un helado (jugador) a partir de su código de sabor.
     *
     * @param fila   fila inicial del helado
     * @param col    columna inicial del helado
     * @param sabor  código del sabor ("CH"=Chocolate, "F"=Fresa, "VH"/null=Vainilla)
     * @return la instancia de {@link Helado}
     * @throws BadDopoException si los parámetros son inválidos
     */
    public static Helado crearHelado(int fila, int col, String sabor) throws BadDopoException {
        String saborReal = mapearCodigoASabor(sabor);
        return new Helado(fila, col, saborReal);
    }

    /**
     * Mapea el código de sabor a la cadena usada por el constructor de {@link Helado}.
     *
     * @param codigo código del sabor
     * @return nombre del sabor (por defecto "Vainilla")
     */
    protected static String mapearCodigoASabor(String codigo) {
        if (codigo == null) return "Vainilla";
        if (codigo.equals("CH")) return "Chocolate";
        if (codigo.equals("F")) return "Fresa";
        if (codigo.equals("VH")) return "Vainilla";
        return "Vainilla";
    }

    /**
     * Fabrica una fruta concreta según el código pasado.
     *
     * @param fila fila donde se ubicará la fruta
     * @param col  columna donde se ubicará la fruta
     * @param tipo código de fruta ("BF"=Banano, "CF"=Cereza, "P"=Pina, "CAF"=Cactus, "U"=Uva)
     * @return la instancia de {@link Fruta} correspondiente, o null si el tipo no coincide
     * @throws BadDopoException si la creación falla por parámetros inválidos
     */
    public static Fruta crearFruta(int fila, int col, String tipo) throws BadDopoException {
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
}
