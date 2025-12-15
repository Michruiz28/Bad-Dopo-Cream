package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import domain.*;

class NodoTest {
    private Nodo nodo;

    @BeforeEach
    void setUp() throws BadDopoException {
        nodo = new Nodo(1, 2, "V", new CreadorElemento());
    }

    @Test
    void deberiaCrearNodoConFilaYColumnaCorrectas() throws BadDopoException {
        Nodo nuevoNodo = new Nodo(3, 4, "V", new CreadorElemento());

        assertEquals(3, nuevoNodo.getFila());
        assertEquals(4, nuevoNodo.getColumna());
    }

    @Test
    void deberiaCrearNodoConTipoNieve() throws BadDopoException {
        Nodo nodoNieve = new Nodo(0, 0, "V", new CreadorElemento());

        assertNotNull(nodoNieve.getCelda());
        assertNotNull(nodoNieve.getCelda().getElemento());
        assertEquals("V", nodoNieve.getCelda().getTipo());
    }

    @Test
    void deberiaCrearNodoConTipoEnemigo() throws BadDopoException {
        Nodo nodoTroll = new Nodo(5, 5, "T", new CreadorElemento());
        assertEquals(5, nodoTroll.getFila());
        assertEquals(5, nodoTroll.getColumna());
        assertEquals("T", nodoTroll.getCelda().getTipo());
    }

    @Test
    void deberiaRetornarFilaCorrecta() {
        int fila = nodo.getFila();

        assertEquals(1, fila);
    }

    @Test
    void deberiaRetornarColumnaCorrecta() {
        int columna = nodo.getColumna();

        assertEquals(2, columna);
    }

    @Test
    void deberiaRetornarCeldaNoNula() {
        Celda celda = nodo.getCelda();

        assertNotNull(celda);
        assertEquals(1, celda.getFila());
        assertEquals(2, celda.getCol());
        assertEquals("V", celda.getTipo());
    }

    @Test
    void deberiaRetornarListaVecinosVaciaInicialmente() {
        var vecinos = nodo.getVecinos();

        assertNotNull(vecinos);
        assertEquals(0, vecinos.size());
        assertTrue(vecinos.isEmpty());
    }

    @Test
    void deberiaAgregarVecinoCorrectamente() throws BadDopoException {
        Nodo vecino = new Nodo(1, 3, "H", new CreadorElemento());

        nodo.agregarVecino(vecino);

        assertEquals(1, nodo.getVecinos().size());
        assertEquals(vecino, nodo.getVecinos().get(0));
    }

    @Test
    void noDeberiaAgregarVecinoNulo() throws BadDopoException {
        Nodo vecino = new Nodo(1, 3, "BO", new CreadorElemento());
        nodo.agregarVecino(vecino);

        nodo.agregarVecino(null);

        assertEquals(1, nodo.getVecinos().size());
        assertEquals(vecino, nodo.getVecinos().get(0));
    }

    @Test
    void noDeberiaAgregarVecinoDuplicado() throws BadDopoException {

        Nodo vecino = new Nodo(1, 3, "FO", new CreadorElemento());

        nodo.agregarVecino(vecino);
        nodo.agregarVecino(vecino);
        nodo.agregarVecino(vecino);

        assertEquals(1, nodo.getVecinos().size());
    }

    @Test
    void deberiaAgregarMultiplesVecinosDiferentes() throws BadDopoException {
        Nodo vecino1 = new Nodo(1, 3, "BF", new CreadorElemento());
        Nodo vecino2 = new Nodo(2, 2, "CF", new CreadorElemento());
        Nodo vecino3 = new Nodo(1, 1, "P", new CreadorElemento());

        nodo.agregarVecino(vecino1);
        nodo.agregarVecino(vecino2);
        nodo.agregarVecino(vecino3);

        assertEquals(3, nodo.getVecinos().size());
        assertTrue(nodo.getVecinos().contains(vecino1));
        assertTrue(nodo.getVecinos().contains(vecino2));
        assertTrue(nodo.getVecinos().contains(vecino3));
    }

    @Test
    void deberiaCrearNodoConDiferentesTiposDeElementos() throws BadDopoException {
        Nodo[] nodos = {
                new Nodo(3, 2, "T", new CreadorElemento()),
                new Nodo(3, 3, "H", new CreadorElemento()),
                new Nodo(3, 4, "BF", new CreadorElemento()),
                new Nodo(3, 5, "CH", new CreadorElemento()),
                new Nodo(3, 6, "V", new CreadorElemento())
        };

        assertEquals(5, nodos.length);
        assertEquals("T", nodos[0].getCelda().getTipo());
        assertEquals("H", nodos[1].getCelda().getTipo());
        assertEquals("BF", nodos[2].getCelda().getTipo());
        assertEquals("CH", nodos[3].getCelda().getTipo());
        assertEquals("V", nodos[4].getCelda().getTipo());
    }

    @Test
    void deberiaMantenerIndependenciaEntreNodos() throws BadDopoException {
        Nodo nodo1 = new Nodo(1, 1, "V", new CreadorElemento());
        Nodo nodo2 = new Nodo(2, 2, "V", new CreadorElemento());

        nodo1.agregarVecino(nodo2);

        assertEquals(1, nodo1.getVecinos().size());
        assertEquals(0, nodo2.getVecinos().size());
        assertFalse(nodo2.getVecinos().contains(nodo1));
    }
}