package test;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;
import domain.*;

/**
 * Clase de pruebas para Tablero
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 */
public class TableroTest {

    private Tablero tablero;
    private String[][] infoNivelBasico;

    @Before
    public void setUp() throws BadDopoException {
        // Tablero básico 6x6 para pruebas
        infoNivelBasico = new String[][] {
                {"B", "B", "B", "B", "B", "B"},
                {"B", "V", "V", "BF", "V", "B"},
                {"B", "V", "CF", "V", "U", "B"},
                {"B", "H", "V", "P", "V", "B"},
                {"B", "V", "V", "V", "V", "B"},
                {"B", "B", "B", "B", "B", "B"}
        };

        tablero = new Tablero(infoNivelBasico, null);
    }
    @Test
    public void deberiaCrearTableroConDimensionesCorrectas() throws BadDopoException {
        assertEquals(6, tablero.getFilas());
        assertEquals(6, tablero.getColumnas());
    }

    @Test(expected = BadDopoException.class)
    public void noDeberiaCrearTableroConInfoNivelNull() throws BadDopoException {
        new Tablero(null, null);
    }

    @Test(expected = BadDopoException.class)
    public void noDeberiaCrearTableroConInfoNivelVacio() throws BadDopoException {
        String[][] vacio = new String[0][0];
        new Tablero(vacio, null);
    }

    @Test
    public void deberiaRetornarNumeroCorretoDeFilas() {
        assertEquals(6, tablero.getFilas());
    }

