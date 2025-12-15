package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MacetaTest {

    private Maceta maceta;

    @BeforeEach
    void setUp() {
        maceta = new Maceta(6, 7);
    }

    @Test
    void deberiaInicializarPosicionCorrectamente() {
        assertEquals(6, maceta.getFila());
        assertEquals(7, maceta.getColumna());
    }

    @Test
    void deberiaTenerCodigoTipoM() {
        assertEquals("M", maceta.codigoTipo());
    }

    @Test
    void noDeberiaSerTransitable() {
        assertEquals(false, maceta.esTransitable());
    }


    @Test
    void noDeberiaFallarAlEjecutarComportamientoConNulls() {
        assertDoesNotThrow(() ->
                maceta.ejecutarComportamiento(null, null, null)
        );
    }


    @Test
    void noDeberiaFallarAlRomperHielo() {
        assertDoesNotThrow(() -> maceta.romperHielo(null, null));
    }

    @Test
    void noDeberiaFallarAlCrearHielo() {
        assertDoesNotThrow(() -> maceta.crearHielo(null, null));
    }

    @Test
    void deberiaRetornarArregloVacioEnCalculoMovimiento() {
        assertEquals(0, maceta.calcularPosicionesMovimieto(0, 10).length);
    }

    @Test
    void noDeberiaModificarGanancia() {
        maceta.aumentarPuntaje(300);
        assertEquals(0, maceta.getGanancia());
    }
}
