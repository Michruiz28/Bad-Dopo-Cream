package domain;

public class Celda {
    private final int fila;
    private final int col;
    private Elemento elemento;
    private String codigoElemento;//Elementos de la dimension fisica
    private TipoCelda tipoCelda;// Elementos de la dimension terreno
    private final CreadorElemento creador;

    public Celda(int fila, int col, String codigo, CreadorElemento creador) throws BadDopoException {
        this.fila = fila;
        this.col = col;
        this.creador = creador;
        this.codigoElemento = codigo == null ? "V" : codigo;
        this.tipoCelda = mapearCodigoATipoCelda(this.codigoElemento);
        if (this.elemento != null){
            this.elemento.setCelda(this);
        } else {
            this.elemento = null;
        }

    }

    public int getFila() { return fila; }

    public int getCol() { return col; }

    public String getCodigoElemento() { return codigoElemento; }

    public Elemento getElemento() { return elemento; }

    public TipoCelda getTipoCelda(){ return tipoCelda;}

    private TipoCelda mapearCodigoATipoCelda(TipoCelda tipoCelda) {
        return switch (tipoCelda) {
            case "B"  -> TipoCelda.BORDE;
            case "H"  -> TipoCelda.HIELO;
            case "FO" -> TipoCelda.FOGATA;
            case "BO" -> TipoCelda.BALDOSA_CALIENTE;
            default   -> TipoCelda.VACIA;
        };
    }

    private boolean esCeldaEstructural(String codigo) {
        // Borde y otros códigos estructurales no deben crear elementos
        return codigo != null && (codigo.equals("B"));
    }

    /**
     * Coloca un elemento en la celda. Si el elemento es null, quita el elemento actual
     * Ajusta la referencia de la celda del elemento
     */
    public void colocarElemento(Elemento nuevo) throws BadDopoException {
        if (nuevo == null) {
            if (this.elemento != null) {
                this.elemento.setCelda(null);
            }
            this.elemento = null;
            this.codigoElemento = "V";
            return;
        }
        this.elemento = nuevo;
        nuevo.setCelda(this);
        this.codigoElemento = null;
    }

    /**
     * Crea y coloca un elemento a partir del codigo
     */
    public void colocarElementoPorCodigo(String codigo) throws BadDopoException {
        Elemento e = creador.creadorElemento(fila, col, codigo);
        colocarElemento(e);
    }
    public void quitarElemento() {
        if (this.elemento != null) {
            this.elemento.setCelda(null);
            this.elemento = null;
            this.codigoElemento = "V";
        }
    }



    public void setElemento(Elemento elemento, CreadorElemento creador) throws BadDopoException {
        if (tipo == elemento){
            this.elemento = creador.creadorElemento(fila, col, "N");;
        }
        this.elemento = elemento;
    }

    public boolean esTransitable() {
        // 1. BORDE NUNCA ES TRANSITABLE
        if (tipoCelda == TipoCelda.BORDE) return false;

        // 2. SI NO HAY ELEMENTO → SÍ ES TRANSITABLE
        if (elemento == null) return true;

        // 3. SI HAY ELEMENTO, USAMOS SU LÓGICA
        return !elemento.esSolido();
    }
    public boolean esRompible() {
        return tipo == TipoCelda.HIELO;
    }
    public void setTipoCelda(TipoCelda nuevoTipo){
        this.tipoCelda = nuevoTipo;
    }
}
