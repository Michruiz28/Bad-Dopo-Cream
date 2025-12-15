package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalamarTest {

    private Calamar calamar;

    @BeforeEach
    void setUp() {
        calamar = new Calamar(2, 4);
    }

    @Test
    void deberiaInicializarPosicionCorrectamente() {
        assertEquals(2, calamar.getFila());
        assertEquals(4, calamar.getColumna());
    }

    @Test
    void deberiaTenerCodigoTipoC() {
        assertEquals("C", calamar.codigoTipo());
    }

    @Test
    void noDeberiaSerTransitable() {
        assertEquals(false, calamar.esTransitable());
    }


    @Test
    void noDeberiaFallarSiParametrosSonNull() {
        assertDoesNotThrow(() ->
                calamar.ejecutarComportamiento(null, null, null)
        );
    }

    @Test
    void noDeberiaFallarAlRomperHieloConCeldaNull() {
        assertDoesNotThrow(() ->
                calamar.romperHielo(null, null)
        );
    }

    @Test
    void deberiaRetornarArregloVacioEnCalculoMovimiento() {
        assertEquals(0, calamar.calcularPosicionesMovimieto(0, 10).length);
    }

    @Test
    void noDeberiaModificarGanancia() {
        calamar.aumentarPuntaje(200);
        assertEquals(0, calamar.getGanancia());
    }
}
