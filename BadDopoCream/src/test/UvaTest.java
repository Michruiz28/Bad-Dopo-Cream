package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UvaTest {

    private Uva uva;

    @BeforeEach
    void setUp() throws BadDopoException {
        uva = new Uva(3, 6);
    }

    @Test
    void deberiaInicializarPosicionCorrectamente() {
        assertEquals(3, uva.getFila());
        assertEquals(6, uva.getColumna());
    }


    @Test
    void deberiaRetornarGananciaCorrecta() {
        assertEquals(Uva.GANANCIA_UVA, uva.getGanancia());
    }

    @Test
    void deberiaRetornarCodigoU() {
        assertEquals("U", uva.getCodigo());
    }

    @Test
    void noDeberiaFallarAlIntentarMover() {
        assertDoesNotThrow(() ->
                uva.mover("ARRIBA")
        );
    }

    @Test
    void noDeberiaModificarGananciaAlAumentarPuntaje() {
        uva.aumentarPuntaje(200);
        assertEquals(Uva.GANANCIA_UVA, uva.getGanancia());
    }

    @Test
    void noDeberiaFallarAlActualizarImagen() {
        assertDoesNotThrow(() ->
                uva.actualizarImagen("IZQUIERDA")
        );
    }

    @Test
    void noDeberiaFallarAlRomperHielo() {
        assertDoesNotThrow(() ->
                uva.romperHielo(null, null)
        );
    }

    @Test
    void noDeberiaFallarAlCrearHielo() {
        assertDoesNotThrow(() ->
                uva.crearHielo(null, null)
        );
    }

    @Test
    void deberiaRetornarArregloVacioEnCalculoMovimiento() {
        assertEquals(0, uva.calcularPosicionesMovimieto(0, 10).length);
    }
}
