package test;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de prueba para Helado
 */
public class HeladoTest {
    
    private Tablero tablero;
    private GrafoTablero grafo;
    private Helado helado;
    
    @Before
    public void setUp() throws BadDopoException {
        // Configuración inicial antes de cada prueba
        tablero = new Tablero(10, 10, true);
        grafo = new GrafoTablero(tablero);
    }

    @Test
    public void testConstructorValido() throws BadDopoException {
        helado = new Helado("Chocolate");
        assertNotNull("El helado no debería ser nulo", helado);
        assertEquals("El sabor debería ser Chocolate", "Chocolate", helado.getSabor());
        assertEquals("El puntaje inicial debería ser 0", 0, helado.getPuntaje());
    }
    
    @Test(expected = BadDopoException.class)
    public void testConstructorSaborNulo() throws BadDopoException {
        helado = new Helado(null);
    }
    
    @Test(expected = BadDopoException.class)
    public void testConstructorSaborVacio() throws BadDopoException {
        helado = new Helado("");
    }
    
    @Test(expected = BadDopoException.class)
    public void testConstructorSaborSoloEspacios() throws BadDopoException {
        helado = new Helado("   ");
    }

    @Test
    public void testSetTableroValido() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setTablero(tablero);
        assertNotNull("El tablero no debería ser nulo", helado.getTablero());
    }
    
    @Test(expected = BadDopoException.class)
    public void testSetTableroNulo() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setTablero(null);
    }
    
    @Test
    public void testSetGrafoValido() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setGrafo(grafo);
        assertNotNull("El grafo no debería ser nulo", helado.getGrafo());
    }
    
    @Test(expected = BadDopoException.class)
    public void testSetGrafoNulo() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setGrafo(null);
    }
    
    @Test
    public void testSetPosicionInicialValida() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setTablero(tablero);
        helado.setPosicionInicial(5, 5);
        
        assertEquals("La fila debería ser 5", 5, helado.getFila());
        assertEquals("La columna debería ser 5", 5, helado.getColumna());
    }
    
    @Test(expected = BadDopoException.class)
    public void testSetPosicionSinTablero() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setPosicionInicial(5, 5); // Debería fallar porque no hay tablero
    }
    
    @Test(expected = BadDopoException.class)
    public void testSetPosicionFueraDeRango() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setTablero(tablero);
        helado.setPosicionInicial(20, 20); // Fuera del tablero 10x10
    }
    
    @Test(expected = BadDopoException.class)
    public void testSetPosicionNegativa() throws BadDopoException {
        helado = new Helado("Vainilla");
        helado.setTablero(tablero);
        helado.setPosicionInicial(-1, 5);
    }

    @Test
    public void testMoverDerecha() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("DERECHA");
        
        assertEquals("Debería estar en columna 6", 6, helado.getColumna());
        assertEquals("La fila no debería cambiar", 5, helado.getFila());
    }
    
    @Test
    public void testMoverIzquierda() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("IZQUIERDA");
        
        assertEquals("Debería estar en columna 4", 4, helado.getColumna());
        assertEquals("La fila no debería cambiar", 5, helado.getFila());
    }
    
    @Test
    public void testMoverArriba() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("ARRIBA");
        
        assertEquals("Debería estar en fila 4", 4, helado.getFila());
        assertEquals("La columna no debería cambiar", 5, helado.getColumna());
    }
    
    @Test
    public void testMoverAbajo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("ABAJO");
        
        assertEquals("Debería estar en fila 6", 6, helado.getFila());
        assertEquals("La columna no debería cambiar", 5, helado.getColumna());
    }
    
    @Test
    public void testMoverVariasVeces() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("ARRIBA");  // (4, 5)
        helado.mover("DERECHA"); // (4, 6)
        helado.mover("ABAJO");   // (5, 6)
        
        assertEquals("Debería estar en fila 5", 5, helado.getFila());
        assertEquals("Debería estar en columna 6", 6, helado.getColumna());
    }
    
    @Test
    public void testMoverEnIngles() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("UP");
        assertEquals("Debería moverse arriba", 4, helado.getFila());
        
        helado.mover("RIGHT");
        assertEquals("Debería moverse a la derecha", 6, helado.getColumna());
        
        helado.mover("DOWN");
        assertEquals("Debería moverse abajo", 5, helado.getFila());
        
        helado.mover("LEFT");
        assertEquals("Debería moverse a la izquierda", 5, helado.getColumna());
    }
    
    @Test(expected = BadDopoException.class)
    public void testMoverSinConfigurar() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.mover("ARRIBA"); // Debería fallar porque no está configurado
    }
    
    @Test(expected = BadDopoException.class)
    public void testMoverDireccionInvalida() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover("DIAGONAL"); // Dirección no válida
    }
    
    @Test(expected = BadDopoException.class)
    public void testMoverDireccionNula() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.mover(null);
    }
    
    @Test(expected = BadDopoException.class)
    public void testMoverContraBorde() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(1, 1);
        
        helado.mover("ARRIBA"); // Intentar moverse contra el borde superior (BORDE)
    }

    @Test
    public void testComerBanano() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        Celda celda = tablero.getCelda(5, 5);
        Banano banano = new Banano(5, 5, 50, celda); // ✅ Constructor correcto con ganancia
        
        int puntajeAntes = helado.getPuntaje();
        helado.comerFruta(banano);
        
        assertEquals("El puntaje debería aumentar en 50", puntajeAntes + 50, helado.getPuntaje());
    }
    
    @Test
    public void testComerUva() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        Celda celda = tablero.getCelda(5, 5);
        Uva uva = new Uva(5, 5, 30, celda); // ✅ Constructor con ganancia
        
        int puntajeAntes = helado.getPuntaje();
        helado.comerFruta(uva);
        
        assertEquals("El puntaje debería aumentar en 30", puntajeAntes + 30, helado.getPuntaje());
    }
    
    @Test
    public void testComerVariasFrutas() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        Celda celda1 = tablero.getCelda(5, 5);
        Banano banano = new Banano(5, 5, 50, celda1);
        helado.comerFruta(banano);
        // Mover a otra posición
        helado.mover("DERECHA"); // (5, 6)
        
        Celda celda2 = tablero.getCelda(5, 6);
        Uva uva = new Uva(5, 6, 30, celda2);
        helado.comerFruta(uva);
        
        assertEquals("El puntaje total debería ser 80", 80, helado.getPuntaje());
    }
    
    @Test(expected = BadDopoException.class)
    public void testComerFrutaNula() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.comerFruta(null);
    }
    
    @Test(expected = BadDopoException.class)
    public void testComerFrutaFueraDeAlcance() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        Celda celda = tablero.getCelda(7, 7);
        Banano banano = new Banano(7, 7, 50, celda);
        
        helado.comerFruta(banano); // Debería fallar porque la fruta está lejos
    }

    @Test
    public void testCrearHielo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.crearHielo();
        
        Celda celda = tablero.getCelda(5, 5);
        assertEquals("La celda debería ser de tipo HIELO", TipoCelda.HIELO, celda.getTipo());
    }
    
    @Test
    public void testPoderCreaHielo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.poder(); // Debería llamar a crearHielo()
        
        Celda celda = tablero.getCelda(5, 5);
        assertEquals("La celda debería ser de tipo HIELO", TipoCelda.HIELO, celda.getTipo());
    }
    
    @Test
    public void testRomperHielo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        // Crear hielo alrededor
        tablero.setCelda(4, 5, TipoCelda.HIELO); // Arriba
        tablero.setCelda(6, 5, TipoCelda.HIELO); // Abajo
        tablero.setCelda(5, 4, TipoCelda.HIELO); // Izquierda
        tablero.setCelda(5, 6, TipoCelda.HIELO); // Derecha
        
        helado.romperHielo();
        
        // Verificar que se rompió el hielo
        Celda celdaArriba = tablero.getCelda(4, 5);
        Celda celdaAbajo = tablero.getCelda(6, 5);
        Celda celdaIzq = tablero.getCelda(5, 4);
        Celda celdaDer = tablero.getCelda(5, 6);
        
        assertEquals("El hielo arriba debería estar roto", TipoCelda.VACIA, celdaArriba.getTipo());
        assertEquals("El hielo abajo debería estar roto", TipoCelda.VACIA, celdaAbajo.getTipo());
        assertEquals("El hielo izquierda debería estar roto", TipoCelda.VACIA, celdaIzq.getTipo());
        assertEquals("El hielo derecha debería estar roto", TipoCelda.VACIA, celdaDer.getTipo());
    }
    
    @Test(expected = BadDopoException.class)
    public void testRomperHieloSinHieloAdyacente() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        helado.romperHielo(); // Debería fallar porque no hay hielo para romper
    }
    
    @Test(expected = BadDopoException.class)
    public void testCrearHieloEnCeldaNoVacia() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setGrafo(grafo);
        helado.setPosicionInicial(5, 5);
        
        // Primero crear hielo
        helado.crearHielo();
        
        // Intentar crear hielo nuevamente en la misma celda (ya tiene hielo)
        helado.crearHielo(); // Debería fallar
    }

    @Test
    public void testPuntajeInicial() throws BadDopoException {
        helado = new Helado("Chocolate");
        assertEquals("El puntaje inicial debería ser 0", 0, helado.getPuntaje());
    }
    
    @Test
    public void testSetPuntajeValido() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setPuntaje(100);
        
        assertEquals("El puntaje debería ser 100", 100, helado.getPuntaje());
    }
    
    @Test(expected = BadDopoException.class)
    public void testSetPuntajeNegativo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setPuntaje(-10);
    }

    @Test
    public void testCambiarSabor() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.cambiarSabor("Fresa");
        
        assertEquals("El sabor debería ser Fresa", "Fresa", helado.getSabor());
    }
    
    @Test
    public void testSetSabor() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setSabor("Menta");
        
        assertEquals("El sabor debería ser Menta", "Menta", helado.getSabor());
    }
    
    @Test(expected = BadDopoException.class)
    public void testCambiarSaborNulo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.cambiarSabor(null);
    }
    
    @Test(expected = BadDopoException.class)
    public void testCambiarSaborVacio() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.cambiarSabor("");
    }

    @Test
    public void testEscogerNivelResetPuntaje() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setPuntaje(50);
        helado.escogerNivel(2);
        
        assertEquals("El puntaje debería resetearse a 0", 0, helado.getPuntaje());
    }
    
    @Test(expected = BadDopoException.class)
    public void testEscogerNivelCero() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.escogerNivel(0); // Nivel inválido
    }
    
    @Test(expected = BadDopoException.class)
    public void testEscogerNivelNegativo() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.escogerNivel(-1);
    }

    @Test
    public void testGetPosicion() throws BadDopoException {
        helado = new Helado("Chocolate");
        helado.setTablero(tablero);
        helado.setPosicionInicial(5, 5);
        
        assertEquals("La posición debería tener 2 elementos", 2, helado.getPosicion().size());
        assertEquals("La fila en posición debería ser 5", Integer.valueOf(5), helado.getPosicion().get(0));
        assertEquals("La columna en posición debería ser 5", Integer.valueOf(5), helado.getPosicion().get(1));
    }
}
