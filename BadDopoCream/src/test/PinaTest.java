package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PinaTest {

    private Pina pina;

    @BeforeEach
    void setUp() throws BadDopoException {
        pina = new Pina(4, 7);
    }

    @Test
    void deberiaInicializarFilaYColumnaCorrectamente() {
        assertEquals(4, pina.getFila());
        assertEquals(7, pina.getColumna());
    }


    @Test
    void deberiaRetornarCodigoP() {
        assertEquals("P", pina.getCodigo());
    }

    @Test
    void deberiaRetornarGananciaCorrecta() {
        assertEquals(Pina.GANANCIA_PINA, pina.getGanancia());
    }


    @Test
    void noDeberiaFallarAlIntentarMover() {
        assertDoesNotThrow(() ->
                pina.mover("ARRIBA")
        );
    }

    @Test
    void deberiaActualizarFilaYColumnaAlMoverConPosicion() {
        pina.moverConPosicion(10, 12);
        assertEquals(10, pina.getFila());
        assertEquals(12, pina.getColumna());
    }

    @Test
    void deberiaRetornarSuPosicionActual() {
        int[] posicion = pina.calcularPosicionesMovimieto(0, 20);
        assertEquals(4, posicion[0]);
        assertEquals(7, posicion[1]);
    }

    @Test
    void deberiaRetornarNullSiNoHayPosicionesDisponibles() {
        assertEquals(null, pina.calcularPosicionAleatoria(null));
    }


    @Test
    void noDeberiaFallarAlActualizarImagen() {
        assertDoesNotThrow(() ->
                pina.actualizarImagen("DERECHA")
        );
    }

    @Test
    void noDeberiaFallarAlRomperHielo() {
        assertDoesNotThrow(() ->
                pina.romperHielo(null, null)
        );
    }

    @Test
    void noDeberiaFallarAlCrearHielo() {
        assertDoesNotThrow(() ->
                pina.crearHielo(null, null)
        );
    }

    @Test
    void noDeberiaModificarGananciaAlAumentarPuntaje() {
        pina.aumentarPuntaje(500);
        assertEquals(Pina.GANANCIA_PINA, pina.getGanancia());
    }
}
