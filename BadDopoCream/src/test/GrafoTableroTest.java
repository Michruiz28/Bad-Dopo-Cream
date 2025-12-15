package test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import domain.*;

/**
 * Clase de pruebas para GrafoTablero
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 */
public class GrafoTableroTest {

    private GrafoTablero grafo;
    private String[][] infoNivelBasico;

    @Before
    public void setUp() throws BadDopoException {
        infoNivelBasico = new String[][] {
                {"B", "B", "B", "B", "B"},
                {"B", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "B"},
                {"B", "B", "B", "B", "B"}
        };

        grafo = new GrafoTablero(5, 5, infoNivelBasico, null);
    }

    @Test
    public void deberiaObtenerNodoEnPosicionValida() {
        Nodo nodo = grafo.getNodo(2, 2);

        assertNotNull("El nodo no debería ser null", nodo);
        assertEquals("V", nodo.getCelda().getTipo());
    }

    @Test
    public void deberiaRetornarNullParaPosicionFueraDeRango() {
        Nodo nodo = grafo.getNodo(10, 10);

        assertNull("El nodo debería ser null para posición fuera de rango", nodo);
    }

    @Test
    public void deberiaRetornarNullParaPosicionNegativa() {
        Nodo nodo = grafo.getNodo(-1, -1);

        assertNull("El nodo debería ser null para posición negativa", nodo);
    }

    @Test
    public void deberiaActualizarTipoDeNodoExistente() throws BadDopoException {
        grafo.setNodo(2, 2, "V");
        Nodo nodo = grafo.getNodo(2, 2);

        assertEquals("V", nodo.getCelda().getTipo());
    }

    @Test
    public void deberiaActualizarVariosNodos() throws BadDopoException {
        grafo.setNodo(1, 1, "H");
        grafo.setNodo(1, 2, "H");

        assertEquals("H", grafo.getNodo(1, 1).getCelda().getTipo());
        assertEquals("H", grafo.getNodo(1, 2).getCelda().getTipo());
    }

    @Test
    public void noDeberiaFallarAlActualizarNodoNull() throws BadDopoException {
        grafo.setNodo(10, 10, "V");

        assertNull(grafo.getNodo(10, 10));
    }

