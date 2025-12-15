package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HieloTest {

    private Hielo hielo;

    @BeforeEach
    void setUp() throws BadDopoException {
        hielo = new Hielo(5, 9);
    }

    @Test
    void deberiaRetornarFilaYColumnaSinErrores() {
        assertDoesNotThrow(() -> {
            hielo.getFila();
            hielo.getColumna();
        });
    }


    @Test
    void noDeberiaSerPeligroso() {
        assertEquals(false, hielo.esPeligroso());
    }


    @Test
    void deberiaRetornarGananciaCero() {
        assertEquals(0, hielo.getGanancia());
    }

    @Test
    void noDeberiaSerTransitable() {
        assertEquals(false, hielo.esTransitable());
    }

    @Test
    void noDeberiaSerRompible() {
        assertEquals(false, hielo.esRompible());
    }


    @Test
    void noDeberiaFallarAlIntentarMover() {
        assertDoesNotThrow(() ->
                hielo.mover("ARRIBA")
        );
    }


    @Test
    void noDeberiaModificarGananciaAlAumentarPuntaje() {
        hielo.aumentarPuntaje(100);
        assertEquals(0, hielo.getGanancia());
    }

    @Test
    void noDeberiaFallarAlActualizarImagen() {
        assertDoesNotThrow(() ->
                hielo.actualizarImagen("IZQUIERDA")
        );
    }

    @Test
    void noDeberiaFallarAlRomperHielo() {
        assertDoesNotThrow(() ->
                hielo.romperHielo(null, null)
        );
    }

    @Test
    void noDeberiaFallarAlCrearHielo() {
        assertDoesNotThrow(() ->
                hielo.crearHielo(null, null)
        );
    }

    @Test
    void deberiaRetornarArregloVacioConLimites() {
        assertEquals(0, hielo.calcularPosicionesMovimieto(0, 10).length);
    }

    @Test
    void deberiaRetornarArregloVacioSinParametros() {
        assertEquals(0, hielo.calcularPosicionesMovimieto().length);
    }
}
