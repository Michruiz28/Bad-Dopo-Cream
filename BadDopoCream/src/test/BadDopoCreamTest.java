package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import domain.*;

class BadDopoCreamTest {

    private BadDopoCream juego;
    private BadDopoCream juegoMultijugador;

    @BeforeEach
    void setUp() throws BadDopoException {
        juego = new BadDopoCream(1, "Un jugador", "VH", null, 
                                 "Jugador 1", null, null, null);

        juegoMultijugador = new BadDopoCream(1, "Jugador vs Jugador", "VH", "CH",
                                             "Jugador 1", "Jugador 2", null, null);
    }


    @Test
    void deberiaCrearJuegoConNivel1Valido() throws BadDopoException {
        BadDopoCream juegoNuevo = new BadDopoCream(1, "Un jugador", "VH", null,
                                                    "Test", null, null, null);
        assertEquals(0, juegoNuevo.getNivelActual()); // nivel 1 = índice 0
    }

    @Test
    void deberiaCrearJuegoConNivel2Valido() throws BadDopoException {
        BadDopoCream juegoNuevo = new BadDopoCream(2, "Un jugador", "VH", null,
                                                    "Test", null, null, null);
        assertEquals(1, juegoNuevo.getNivelActual()); // nivel 2 = índice 1
    }

    @Test
    void noDeberiaCrearJuegoConNivelInvalido() {
        BadDopoException excepcion = assertThrows(BadDopoException.class, () -> {
            new BadDopoCream(5, "Un jugador", "VH", null, "Test", null, null, null);
        });
        assertEquals(BadDopoException.NIVEL_INVALIDO, excepcion.getMessage());
    }