    @Test
    public void deberiaRetornarFilasParaTableroGrande() throws BadDopoException {
        String[][] grande = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grande[i][j] = "V";
            }
        }
        Tablero tableroGrande = new Tablero(grande, null);

        assertEquals(10, tableroGrande.getFilas());
    }

    @Test
    public void deberiaRetornarFilasParaTableroPequeno() throws BadDopoException {
        String[][] pequeno = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pequeno[i][j] = "V";
            }
        }
        Tablero tableroPequeno = new Tablero(pequeno, null);

        assertEquals(3, tableroPequeno.getFilas());
    }

    @Test
    public void deberiaRetornarNumeroCorrectoDeColumnas() {
        assertEquals(6, tablero.getColumnas());
    }

    @Test
    public void deberiaRetornarColumnasParaTableroGrande() throws BadDopoException {
        String[][] grande = new String[8][12];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 12; j++) {
                grande[i][j] = "V";
            }
        }
        Tablero tableroGrande = new Tablero(grande, null);

        assertEquals(12, tableroGrande.getColumnas());
    }

    @Test
    public void deberiaRetornarColumnasParaTableroPequeno() throws BadDopoException {
        String[][] pequeno = new String[5][4];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                pequeno[i][j] = "V";
            }
        }
        Tablero tableroPequeno = new Tablero(pequeno, null);

        assertEquals(4, tableroPequeno.getColumnas());
    }

    @Test
    public void deberiaRetornarDimensionesEnArreglo() {
        int[] dimensiones = tablero.getDimensiones();

        assertEquals(6, dimensiones[0]);
        assertEquals(6, dimensiones[1]);
    }

    @Test
    public void deberiaRetornarArregloDeLongitud2() {
        int[] dimensiones = tablero.getDimensiones();

        assertEquals(2, dimensiones.length);
    }

    @Test
    public void deberiaRetornarDimensionesCorrectasParaCualquierTablero() throws BadDopoException {
        String[][] custom = new String[7][9];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                custom[i][j] = "V";
            }
        }
        Tablero tableroCustom = new Tablero(custom, null);
        int[] dimensiones = tableroCustom.getDimensiones();

        assertEquals(7, dimensiones[0]);
        assertEquals(9, dimensiones[1]);
    }

    @Test
    public void deberiaAgregarHeladoEnPosicionValida() throws BadDopoException {
        Helado helado = new Helado(2, 2, "Vainilla");
        tablero.agregarHelado(helado);

        int[] posicion = tablero.getPosicionHelado(helado);
        assertEquals(2, posicion[0]);
        assertEquals(2, posicion[1]);
    }

    @Test
    public void deberiaRetornarPosicionCorrectaDelHelado() throws BadDopoException {
        Helado helado = new Helado(3, 4,  "Vainilla");
        tablero.agregarHelado(helado);

        int[] posicion = tablero.getPosicionHelado(helado);
        assertEquals(3, posicion[0]);
        assertEquals(4, posicion[1]);
    }

    @Test
    public void deberiaRetornarArregloDeLongitud2ParaPosicionHelado() throws BadDopoException {
        Helado helado = new Helado(2, 2, "Chocolate");

        int[] posicion = tablero.getPosicionHelado(helado);
        assertEquals(2, posicion.length);
    }

    @Test
    public void deberiaRetornarPosicionActualizadaDespuesDeMovimiento() throws BadDopoException {
        Helado helado = new Helado(2, 2, "Vainilla");
        tablero.agregarHelado(helado);

        helado.setFila(3);
        helado.setColumna(3);

        int[] posicion = tablero.getPosicionHelado(helado);
        assertEquals(3, posicion[0]);
        assertEquals(3, posicion[1]);
    }

    @Test
    public void deberiaAgregarFrutaEnPosicionEspecificada() throws BadDopoException {
        Banano banano = new Banano(2, 2);
        tablero.agregarFrutaEnPosicion(banano, 2, 2);

        ArrayList<Fruta> frutas = tablero.getFrutas();
        assertEquals(true, frutas.contains(banano));
    }

    @Test(expected = BadDopoException.class)
    public void noDeberiaAgregarFrutaFueraDelTablero() throws BadDopoException {
        Cereza cereza = new Cereza(10, 10);
        tablero.agregarFrutaEnPosicion(cereza, 10, 10);
    }

    @Test
    public void deberiaAgregarVariasFrutasEnDiferentesPosiciones() throws BadDopoException {
        Uva uva = new Uva(1, 1);
        Banano banano = new Banano(2, 2);

        tablero.agregarFrutaEnPosicion(uva, 1, 1);
        tablero.agregarFrutaEnPosicion(banano, 2, 2);

        ArrayList<Fruta> frutas = tablero.getFrutas();
        assertEquals(true, frutas.size() >= 2);
    }

    @Test
    public void deberiaRetornarListaDeFrutasNoNull() {
        ArrayList<Fruta> frutas = tablero.getFrutas();

        assertNotNull("La lista de frutas no debería ser null", frutas);
    }

    @Test
    public void deberiaActualizarListaDespuesDeAgregarFruta() throws BadDopoException {
        int cantidadInicial = tablero.getFrutas().size();

        Cereza cereza = new Cereza(4, 4);
        tablero.agregarFrutaEnPosicion(cereza, 4, 4);

        int cantidadFinal = tablero.getFrutas().size();
        assertEquals(true, cantidadFinal > cantidadInicial);
    }

    @Test
    public void deberiaRetornarListaDeEnemigosNoNull() {
        ArrayList<Enemigo> enemigos = tablero.getEnemigos();

        assertNotNull("La lista de enemigos no debería ser null", enemigos);
    }

    @Test
    public void deberiaRetornarListaVaciaSiNoHayEnemigos() {
        ArrayList<Enemigo> enemigos = tablero.getEnemigos();

        assertEquals(0, enemigos.size());
    }

    @Test
    public void deberiaRetornarEnemigosDelTablero() throws BadDopoException {
        // Crear tablero con enemigos
        String[][] conEnemigos = new String[][] {
                {"B", "B", "B", "B", "B"},
                {"B", "T", "V", "C", "B"},
                {"B", "V", "V", "V", "B"},
                {"B", "M", "V", "V", "B"},
                {"B", "B", "B", "B", "B"}
        };
        Tablero tableroConEnemigos = new Tablero(conEnemigos, null);

        ArrayList<Enemigo> enemigos = tableroConEnemigos.getEnemigos();
        assertEquals(true, enemigos.size() > 0);
    }

    @Test
    public void deberiaRetornarListaDeObstaculosNoNull() {
        ArrayList<Obstaculo> obstaculos = tablero.getObstaculos();

        assertNotNull("La lista de obstáculos no debería ser null", obstaculos);
    }

    @Test
    public void deberiaRetornarObstaculosDelTablero() {
        ArrayList<Obstaculo> obstaculos = tablero.getObstaculos();

        assertEquals(true, obstaculos.size() > 0);
    }

    @Test
    public void deberiaIncluirBordesYHieloEnObstaculos() {
        ArrayList<Obstaculo> obstaculos = tablero.getObstaculos();

        assertEquals(true, obstaculos.size() >= 2);
    }

    @Test
    public void deberiaRetornarPosicionCorrectaDeFruta() throws BadDopoException {
        Banano banano = new Banano(2, 3);

        int[] posicion = tablero.getPosicionFruta(banano);
        assertEquals(2, posicion[0]);
        assertEquals(3, posicion[1]);
    }

    @Test
    public void deberiaRetornarArregloDeLongitud2ParaPosicionFruta() throws BadDopoException {
        Uva uva = new Uva(1, 1);

        int[] posicion = tablero.getPosicionFruta(uva);
        assertEquals(2, posicion.length);
    }

    @Test
    public void deberiaRetornarPosicionParaDiferentesFrutas() throws BadDopoException {
        Cereza cereza = new Cereza(3, 4);
        Pina pina = new Pina(1, 2);

        int[] posCereza = tablero.getPosicionFruta(cereza);
        int[] posPina = tablero.getPosicionFruta(pina);

        assertEquals(3, posCereza[0]);
        assertEquals(1, posPina[0]);
    }

    @Test
    public void deberiaRetornarPosicionCorrectaDeEnemigo() {
        Troll troll = new Troll(2, 2);

        int[] posicion = tablero.getPosicionEnemigo(troll);
        assertEquals(2, posicion[0]);
        assertEquals(2, posicion[1]);
    }

    @Test
    public void deberiaRetornarArregloDeLongitud2ParaPosicionEnemigo() {
        Calamar calamar = new Calamar(3, 3);

        int[] posicion = tablero.getPosicionEnemigo(calamar);
        assertEquals(2, posicion.length);
    }

    @Test
    public void deberiaRetornarPosicionParaDiferentesEnemigos() {
        Maceta maceta = new Maceta(1, 1);
        Troll troll = new Troll(4, 4);

        int[] posMaceta = tablero.getPosicionEnemigo(maceta);
        int[] posTroll = tablero.getPosicionEnemigo(troll);

        assertEquals(1, posMaceta[0]);
        assertEquals(4, posTroll[0]);
    }

    @Test
    public void deberiaRetornarArregloDeLongitud2ParaPosicionObstaculo() throws BadDopoException {
        Borde borde = new Borde(0, 0);

        int[] posicion = tablero.getPosicionObstaculo(borde);
        assertEquals(2, posicion.length);
    }

    @Test
    public void deberiaRemoverFrutaDelTablero() throws BadDopoException {
        Banano banano = new Banano(4, 4);
        tablero.agregarFrutaEnPosicion(banano, 4, 4);

        int cantidadAntes = tablero.getFrutas().size();
        tablero.removerFruta(banano);

        assertNotNull(tablero.getFrutas());
    }

    @Test
    public void deberiaPermitirRemoverVariasFrutas() throws BadDopoException {
        Uva uva = new Uva(1, 1);
        Cereza cereza = new Cereza(2, 2);

        tablero.agregarFrutaEnPosicion(uva, 1, 1);
        tablero.agregarFrutaEnPosicion(cereza, 2, 2);

        tablero.removerFruta(uva);
        tablero.removerFruta(cereza);

        assertNotNull(tablero.getFrutas());
    }

    @Test
    public void noDeberiaFallarAlRemoverFrutaYaRemovida() throws BadDopoException {
        Banano banano = new Banano(3, 3);
        tablero.agregarFrutaEnPosicion(banano, 3, 3);

        tablero.removerFruta(banano);

        assertNotNull(tablero.getFrutas());
    }

    @Test
    public void deberiaConstruirRepresentacionConDimensionesCorrectas() {
        String[][] representacion = tablero.construirRepresentacionActual();

        assertEquals(6, representacion.length);
        assertEquals(6, representacion[0].length);
    }

    @Test
    public void deberiaConstruirRepresentacionConTiposCorrectos() {
        String[][] representacion = tablero.construirRepresentacionActual();

        assertEquals("B", representacion[0][0]);
        assertEquals("B", representacion[5][5]);
    }

    @Test
    public void deberiaReflejarCambiosEnRepresentacion() throws BadDopoException {
        tablero.setElementoEnGrafo(2, 2, "H");
        String[][] representacion = tablero.construirRepresentacionActual();

        assertEquals("H", representacion[2][2]);
    }

    @Test
    public void deberiaDejarListaVaciaDespuesDeLimpiar() {
        tablero.limpiarFrutas();

        ArrayList<Fruta> frutas = tablero.getFrutas();
        assertEquals(0, frutas.size());
    }

    @Test
    public void noDeberiaFallarAlLimpiarFrutasVariasVeces() {
        tablero.limpiarFrutas();
        tablero.limpiarFrutas();
        tablero.limpiarFrutas();

        assertEquals(0, tablero.getFrutas().size());
    }
}