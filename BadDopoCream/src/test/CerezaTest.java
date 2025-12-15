package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import domain.*;

class CerezaTest {

    private Cereza cereza;
    private Cereza cereza2;
    private CreadorElemento creador;

    @BeforeEach
    void setUp() throws BadDopoException {
        cereza = new Cereza(5, 5);
        cereza2 = new Cereza(10, 12);
        creador = new UnJugador();
    }


    @Test
    void deberiaCrearCerezaConPosicionValida() throws BadDopoException {
        Cereza nuevaCereza = new Cereza(8, 9);
        assertEquals(8, nuevaCereza.getFila());
        assertEquals(9, nuevaCereza.getColumna());
    }

    @Test
    void deberiaCrearCerezaEnPosicion5_5() throws BadDopoException {
        assertEquals(5, cereza.getFila());
        assertEquals(5, cereza.getColumna());
    }

    @Test
    void deberiaCrearCerezaEnPosicion10_12() throws BadDopoException {
        assertEquals(10, cereza2.getFila());
        assertEquals(12, cereza2.getColumna());
    }


    @Test
    void deberiaRetornarCodigoCF() {
        assertEquals("CF", cereza.getCodigo());
    }

    @Test
    void deberiaRetornarMismoCodigoParaTodasLasCerezas() {
        assertEquals("CF", cereza.getCodigo());
        assertEquals("CF", cereza2.getCodigo());
    }

    @Test
    void deberiaRetornarCodigoNoNulo() {
        assertNotNull(cereza.getCodigo());
    }

    @Test
    void deberiaRetornar150PuntosDeGanancia() {
        assertEquals(150, cereza.getGanancia());
    }

    @Test
    void deberiaRetornarConstanteGANANCIA_CEREZA() {
        assertEquals(Cereza.GANANCIA_CEREZA, cereza.getGanancia());
    }

    @Test
    void deberiaRetornarMismaGananciaParaTodasLasCerezas() {
        assertEquals(150, cereza.getGanancia());
        assertEquals(150, cereza2.getGanancia());
    }

    @Test
    void deberiaRetornarArrayDe2Elementos() {
        int[] posicion = cereza.calcularPosicionesMovimieto(2, 15, 15);
        assertEquals(2, posicion.length);
    }

    @Test
    void deberiaRetornarFilaDentroDelRango() {
        int[] posicion = cereza.calcularPosicionesMovimieto(5, 10, 10);
        int fila = posicion[0];
        assertEquals(true, fila >= 5 && fila <= 10);
    }

    @Test
    void deberiaRetornarColumnaDentroDelRango() {
        int[] posicion = cereza.calcularPosicionesMovimieto(3, 12, 12);
        int columna = posicion[1];
        assertEquals(true, columna >= 3 && columna <= 12);
    }

    @Test
    void deberiaRetornarPosicionValidaDeLista() {
        ArrayList<int[]> posiciones = new ArrayList<>();
        posiciones.add(new int[]{5, 5});
        posiciones.add(new int[]{8, 8});
        posiciones.add(new int[]{10, 10});

        int[] resultado = cereza.calcularPosicionAleatoria(posiciones);

        assertNotNull(resultado);
        assertEquals(2, resultado.length);
    }

    @Test
    void deberiaRetornarNullConListaVacia() {
        ArrayList<int[]> posicionesVacias = new ArrayList<>();
        int[] resultado = cereza.calcularPosicionAleatoria(posicionesVacias);
        assertNull(resultado);
    }

    @Test
    void deberiaRetornarNullConListaNula() {
        int[] resultado = cereza.calcularPosicionAleatoria(null);
        assertNull(resultado);
    }

    @Test
    void deberiaSeleccionarUnaDeLasPosicionesDisponibles() {
        ArrayList<int[]> posiciones = new ArrayList<>();
        posiciones.add(new int[]{3, 3});
        posiciones.add(new int[]{6, 6});
        posiciones.add(new int[]{9, 9});

        int[] resultado = cereza.calcularPosicionAleatoria(posiciones);

        boolean esValida = (resultado[0] == 3 && resultado[1] == 3) ||
                (resultado[0] == 6 && resultado[1] == 6) ||
                (resultado[0] == 9 && resultado[1] == 9);

        assertEquals(true, esValida);
    }

