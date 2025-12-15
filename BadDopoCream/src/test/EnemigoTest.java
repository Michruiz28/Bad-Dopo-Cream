package test;
import domain.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemigoTest {

    private Enemigo enemigo;

    /**
     * Implementación mínima SOLO para pruebas
     */
    private static class EnemigoDummy extends Enemigo {

        public EnemigoDummy(int fila, int col, int velocidad) {
            super(fila, col, velocidad);
        }

        @Override
        public void actualizarImagen(String ultimaDireccion) {
        }

        @Override
        public void romperHielo(Celda celdaARomper, CreadorElemento creador) {
        }

        @Override
        public void crearHielo(Celda celdaACrear, CreadorElemento creador) {
        }

        @Override
        public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
            return new int[0];
        }

        @Override
        public void aumentarPuntaje(int puntaje) {
        }

        @Override
        public int getGanancia() {
            return 0;
        }

        @Override
        public boolean esTransitable() {
            return false;
        }
    }

    @BeforeEach
    void setUp() {
        enemigo = new EnemigoDummy(4, 6, 2);
    }

    @Test
    void deberiaInicializarCorrectamenteAtributosBase() {
        assertEquals(4, enemigo.getFila());
        assertEquals(6, enemigo.getColumna());
        assertEquals(2, enemigo.getVelocidad());
        assertEquals("ARRIBA", enemigo.getUltimaDireccion());
    }

    @Test
    void deberiaInicializarFlagsEnFalse() {
        assertEquals(false, enemigo.isPersigueJugador());
        assertEquals(false, enemigo.canRomperBloques());
        assertEquals(false, enemigo.rompeUnBloquePorVez());
    }

    @Test
    void deberiaActualizarUltimaDireccionAlMover() throws BadDopoException {
        enemigo.mover("DERECHA");
        assertEquals("DERECHA", enemigo.getUltimaDireccion());
    }

    @Test
    void noDeberiaPermitirMoverConDireccionNull() {
        assertThrows(
                BadDopoException.class,
                () -> enemigo.mover(null)
        );
    }

    @Test
    void noDeberiaPermitirMoverConDireccionVacia() {
        assertThrows(
                BadDopoException.class,
                () -> enemigo.mover("")
        );
    }

    @Test
    void noDeberiaFallarSiNoHayEstrategiaMovimiento() {
        assertDoesNotThrow(() ->
                enemigo.ejecutarComportamiento(null, null, null)
        );
    }

    @Test
    void deberiaSerSolido() {
        assertEquals(true, enemigo.esSolido());
    }

    @Test
    void deberiaSerEnemigo() {
        assertEquals(true, enemigo.esEnemigo());
    }

    @Test
    void deberiaRetornarCodigoTipoV() {
        assertEquals("V", enemigo.codigoTipo());
    }

    @Test
    void deberiaSerSolidoEstatico() {
        assertEquals(true, Enemigo.esSolidoEstatico());
    }
}
