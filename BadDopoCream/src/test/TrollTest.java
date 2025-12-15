package test;
import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrollTest {

    private Troll troll;

    @BeforeEach
    void setUp() {
        troll = new Troll(3, 5);
    }

    @Test
    void deberiaInicializarPosicionCorrectamente() {
        assertEquals(3, troll.getFila());
        assertEquals(5, troll.getColumna());
    }

    @Test
    void deberiaTenerCodigoTipoT() {
        assertEquals("T", troll.codigoTipo());
    }

    @Test
    void noDeberiaSerTransitable() {
        assertEquals(false, troll.esTransitable());
    }


    @Test
    void noDeberiaFallarAlRomperHielo() {
        assertDoesNotThrow(() -> troll.romperHielo(null, null));
    }

    @Test
    void noDeberiaFallarAlCrearHielo() {
        assertDoesNotThrow(() -> troll.crearHielo(null, null));
    }

    @Test
    void deberiaRetornarArregloVacioEnCalculoMovimiento() {
        assertEquals(0, troll.calcularPosicionesMovimieto(0, 10).length);
    }

    @Test
    void noDeberiaModificarGanancia() {
        troll.aumentarPuntaje(100);
        assertEquals(0, troll.getGanancia());
    }
}
