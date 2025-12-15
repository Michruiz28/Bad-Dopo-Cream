package domain;

import java.util.List;

/**
 * Estrategia perseguidor
 */
public class EstrategiaPerseguidor implements MovimientoEnemigoStrategy {
    @Override
    public void ejecutarTurno(Enemigo enemigo, VistaTablero vista, Helado jugador, GrafoTablero grafo) throws BadDopoException {
        String direccion = vista.calcularDireccionHaciaObjetivo(
            enemigo.getFila(), enemigo.getColumna(),
            jugador.getFila(), jugador.getColumna(),
            false
        );

        if (direccion != null) {
            enemigo.setUltimaDireccion(direccion);
            grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), direccion);
            return;
        }

        List<String> validas = vista.obtenerDireccionesValidas(enemigo.getFila(), enemigo.getColumna());
        if (!validas.isEmpty()) {
            String aleatoria = validas.get(new java.util.Random().nextInt(validas.size()));
            enemigo.setUltimaDireccion(aleatoria);
            grafo.solicitarMovimientoHacia(enemigo.getFila(), enemigo.getColumna(), aleatoria);
        }
    }
}
