package test;

import domain.*;
import org.junit.jupiter.api.Test;
import javax.swing.JOptionPane;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.JOptionPane;

public class BadDopoCreamAcceptanceTest {

    @Test
    public void testAceptacion() throws BadDopoException {

        BadDopoCream juego = new BadDopoCream(
                1, "Un jugador", "VH", null,
                "Solo", null, null, null
        );

        juego.iniciarJuego();

        assertNotNull(juego.getHelado1());
        assertNull(juego.getHelado2());
        assertEquals("Un jugador", juego.getModoJuego());
    }

}
