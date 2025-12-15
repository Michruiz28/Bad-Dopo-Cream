package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BananoTest {

    private Banano banano;

    @BeforeEach
    void setUp() throws BadDopoException {
        banano = new Banano(5, 8);
    }

    @Test
    void deberiaInicializarPosicionCorrectamente() {
        assertEquals(5, banano.getFila());
        assertEquals(8, banano.getColumna());
    }

    @Test
    void deberiaTenerGananciaCorrecta() {
        assertEquals(Banano.GANANCIA_BANANO, banano.getGanancia());
    }

    @Test
    void deberiaRetornarCodigoBF() {
        assertEquals("BF", banano.getCodigo());
    }

    @Test
    void noDeberiaFallarAlIntentarMover() {
        assertDoesNotThrow(() ->
                banano.mover("ARRIBA")
        );
    }

    @Test
    void noDeberiaModificarGananciaAlAumentarPuntaje() {
        banano.aumentarPuntaje(200);
        assertEquals(Banano.GANANCIA_BANANO, banano.getGanancia());
    }

    @Test
    void noDeberiaFallarAlActualizarImagen() {
        assertDoesNotThrow(() ->
                banano.actualizarImagen("DERECHA")
        );
    }

    @Test
    void noDeberiaFallarAlRomperHielo() {
        assertDoesNotThrow(() ->
                banano.romperHielo(null, null)
        );
    }

    @Test
    void noDeberiaFallarAlCrearHielo() {
        assertDoesNotThrow(() ->
                banano.crearHielo(null, null)
        );
    }

    @Test
    void deberiaRetornarArregloVacioEnCalculoMovimiento() {
        assertEquals(0, banano.calcularPosicionesMovimieto(0, 10).length);
    }
    @Test
    void noDeberiaFallarConstructorConCeldaNull() {
        assertDoesNotThrow(() ->
                new Banano(1, 1, 50, null)
        );
    }
}
