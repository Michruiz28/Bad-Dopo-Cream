package domain;

import java.util.List;


public class EstrategiaPerseguidor implements MovimientoEnemigoStrategy {
    /**
     * Ejecuta un turno de movimiento para un enemigo que persigue al jugador.
     *
     * @param enemigo enemigo que ejecuta la estrategia
     * @param vista    interfaz para consultar direcciones y posiciones
     * @param jugador  referencia al helado jugador (objetivo)
     * @param grafo    grafo del tablero para solicitar movimientos y comprobaciones
     * @throws BadDopoException si ocurre un error de juego durante la ejecución
     */
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