    @Test
    public void deberiaCalcularPosicionArribaCorrectamente() throws BadDopoException {
        int[] nuevaPos = grafo.moverArriba(2, 2);

        assertEquals(1, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionArribaDesdeOtraCelda() throws BadDopoException {
        int[] nuevaPos = grafo.moverArriba(3, 3);

        assertEquals(2, nuevaPos[0]);
        assertEquals(3, nuevaPos[1]);
    }

    @Test
    public void deberiaPermitirCalculoArribaAunqueSalgaDelTablero() throws BadDopoException {
        int[] nuevaPos = grafo.moverArriba(0, 2);

        assertEquals(-1, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionAbajoCorrectamente() throws BadDopoException {
        int[] nuevaPos = grafo.moverAbajo(2, 2);

        assertEquals(3, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionAbajoDesdeOtraCelda() throws BadDopoException {
        int[] nuevaPos = grafo.moverAbajo(1, 1);

        assertEquals(2, nuevaPos[0]);
        assertEquals(1, nuevaPos[1]);
    }

    @Test
    public void deberiaPermitirCalculoAbajoAunqueSalgaDelTablero() throws BadDopoException {
        int[] nuevaPos = grafo.moverAbajo(4, 2);

        assertEquals(5, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionDerechaCorrectamente() throws BadDopoException {
        int[] nuevaPos = grafo.moverDerecha(2, 2);

        assertEquals(2, nuevaPos[0]);
        assertEquals(3, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionDerechaDesdeOtraCelda() throws BadDopoException {
        int[] nuevaPos = grafo.moverDerecha(1, 1);

        assertEquals(1, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaPermitirCalculoDerechaAunqueSalgaDelTablero() throws BadDopoException {
        int[] nuevaPos = grafo.moverDerecha(2, 4);

        assertEquals(2, nuevaPos[0]);
        assertEquals(5, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionIzquierdaCorrectamente() throws BadDopoException {
        int[] nuevaPos = grafo.moverIzquierda(2, 2);

        assertEquals(2, nuevaPos[0]);
        assertEquals(1, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularPosicionIzquierdaDesdeOtraCelda() throws BadDopoException {
        int[] nuevaPos = grafo.moverIzquierda(3, 3);

        assertEquals(3, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaPermitirCalculoIzquierdaAunqueSalgaDelTablero() throws BadDopoException {
        int[] nuevaPos = grafo.moverIzquierda(2, 0);

        assertEquals(2, nuevaPos[0]);
        assertEquals(-1, nuevaPos[1]);
    }

    @Test
    public void deberiaDeterminarPosicionValidaDentroDelTablero() {
        boolean esValida = grafo.esPosicionValida(2, 2);

        assertEquals(true, esValida);
    }

    @Test
    public void noDeberiaConsiderarValidaPosicionNegativa() {
        boolean esValida = grafo.esPosicionValida(-1, 2);

        assertEquals(false, esValida);
    }

    @Test
    public void noDeberiaConsiderarValidaPosicionFueraDeRango() {
        boolean esValida = grafo.esPosicionValida(10, 10);

        assertEquals(false, esValida);
    }

    @Test
    public void deberiaIdentificarCeldaConHielo() throws BadDopoException {
        grafo.setNodo(2, 2, "H");
        boolean esHielo = grafo.esHielo(2, 2);

        assertEquals(true, esHielo);
    }

    @Test
    public void noDeberiaIdentificarComoHieloCeldaVacia() {
        boolean esHielo = grafo.esHielo(1, 1);

        assertEquals(false, esHielo);
    }

    @Test
    public void noDeberiaIdentificarComoHieloPosicionInvalida() {
        boolean esHielo = grafo.esHielo(-1, -1);

        assertEquals(false, esHielo);
    }


    @Test
    public void deberiaCalcularNuevaPosicionParaArriba() throws BadDopoException {
        int[] nuevaPos = grafo.calcularNuevaPosicion(2, 2, "ARRIBA");

        assertEquals(1, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaCalcularNuevaPosicionParaAbajo() throws BadDopoException {
        int[] nuevaPos = grafo.calcularNuevaPosicion(2, 2, "ABAJO");

        assertEquals(3, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaRetornarPosicionActualParaDireccionNull() throws BadDopoException {
        int[] nuevaPos = grafo.calcularNuevaPosicion(2, 2, null);

        assertEquals(2, nuevaPos[0]);
        assertEquals(2, nuevaPos[1]);
    }

    @Test
    public void deberiaRemoverElementoYDejarCeldaVacia() throws BadDopoException {
        grafo.setNodo(2, 2, "CF");
        grafo.removeElemento(2, 2);

        assertEquals("V", grafo.getNodo(2, 2).getCelda().getTipo());
    }

    @Test
    public void deberiaRemoverVariosElementos() throws BadDopoException {
        grafo.setNodo(1, 1, "BF");
        grafo.setNodo(1, 2, "U");

        grafo.removeElemento(1, 1);
        grafo.removeElemento(1, 2);

        assertEquals("V", grafo.getNodo(1, 1).getCelda().getTipo());
        assertEquals("V", grafo.getNodo(1, 2).getCelda().getTipo());
    }

    @Test
    public void noDeberiaFallarAlRemoverEnPosicionInvalida() {
        grafo.removeElemento(10, 10);

        assertNull(grafo.getNodo(10, 10));
    }


    @Test
    public void deberiaRetornarDireccionValidaParaCeldaCentral() {
        String direccion = grafo.obtenerDireccionAleatoria(2, 2);

        assertNotNull("Debería retornar una dirección", direccion);
    }

    @Test
    public void deberiaRetornarDireccionValidaEnCualquierFormato() {
        String direccion = grafo.obtenerDireccionAleatoria(2, 2);

        boolean esValida = direccion.equals("ARRIBA") ||
                direccion.equals("ABAJO") ||
                direccion.equals("DERECHA") ||
                direccion.equals("IZQUIERDA");
        assertEquals(true, esValida);
    }

    @Test
    public void deberiaRetornarNullSiNoHayDireccionesDisponibles() throws BadDopoException {
        grafo.setNodo(1, 2, "B");
        grafo.setNodo(3, 2, "B");
        grafo.setNodo(2, 1, "B");
        grafo.setNodo(2, 3, "B");

        String direccion = grafo.obtenerDireccionAleatoria(2, 2);

        assertNull("Debería retornar null si no hay direcciones disponibles", direccion);
    }


    @Test
    public void deberiaConstruirRepresentacionConDimensionesCorrectas() {
        String[][] rep = grafo.construirRepresentacionActual();

        assertEquals(5, rep.length);
        assertEquals(5, rep[0].length);
    }

    @Test
    public void deberiaConstruirRepresentacionConTiposCorrectos() {
        String[][] rep = grafo.construirRepresentacionActual();

        assertEquals("B", rep[0][0]);
        assertEquals("V", rep[1][1]);
    }

    @Test
    public void deberiaReflejarCambiosEnRepresentacion() throws BadDopoException {
        grafo.setNodo(2, 2, "H");
        String[][] rep = grafo.construirRepresentacionActual();

        assertEquals("H", rep[2][2]);
    }


    @Test
    public void deberiaAgregarHeladoEnPosicionValida() throws BadDopoException {
        Helado helado = new Helado(2, 2, "Vainilla");
        grafo.agregarHelado(helado);

        Elemento elemento = grafo.getNodo(2, 2).getCelda().getElemento();
        assertNotNull("El helado debería estar en la celda", elemento);
        assertEquals(true, elemento.esHelado());
    }

    @Test(expected = BadDopoException.class)
    public void noDeberiaPermitirAgregarHeladoEnPosicionInvalida() throws BadDopoException {
        Helado helado = new Helado(10, 10, "Fresa");
        grafo.agregarHelado(helado);
    }

    @Test
    public void deberiaAgregarFrutaEnPosicionEspecificada() throws BadDopoException {
        Cereza cereza = new Cereza(2, 2);
        grafo.agregarElementoEnPosicion(cereza, 2, 2);

        Elemento elemento = grafo.getNodo(2, 2).getCelda().getElemento();
        assertNotNull("La cereza debería estar en la celda", elemento);
    }

    @Test
    public void deberiaActualizarTipoDeCeldaAlAgregarFruta() throws BadDopoException {
        Banano banano = new Banano(1, 1);
        grafo.agregarElementoEnPosicion(banano, 1, 1);

        String tipo = grafo.getNodo(1, 1).getCelda().getTipo();
        assertEquals("BF", tipo);
    }

    @Test
    public void deberiaAgregarVariasFrutasEnDiferentesPosiciones() throws BadDopoException {
        Uva uva = new Uva(1, 1);
        Cereza cereza = new Cereza(1, 2);

        grafo.agregarElementoEnPosicion(uva, 1, 1);
        grafo.agregarElementoEnPosicion(cereza, 1, 2);

        assertEquals("U", grafo.getNodo(1, 1).getCelda().getTipo());
        assertEquals("CF", grafo.getNodo(1, 2).getCelda().getTipo());
    }
}