    @Test
    void deberiaIniciarJuegoCorrectamente() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals(true, juego.isJuegoIniciado());
    }

    @Test
    void deberiaCrearHelado1AlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getHelado1());
    }

    @Test
    void deberiaInicializarTiempoAlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        long tiempoRestante = juego.getTiempoRestante();
        assertEquals(180000, tiempoRestante, 1000);
    }

    @Test
    void deberiaMoverHelado1EnDireccionValida() throws BadDopoException {
        juego.iniciarJuego();
        Helado helado = juego.getHelado1();
        int filaInicial = helado.getFila();
        
        juego.moverHelado1("DERECHA");

        assertEquals("DERECHA", helado.getUltimaDireccion());
    }

    @Test
    void noDeberiaMoverSiJuegoPausado() throws BadDopoException {
        juego.iniciarJuego();
        juego.pausar();
        
        assertThrows(BadDopoException.class, () -> {
            juego.moverHelado1("ABAJO");
        });
    }

    @Test
    void deberiaMoverHelado2EnModoMultijugador() throws BadDopoException {
        juegoMultijugador.iniciarJuego();
        Helado helado2 = juegoMultijugador.getHelado2();
        assertNotNull(helado2);
        
        juegoMultijugador.moverHelado2("IZQUIERDA");
        assertEquals("IZQUIERDA", helado2.getUltimaDireccion());
    }

    @Test
    void noDeberiaTenerHelado2EnModoUnJugador() throws BadDopoException {
        juego.iniciarJuego();
        assertNull(juego.getHelado2());
    }

    @Test
    void deberiaCrearHelado2EnModoJugadorVsJugador() throws BadDopoException {
        juegoMultijugador.iniciarJuego();
        assertNotNull(juegoMultijugador.getHelado2());
    }

    @Test
    void deberiaPausarJuegoCorrectamente() throws BadDopoException {
        juego.iniciarJuego();
        juego.pausar();
        assertEquals(true, juego.isPausado());
    }

    @Test
    void deberiaReanudarJuegoDespuesDePausa() throws BadDopoException {
        juego.iniciarJuego();
        juego.pausar();
        juego.reanudar();
        assertEquals(false, juego.isPausado());
    }

    @Test
    void noDeberiaPausarSiJuegoNoIniciado() {
        juego.pausar();
        assertEquals(false, juego.isPausado());
    }

    @Test
    void deberiaTenerTiempoMaximoAlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        long tiempo = juego.getTiempoRestante();
        assertEquals(180000, tiempo, 1000); // 180 segundos
    }

    @Test
    void deberiaRetornarTiempoMaximoSiNoIniciado() {
        long tiempo = juego.getTiempoRestante();
        assertEquals(180000, tiempo);
    }

    @Test
    void deberiaFormatearConCeros() throws BadDopoException {
        juego.iniciarJuego();
        String formato = juego.getTiempoRestanteFormato();
        assertEquals(5, formato.length()); // Formato MM:SS
    }

    @Test
    void deberiaIniciarEnFase0() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals(0, juego.getFaseActual());
    }

    @Test
    void deberiaTenerAlMenosUnaFase() throws BadDopoException {
        juego.iniciarJuego();
        int totalFases = juego.getTotalFases();
        assertEquals(true, totalFases >= 1);
    }

    @Test
    void deberiaIniciarConPuntajeCero() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals(0, juego.getPuntajeJugador1());
    }

    @Test
    void deberiaIniciarAmbosJugadoresConPuntajeCero() throws BadDopoException {
        juegoMultijugador.iniciarJuego();
        assertEquals(0, juegoMultijugador.getPuntajeJugador1());
        assertEquals(0, juegoMultijugador.getPuntajeJugador2());
    }

    @Test
    void deberiaRetornarJugador1ComoGanadorEnModoUnJugador() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals("Jugador 1", juego.getGanador());
    }

    @Test
    void deberiaRetornarEmpateConPuntajesIguales() throws BadDopoException {
        juegoMultijugador.iniciarJuego();
        assertEquals("Empate", juegoMultijugador.getGanador());
    }

    @Test
    void noDeberiaEstarCompletoAlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals(false, juego.isNivelCompletado());
    }

    @Test
    void noDeberiaEstarTerminadoAlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals(false, juego.isJuegoTerminado());
    }

    @Test
    void deberiaTerminarAlLlamarTerminarJuego() throws BadDopoException {
        juego.iniciarJuego();
        juego.terminarJuego();
        assertEquals(true, juego.isJuegoTerminado());
    }

    @Test
    void deberiaRetornarDimensionesValidas() throws BadDopoException {
        juego.iniciarJuego();
        int[] dimensiones = juego.getDimensionesTablero();
        assertEquals(2, dimensiones.length);
    }

    @Test
    void deberiaRetornarFilasPositivas() throws BadDopoException {
        juego.iniciarJuego();
        int[] dimensiones = juego.getDimensionesTablero();
        assertEquals(true, dimensiones[0] > 0);
    }

    @Test
    void deberiaRetornarColumnasPositivas() throws BadDopoException {
        juego.iniciarJuego();
        int[] dimensiones = juego.getDimensionesTablero();
        assertEquals(true, dimensiones[1] > 0);
    }

    @Test
    void deberiaRetornarTableroNoNulo() throws BadDopoException {
        juego.iniciarJuego();
        String[][] tablero = juego.getRepresentacionTablero();
        assertNotNull(tablero);
    }

    @Test
    void deberiaRetornarTableroConFilas() throws BadDopoException {
        juego.iniciarJuego();
        String[][] tablero = juego.getRepresentacionTablero();
        assertEquals(true, tablero.length > 0);
    }

    @Test
    void deberiaRetornarTableroConColumnas() throws BadDopoException {
        juego.iniciarJuego();
        String[][] tablero = juego.getRepresentacionTablero();
        assertEquals(true, tablero[0].length > 0);
    }

    @Test
    void deberiaRetornarListaFrutasNoNula() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getFrutasEnJuego());
    }

    @Test
    void deberiaTenerFrutasAlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        int cantidadFrutas = juego.getFrutasEnJuego().size();
        assertEquals(true, cantidadFrutas > 0);
    }

    @Test
    void deberiaRetornarListaEnemigosNoNula() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getEnemigosEnJuego());
    }
    @Test
    void deberiaRetornarModoUnJugador() {
        assertEquals("Un jugador", juego.getModo());
    }

    @Test
    void deberiaRetornarModoJugadorVsJugador() {
        assertEquals("Jugador vs Jugador", juegoMultijugador.getModo());
    }


    @Test
    void noDeberiaSerUltimoNivelEnNivel1() throws BadDopoException {
        juego.iniciarJuego();
        assertEquals(false, juego.esUltimoNivel());
    }

    @Test
    void deberiaSerUltimoNivelEnNivel2() throws BadDopoException {
        BadDopoCream juegoNivel2 = new BadDopoCream(2, "Un jugador", "VH", null,
                                                     "Test", null, null, null);
        juegoNivel2.iniciarJuego();
        assertEquals(true, juegoNivel2.esUltimoNivel());
    }

    @Test
    void deberiaRetornarIndice0ParaNivel1() {
        assertEquals(0, juego.getNivelActual());
    }

    @Test
    void deberiaRetornarIndice1ParaNivel2() throws BadDopoException {
        BadDopoCream juegoNivel2 = new BadDopoCream(2, "Un jugador", "VH", null,
                                                     "Test", null, null, null);
        assertEquals(1, juegoNivel2.getNivelActual());
    }

    @Test
    void deberiaRetornarSaborVainillaParaJugador1() {
        assertEquals("VH", juego.getSabor1());
    }

    @Test
    void deberiaRetornarSaborChocolateParaJugador2() {
        assertEquals("CH", juegoMultijugador.getSabor2());
    }

    @Test
    void deberiaRetornarNullParaSabor2EnModoUnJugador() {
        assertNull(juego.getSabor2());
    }

    @Test
    void deberiaRetornarMapaFrutasRequeridasNoNulo() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getFrutasRequeridas());
    }

    @Test
    void deberiaRetornarMapaFrutasRecolectadasNoNulo() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getFrutasRecolectadas());
    }

    @Test
    void deberiaIniciarConCeroFrutasRecolectadas() throws BadDopoException {
        juego.iniciarJuego();
        var frutasRecolectadas = juego.getFrutasRecolectadas();
        int total = frutasRecolectadas.values().stream().mapToInt(Integer::intValue).sum();
        assertEquals(0, total);
    }

    @Test
    void deberiaRetornarFormatoCorrectoProgresoFrutas() throws BadDopoException {
        juego.iniciarJuego();
        String progreso = juego.getProgresoFrutas("UVA");
        assertEquals(true, progreso.contains("/"));
    }

    @Test
    void deberiaExistirNivel0() {
        assertEquals(true, BadDopoCream.existeNivel(0));
    }

    @Test
    void deberiaExistirNivel1() {
        assertEquals(true, BadDopoCream.existeNivel(1));
    }

    @Test
    void noDeberiaExistirNivel5() {
        assertEquals(false, BadDopoCream.existeNivel(5));
    }

    @Test
    void deberiaTenerMensajeEstadoAlIniciar() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getMensajeEstado());
    }

    @Test
    void deberiaTenerMensajeInicialCorrecto() throws BadDopoException {
        juego.iniciarJuego();
        String mensaje = juego.getMensajeEstado();
        assertEquals(true, mensaje.contains("iniciado") || mensaje.contains("Fase"));
    }

    @Test
    void deberiaRetornarEstadisticasNoNulas() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getEstadisticas());
    }

    @Test
    void deberiaIncluirModoEnEstadisticas() throws BadDopoException {
        juego.iniciarJuego();
        var stats = juego.getEstadisticas();
        assertEquals("Un jugador", stats.get("modo"));
    }

    @Test
    void deberiaIncluirNivelEnEstadisticas() throws BadDopoException {
        juego.iniciarJuego();
        var stats = juego.getEstadisticas();
        assertEquals(0, stats.get("nivelActual"));
    }

    @Test
    void deberiaRetornarResumenNoNulo() throws BadDopoException {
        juego.iniciarJuego();
        assertNotNull(juego.getResumenFinal());
    }

    @Test
    void deberiaIncluirModoEnResumen() throws BadDopoException {
        juego.iniciarJuego();
        String resumen = juego.getResumenFinal();
        assertEquals(true, resumen.contains("Modo"));
    }

    @Test
    void deberiaIncluirPuntuacionEnResumen() throws BadDopoException {
        juego.iniciarJuego();
        String resumen = juego.getResumenFinal();
        assertEquals(true, resumen.contains("PUNTUACIÓN"));
    }
}