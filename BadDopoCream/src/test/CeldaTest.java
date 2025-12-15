package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import domain.*;

class CeldaTest {
    private Celda celda;
    private CreadorElemento creador;

    @BeforeEach
    void setUp() throws BadDopoException {
        creador = new CreadorElemento();
        celda = new Celda(1, 2, "V", creador);
    }

    @Test
    void deberiaCrearCeldaConFilaYColumnaCorrectas() throws BadDopoException {
        Celda nuevaCelda = new Celda(3, 4, "V", creador);

        assertEquals(3, nuevaCelda.getFila());
        assertEquals(4, nuevaCelda.getCol());
    }

    @Test
    void deberiaCrearCeldaConTipoCorrecto() throws BadDopoException {
        Celda celdaTroll = new Celda(1, 1, "T", creador);

        assertEquals("T", celdaTroll.getTipo());
    }

    @Test
    void deberiaCrearCeldaConElementoNoNulo() throws BadDopoException {
        Celda celdaNieve = new Celda(2, 2, "V", creador);

        assertNotNull(celdaNieve.getElemento());
        assertEquals("V", celdaNieve.getTipo());
    }

    @Test
    void deberiaRetornarFilaCorrecta() {
        int fila = celda.getFila();

        assertEquals(1, fila);
    }

    @Test
    void deberiaRetornarColumnaCorrecta() {
        int columna = celda.getCol();

        assertEquals(2, columna);
    }

    @Test
    void deberiaRetornarTipoCorrecto() {
        String tipo = celda.getTipo();

        assertEquals("V", tipo);
    }

    @Test
    void deberiaRetornarElementoNoNulo() {
        Elemento elemento = celda.getElemento();

        assertNotNull(elemento);
        assertTrue(elemento instanceof Nieve);
    }

    @Test
    void deberiaTenerPermitidaReconstruccionPorDefecto() {
        boolean permiteReconstruccion = celda.permiteReconstruccion();

        assertTrue(permiteReconstruccion);
    }

    @Test
    void deberiaCambiarPermiteReconstruccion() {
        celda.setPermiteReconstruccion(false);

        boolean permiteReconstruccion = celda.permiteReconstruccion();

        assertFalse(permiteReconstruccion);

        celda.setPermiteReconstruccion(true);

        assertTrue(celda.permiteReconstruccion());
    }

    @Test
    void deberiaCambiarTipoCorrectamente() {
        celda.setTipo("T");

        String tipo = celda.getTipo();

        assertEquals("T", tipo);
    }

    @Test
    void deberiaCambiarTipoYElementoConSetElementoConTipo() throws BadDopoException {
        celda.setElementoConTipo("H", creador);

        assertEquals("H", celda.getTipo());
        assertNotNull(celda.getElemento());
        assertTrue(celda.getElemento() instanceof Hielo);
    }

    @Test
    void deberiaCambiarElementoConSetElemento() throws BadDopoException {
        Elemento nuevoElemento = new Troll(3, 3);

        celda.setElemento(nuevoElemento, creador);

        assertEquals(nuevoElemento, celda.getElemento());
        assertTrue(celda.getElemento() instanceof Troll);
    }

    @Test
    void noDeberiaTenerFrutaCuandoEsNieve() {
        boolean tieneFruta = celda.tieneFruta();

        assertFalse(tieneFruta);
    }

    @Test
    void deberiaTenerFrutaCuandoElementoEsFruta() throws BadDopoException {
        Celda celdaFruta = new Celda(1, 1, "BF", creador); // Banano

        boolean tieneFruta = celdaFruta.tieneFruta();

        assertTrue(tieneFruta);
        assertTrue(celdaFruta.getElemento() instanceof Banano);
    }

    @Test
    void deberiaSerTransitableCuandoEsNieve() {
        boolean transitable = celda.esTransitable();

        assertTrue(transitable);
    }

    @Test
    void deberiaSerTransitableCuandoElementoEsNulo() throws BadDopoException {
        Celda celdaConTipoNulo = new Celda(1, 1, "V", creador);
        celdaConTipoNulo.setTipo(null);

        boolean transitable = celdaConTipoNulo.esTransitable();

        assertTrue(transitable);
    }

    @Test
    void noDeberiaSerTransitableCuandoEsHielo() throws BadDopoException {
        Celda celdaHielo = new Celda(1, 1, "H", creador);

        boolean transitable = celdaHielo.esTransitable();

        assertFalse(transitable);
    }

    @Test
    void noDeberiaSerTransitableCuandoEsBorde() throws BadDopoException {
        Celda celdaBorde = new Celda(1, 1, "B", creador);

        boolean transitable = celdaBorde.esTransitable();

        assertFalse(transitable);
    }

    @Test
    void deberiaSerTransitableCuandoElementoNoEsSolido() throws BadDopoException {
        Celda celdaFruta = new Celda(1, 1, "BF", creador); // Banano (no sólido)

        boolean transitable = celdaFruta.esTransitable();

        assertTrue(transitable);
        assertFalse(celdaFruta.getElemento().esSolido());
    }

    @Test
    void noDeberiaSerTransitableCuandoElementoEsSolido() throws BadDopoException {
        Celda celdaTroll = new Celda(1, 1, "T", creador);

        boolean transitable = celdaTroll.esTransitable();


        assertFalse(transitable);
        assertTrue(celdaTroll.getElemento().esSolido());
    }

    @Test
    void deberiaCambiarDeNieveAFrutaCorrectamente() throws BadDopoException {
        assertEquals("V", celda.getTipo());
        assertFalse(celda.tieneFruta());

        celda.setElementoConTipo("BF", creador);

        assertEquals("BF", celda.getTipo());
        assertTrue(celda.tieneFruta());
        assertTrue(celda.getElemento() instanceof Banano);
    }

    @Test
    void deberiaMantenerMismasCoordenadasAlCambiarElemento() throws BadDopoException {
        assertEquals(1, celda.getFila());
        assertEquals(2, celda.getCol());

        celda.setElementoConTipo("T", creador);

        assertEquals(1, celda.getFila());
        assertEquals(2, celda.getCol());
        assertEquals("T", celda.getTipo());
        assertTrue(celda.getElemento() instanceof Troll);
    }

    @Test
    void deberiaCrearCeldaConHeladoYSerTransitable() throws BadDopoException {
        Celda celdaHelado = new Celda(2, 2, "CH", creador);

        boolean transitable = celdaHelado.esTransitable();

        assertTrue(transitable);
        assertTrue(celdaHelado.getElemento() instanceof Helado);
    }

    @Test
    void deberiaLanzarExcepcionConTipoInvalido() {
        assertThrows(BadDopoException.class, () -> {
            new Celda(1, 1, "TIPO_INVALIDO", creador);
        });
    }
}