package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class HeladoTest {

    private Helado heladoVainilla;
    private Helado heladoChocolate;
    private Helado heladoFresa;
    private CreadorElemento creador;

    @BeforeEach
    void setUp() throws BadDopoException {
        heladoVainilla = new Helado(5, 5, "VH");
        heladoChocolate = new Helado(7, 8, "CH");
        heladoFresa = new Helado(10, 12, "F");
        creador = new UnJugador();
    }

    @Test
    void deberiaCrearHeladoConPosicionValida() throws BadDopoException {
        Helado helado = new Helado(5, 5, "VH");
        assertEquals(5, helado.getFila());
        assertEquals(5, helado.getColumna());
    }

    @Test
    void deberiaCrearHeladoConSaborVainilla() throws BadDopoException {
        Helado helado = new Helado(5, 5, "VH");
        assertEquals("VH", helado.getSabor());
    }

    @Test
    void noDeberiaCrearHeladoEnPosicionInvalida() {
        BadDopoException excepcion = assertThrows(BadDopoException.class, () -> {
            new Helado(0, 5, "VH");
        });
        assertEquals(BadDopoException.POSICION_INVALIDA, excepcion.getMessage());
    }

    @Test
    void noDeberiaCrearHeladoEnFila0() {
        assertThrows(BadDopoException.class, () -> {
            new Helado(0, 5, "VH");
        });
    }

    @Test
    void noDeberiaCrearHeladoEnFila1() {
        assertThrows(BadDopoException.class, () -> {
            new Helado(1, 5, "VH");
        });
    }

    @Test
    void noDeberiaCrearHeladoEnColumna17() {
        assertThrows(BadDopoException.class, () -> {
            new Helado(5, 17, "VH");
        });
    }

    @Test
    void noDeberiaCrearHeladoConSaborNulo() {
        BadDopoException excepcion = assertThrows(BadDopoException.class, () -> {
            new Helado(5, 5, null);
        });
        assertEquals(BadDopoException.SABOR_INVALIDO, excepcion.getMessage());
    }

    @Test
    void noDeberiaCrearHeladoConSaborVacio() {
        assertThrows(BadDopoException.class, () -> {
            new Helado(5, 5, "");
        });
    }

    @Test
    void noDeberiaCrearHeladoConSaborEspaciosBlanco() {
        assertThrows(BadDopoException.class, () -> {
            new Helado(5, 5, "   ");
        });
    }

    @Test
    void deberiaRetornarSaborVainilla() {
        assertEquals("VH", heladoVainilla.getSabor());
    }

    @Test
    void deberiaRetornarSaborChocolate() {
        assertEquals("CH", heladoChocolate.getSabor());
    }

    @Test
    void deberiaRetornarSaborFresa() {
        assertEquals("F", heladoFresa.getSabor());
    }

    @Test
    void deberiaCambiarSaborCorrectamente() throws BadDopoException {
        heladoVainilla.setSabor("CH");
        assertEquals("CH", heladoVainilla.getSabor());
    }

    @Test
    void noDeberiaPermitirCambiarASaborNulo() {
        BadDopoException excepcion = assertThrows(BadDopoException.class, () -> {
            heladoVainilla.setSabor(null);
        });
        assertEquals(BadDopoException.SABOR_INVALIDO, excepcion.getMessage());
    }

    @Test
    void noDeberiaPermitirCambiarASaborVacio() {
        assertThrows(BadDopoException.class, () -> {
            heladoVainilla.setSabor("");
        });
    }

    @Test
    void deberiaIniciarConPuntajeCero() {
        assertEquals(0, heladoVainilla.getPuntaje());
    }

    @Test
    void deberiaIniciarTodosLosHeladosConPuntajeCero() {
        assertEquals(0, heladoVainilla.getPuntaje());
        assertEquals(0, heladoChocolate.getPuntaje());
        assertEquals(0, heladoFresa.getPuntaje());
    }

    @Test
    void deberiaEstablecerPuntajePositivo() throws BadDopoException {
        heladoVainilla.setPuntaje(100);
        assertEquals(100, heladoVainilla.getPuntaje());
    }

    @Test
    void deberiaEstablecerPuntajeCero() throws BadDopoException {
        heladoVainilla.setPuntaje(50);
        heladoVainilla.setPuntaje(0);
        assertEquals(0, heladoVainilla.getPuntaje());
    }

    @Test
    void noDeberiaPermitirPuntajeNegativo() {
        BadDopoException excepcion = assertThrows(BadDopoException.class, () -> {
            heladoVainilla.setPuntaje(-10);
        });
        assertEquals(BadDopoException.PUNTAJE_INVALIDO, excepcion.getMessage());
    }

    @Test
    void deberiaAumentarPuntajeCorrectamente() {
        heladoVainilla.aumentarPuntaje(50);
        assertEquals(50, heladoVainilla.getPuntaje());
    }

    @Test
    void deberiaAcumularPuntajesMultiples() {
        heladoVainilla.aumentarPuntaje(30);
        heladoVainilla.aumentarPuntaje(20);
        heladoVainilla.aumentarPuntaje(10);
        assertEquals(60, heladoVainilla.getPuntaje());
    }

    @Test
    void deberiaPermitirAumentarPuntajeCon100Puntos() {
        heladoVainilla.aumentarPuntaje(100);
        assertEquals(100, heladoVainilla.getPuntaje());
    }

    @Test
    void deberiaRetornarGananciaCero() {
        assertEquals(0, heladoVainilla.getGanancia());
    }

    @Test
    void deberiaRetornarGananciaIgualAPuntaje() {
        heladoVainilla.aumentarPuntaje(75);
        assertEquals(75, heladoVainilla.getGanancia());
    }

    @Test
    void deberiaRetornarGananciaActualizada() {
        heladoVainilla.aumentarPuntaje(50);
        heladoVainilla.aumentarPuntaje(25);
        assertEquals(75, heladoVainilla.getGanancia());
    }

    @Test
    void deberiaCambiarFilaCorrectamente() {
        heladoVainilla.setFila(10);
        assertEquals(10, heladoVainilla.getFila());
    }

    @Test
    void deberiaCambiarFilaMultiplesVeces() {
        heladoVainilla.setFila(8);
        heladoVainilla.setFila(12);
        assertEquals(12, heladoVainilla.getFila());
    }

    @Test
    void deberiaCambiarFilaDe5A15() {
        assertEquals(5, heladoVainilla.getFila());
        heladoVainilla.setFila(15);
        assertEquals(15, heladoVainilla.getFila());
    }

    @Test
    void deberiaCambiarColumnaCorrectamente() {
        heladoVainilla.setColumna(10);
        assertEquals(10, heladoVainilla.getColumna());
    }

    @Test
    void deberiaCambiarColumnaMultiplesVeces() {
        heladoVainilla.setColumna(7);
        heladoVainilla.setColumna(14);
        assertEquals(14, heladoVainilla.getColumna());
    }

    @Test
    void deberiaCambiarColumnaDe5A15() {
        assertEquals(5, heladoVainilla.getColumna());
        heladoVainilla.setColumna(15);
        assertEquals(15, heladoVainilla.getColumna());
    }

    @Test
    void deberiaGuardarDireccionAlMover() throws BadDopoException {
        heladoVainilla.mover("ARRIBA");
        assertEquals("ARRIBA", heladoVainilla.getUltimaDireccion());
    }

    @Test
    void deberiaActualizarDireccionConCadaMovimiento() throws BadDopoException {
        heladoVainilla.mover("DERECHA");
        heladoVainilla.mover("IZQUIERDA");
        assertEquals("IZQUIERDA", heladoVainilla.getUltimaDireccion());
    }

    @Test
    void noDeberiaPermitirDireccionNula() {
        BadDopoException excepcion = assertThrows(BadDopoException.class, () -> {
            heladoVainilla.mover(null);
        });
        assertEquals(BadDopoException.DIRECCION_INVALIDA, excepcion.getMessage());
    }

    @Test
    void deberiaRetornarNuloSinMovimientos() {
        assertNull(heladoVainilla.getUltimaDireccion());
    }

    @Test
    void deberiaRetornarUltimaDireccionDerecha() throws BadDopoException {
        heladoVainilla.mover("DERECHA");
        assertEquals("DERECHA", heladoVainilla.getUltimaDireccion());
    }

    @Test
    void deberiaRetornarUltimaDireccionAbajo() throws BadDopoException {
        heladoVainilla.mover("ABAJO");
        assertEquals("ABAJO", heladoVainilla.getUltimaDireccion());
    }

    @Test
    void deberiaIniciarConTimestampCero() {
        assertEquals(0, heladoVainilla.getUltimoMovimientoTime());
    }

    @Test
    void deberiaActualizarTimestampAlMover() throws BadDopoException {
        long antes = System.currentTimeMillis();
        heladoVainilla.mover("ARRIBA");
        long despues = heladoVainilla.getUltimoMovimientoTime();

        assertEquals(true, despues >= antes);
    }

    @Test
    void deberiaActualizarTimestampEnCadaMovimiento() throws BadDopoException, InterruptedException {
        heladoVainilla.mover("DERECHA");
        long primerTimestamp = heladoVainilla.getUltimoMovimientoTime();

        Thread.sleep(10);
        heladoVainilla.mover("IZQUIERDA");
        long segundoTimestamp = heladoVainilla.getUltimoMovimientoTime();

        assertEquals(true, segundoTimestamp > primerTimestamp);
    }

    @Test
    void deberiaVolverAPosicionInicialFila() throws BadDopoException {
        heladoVainilla.setFila(10);
        heladoVainilla.setPosicionInicial();
        assertEquals(5, heladoVainilla.getFila());
    }

    @Test
    void deberiaVolverAPosicionInicialColumna() throws BadDopoException {
        heladoVainilla.setColumna(15);
        heladoVainilla.setPosicionInicial();
        assertEquals(5, heladoVainilla.getColumna());
    }

    @Test
    void deberiaVolverAPosicionInicialCompleta() throws BadDopoException {
        heladoChocolate.setFila(15);
        heladoChocolate.setColumna(15);
        heladoChocolate.setPosicionInicial();

        assertEquals(7, heladoChocolate.getFila());
        assertEquals(8, heladoChocolate.getColumna());
    }


    @Test
    void deberiaCrearHieloEnCeldaVacia() throws BadDopoException {
        Celda celda = new Celda(5, 5, "V", creador);
        heladoVainilla.crearHielo(celda, creador);
        assertEquals("H", celda.getTipo());
    }

    @Test
    void deberiaConvertirCeldaVaciaEnHielo() throws BadDopoException {
        Celda celda = new Celda(3, 3, "V", creador);
        assertEquals("V", celda.getTipo());

        heladoVainilla.crearHielo(celda, creador);
        assertEquals("H", celda.getTipo());
    }

    @Test
    void deberiaPoderCrearHieloConCualquierHelado() throws BadDopoException {
        Celda celda1 = new Celda(5, 5, "V", creador);
        Celda celda2 = new Celda(6, 6, "V", creador);
        Celda celda3 = new Celda(7, 7, "V", creador);

        heladoVainilla.crearHielo(celda1, creador);
        heladoChocolate.crearHielo(celda2, creador);
        heladoFresa.crearHielo(celda3, creador);

        assertEquals("H", celda1.getTipo());
        assertEquals("H", celda2.getTipo());
        assertEquals("H", celda3.getTipo());
    }

    @Test
    void deberiaRomperHieloEnCelda() throws BadDopoException {
        Celda celda = new Celda(5, 5, "H", creador);
        heladoVainilla.romperHielo(celda, creador);
        assertEquals("V", celda.getTipo());
    }

    @Test
    void deberiaConvertirHieloEnCeldaVacia() throws BadDopoException {
        Celda celda = new Celda(8, 8, "H", creador);
        assertEquals("H", celda.getTipo());

        heladoVainilla.romperHielo(celda, creador);
        assertEquals("V", celda.getTipo());
    }

    @Test
    void deberiaPoderRomperHieloConCualquierHelado() throws BadDopoException {
        Celda celda1 = new Celda(5, 5, "H", creador);
        Celda celda2 = new Celda(6, 6, "H", creador);
        Celda celda3 = new Celda(7, 7, "H", creador);

        heladoVainilla.romperHielo(celda1, creador);
        heladoChocolate.romperHielo(celda2, creador);
        heladoFresa.romperHielo(celda3, creador);

        assertEquals("V", celda1.getTipo());
        assertEquals("V", celda2.getTipo());
        assertEquals("V", celda3.getTipo());
    }


    @Test
    void noDeberiaSerTransitable() {
        assertEquals(false, heladoVainilla.esTransitable());
    }

    @Test
    void ningunHeladoDeberiaSerTransitable() {
        assertEquals(false, heladoVainilla.esTransitable());
        assertEquals(false, heladoChocolate.esTransitable());
        assertEquals(false, heladoFresa.esTransitable());
    }


    @Test
    void deberiaSerHelado() {
        assertEquals(true, heladoVainilla.esHelado());
    }

    @Test
    void todosLosHeladosDeberianSerHelado() {
        assertEquals(true, heladoVainilla.esHelado());
        assertEquals(true, heladoChocolate.esHelado());
        assertEquals(true, heladoFresa.esHelado());
    }

    @Test
    void deberiaActualizarImagenConDireccionDerecha() {
        heladoVainilla.actualizarImagen("DERECHA");
        assertNotNull(heladoVainilla);
    }

    @Test
    void deberiaActualizarImagenConDireccionIzquierda() {
        heladoVainilla.actualizarImagen("IZQUIERDA");
        assertNotNull(heladoVainilla);
    }

    @Test
    void deberiaActualizarImagenConDireccionArriba() {
        heladoVainilla.actualizarImagen("ARRIBA");
        assertNotNull(heladoVainilla);
    }

    @Test
    void deberiaDefinirImagenesParaVainilla() throws BadDopoException {
        Helado helado = new Helado(5, 5, "VH");
        // Si no lanza excepción, las imágenes se definieron correctamente
        assertNotNull(helado);
    }

    @Test
    void deberiaDefinirImagenesParaChocolate() throws BadDopoException {
        Helado helado = new Helado(5, 5, "CH");
        assertNotNull(helado);
    }

    @Test
    void deberiaDefinirImagenesParaFresa() throws BadDopoException {
        Helado helado = new Helado(5, 5, "F");
        assertNotNull(helado);
    }

    @Test
    void deberiaRetornarArrayVacio() {
        int[] posiciones = heladoVainilla.calcularPosicionesMovimieto(0, 10);
        assertEquals(0, posiciones.length);
    }

    @Test
    void deberiaRetornarArrayVacioConCualquierLimite() {
        int[] posiciones = heladoVainilla.calcularPosicionesMovimieto(5, 15);
        assertEquals(0, posiciones.length);
    }

    @Test
    void deberiaCrearYRomperHieloEnSecuencia() throws BadDopoException {
        Celda celda = new Celda(5, 5, "V", creador);

        heladoVainilla.crearHielo(celda, creador);
        assertEquals("H", celda.getTipo());

        heladoVainilla.romperHielo(celda, creador);
        assertEquals("V", celda.getTipo());
    }

    @Test
    void deberiaMoverYGuardarDireccionesMultiples() throws BadDopoException {
        heladoVainilla.mover("DERECHA");
        assertEquals("DERECHA", heladoVainilla.getUltimaDireccion());

        heladoVainilla.mover("ABAJO");
        assertEquals("ABAJO", heladoVainilla.getUltimaDireccion());

        heladoVainilla.mover("IZQUIERDA");
        assertEquals("IZQUIERDA", heladoVainilla.getUltimaDireccion());
    }

    @Test
    void deberiaMantenerPuntajeAcumuladoDespuesDeVariosMov() throws BadDopoException {
        heladoVainilla.aumentarPuntaje(10);
        heladoVainilla.mover("DERECHA");
        heladoVainilla.aumentarPuntaje(20);
        heladoVainilla.mover("ABAJO");
        heladoVainilla.aumentarPuntaje(30);

        assertEquals(60, heladoVainilla.getPuntaje());
    }
}