    @Test
    void noDeberiaLanzarExcepcionAlActualizar() {
        assertDoesNotThrow(() -> {
            cereza.actualizar(System.currentTimeMillis());
        });
    }

    @Test
    void deberiaCompletarActualizacionSinCambios() throws BadDopoException {
        int filaAntes = cereza.getFila();
        int colAntes = cereza.getColumna();

        cereza.actualizar(System.currentTimeMillis());

        assertEquals(filaAntes, cereza.getFila());
        assertEquals(colAntes, cereza.getColumna());
    }

    @Test
    void noDeberiaPermitirUsoDeMetodoConDosParametros() {
        assertThrows(UnsupportedOperationException.class, () -> {
            cereza.calcularPosicionesMovimieto(5, 15);
        });
    }

    @Test
    void deberiaDefinirConstanteGANANCIA_CEREZA() {
        assertEquals(150, Cereza.GANANCIA_CEREZA);
    }

    @Test
    void constanteGananciaDeberiaSer150() {
        int ganancia = Cereza.GANANCIA_CEREZA;
        assertEquals(150, ganancia);
    }

    @Test
    void deberiaSerInstanciaDeFruta() {
        assertEquals(true, cereza instanceof Fruta);
    }

    @Test
    void deberiaSerInstanciaDeFrutaEnMovimiento() {
        assertEquals(true, cereza instanceof FrutaEnMovimiento);
    }

    @Test
    void deberiaSerInstanciaDeElemento() {
        assertEquals(true, cereza instanceof Elemento);
    }

    @Test
    void deberiaCalcularMultiplesPosicionesAleatorias() {
        boolean variacion = false;
        int primerResultadoFila = -1;

        for (int i = 0; i < 10; i++) {
            int[] posicion = cereza.calcularPosicionesMovimieto(2, 15, 15);
            if (primerResultadoFila == -1) {
                primerResultadoFila = posicion[0];
            } else if (primerResultadoFila != posicion[0]) {
                variacion = true;
                break;
            }
        }

        assertEquals(true, variacion || primerResultadoFila >= 2);
    }

    @Test
    void deberiaPermitirPosicionEnEsquinaSuperiorIzquierda() throws BadDopoException {
        Cereza cerezaEsquina = new Cereza(2, 2);
        assertEquals(2, cerezaEsquina.getFila());
        assertEquals(2, cerezaEsquina.getColumna());
    }

    @Test
    void deberiaPermitirPosicionEnEsquinaInferiorDerecha() throws BadDopoException {
        Cereza cerezaEsquina = new Cereza(18, 18);
        assertEquals(18, cerezaEsquina.getFila());
        assertEquals(18, cerezaEsquina.getColumna());
    }

    @Test
    void deberiaPermitirPosicionEnCentroDelTablero() throws BadDopoException {
        Cereza cerezaCentro = new Cereza(10, 10);
        assertEquals(10, cerezaCentro.getFila());
        assertEquals(10, cerezaCentro.getColumna());
    }

    @Test
    void deberiaSeleccionarDeListaConUnaPosicion() {
        ArrayList<int[]> posiciones = new ArrayList<>();
        posiciones.add(new int[]{7, 7});

        int[] resultado = cereza.calcularPosicionAleatoria(posiciones);

        assertEquals(7, resultado[0]);
        assertEquals(7, resultado[1]);
    }

    @Test
    void deberiaSeleccionarDeListaCon10Posiciones() {
        ArrayList<int[]> posiciones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posiciones.add(new int[]{i, i});
        }

        int[] resultado = cereza.calcularPosicionAleatoria(posiciones);

        assertNotNull(resultado);
        assertEquals(true, resultado[0] >= 0 && resultado[0] < 10);
    }
}