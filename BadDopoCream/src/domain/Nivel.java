 
package domain;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un nivel específico del juego Bad Dopo Cream.
 * Contiene el tablero, las entidades del nivel y la lógica de actualización.
 */
public class Nivel {

    private int nivel;                    
    private Tablero tablero; 
    private GrafoTablero grafo;

    private List<Fruta> frutas;          
    private List<Enemigo> enemigos;       
    private List<Obstaculo> obstaculos;  

    private List<Jugador> jugadores;     

    private boolean completado;           // El nivel se completa cuando no hay frutas

    /**
     * Constructor del nivel.
     * @param nivel Número de nivel que se está creando.
     * @throws BadDopoException Si el número es inválido.
     */
    public Nivel(int nivel) throws BadDopoException {
        if (nivel <= 0) {
            throw new BadDopoException("El número del nivel debe ser mayor a 0");
        }

        this.nivel = nivel;
        this.tablero = new Tablero(13, 13, true);   // Ajusta según el tamaño real del mapa

        this.frutas = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.obstaculos = new ArrayList<>();
        this.jugadores = new ArrayList<>();

        this.completado = false;
    }
    
    public int getNumeroNivel() {
        return nivel;
    }

    public Tablero getTablero() {
        return tablero;
    }
    public GrafoTablero getGrafo(){
        return grafo;
    }

    public boolean estaCompletado() {
        return completado;
    }

    public List<Fruta> getFrutas() {
        return frutas;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public List<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public void agregarJugador(Jugador jugador) throws BadDopoException {
        if (jugadores.size() >= 2) {
            throw new BadDopoException("Un nivel no puede tener más de dos jugadores.");
        }
        jugadores.add(jugador);
    }

    public void agregarFruta(Fruta fruta) {
        frutas.add(fruta);
    }

    public void agregarEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
    }

    public void agregarObstaculo(Obstaculo obstaculo) {
        obstaculos.add(obstaculo);
    }
    /**
     * Este método se llama en cada ciclo de actualización.
     * Controla movimientos, colisiones y fin del nivel.
     */
    public void actualizar() throws BadDopoException {

        if (completado) return;

        // Movimiento de jugadores 
        for (Jugador jugador : jugadores) {
            jugador.realizarMovimiento(this);
        }

        // Movimiento de enemigos
        for (Enemigo e : enemigos) {
            e.realizarMovimiento(this);
        }
        detectarFrutasRecolectadas();
        verificarCompletado();
    }
    /**
     * Detecta si algún jugador ha recolectado frutas.
     * Si un jugador está en la misma posición que una fruta, la recolecta y gana puntos.
     */

    private void detectarFrutasRecolectadas() throws BadDopoException {

        List<Fruta> frutasRecolectadas = new ArrayList<>();

        for (Jugador jugador : jugadores) {
            Helado h = jugador.getHelado();
            int hf = h.getFila();
            int hc = h.getColumna();

            for (Fruta fruta : frutas) {
                if (fruta.getFila() == hf && fruta.getColumna() == hc) {
                    jugador.sumarPuntos(fruta.getGANANCIA());
                    frutasRecolectadas.add(fruta);
                }
            }
        }

        frutas.removeAll(frutasRecolectadas);
    }
    /**
     * El nivel se completa cuando ya no quedan frutas.
     */
    private void verificarCompletado() {
        if (frutas.isEmpty()) {
            completado = true;
        }
    }
    public boolean hayObstaculo(int fila, int columna) {
        for (Obstaculo o : obstaculos) {
            if (o.getFila() == fila && o.getColumna() == columna) return true;
        }
        return false;
    }
    
    public void inicializar() throws BadDopoException {
        if (tablero == null)
        throw new BadDopoException("El tablero no está inicializado.");
        // Asignar tablero y grafo a cada helado
        this.grafo = new GrafoTablero(tablero);
        for (Jugador j : jugadores) {
            Helado h = j.getHelado();
            h.setTablero(tablero);
            h.setGrafo(grafo);
        }
    }
    
    /**
    * Verifica colisiones del nivel:
    * - Helado tocado por enemigo
    * - Helado en cactus con púas 
    */
   public void verificarColisiones() throws BadDopoException {
       for (Jugador j : jugadores) {
           Helado h = j.getHelado();
           int f = h.getFila();
           int c = h.getColumna();
           // 1) Enemigos (muerte)
           for (Enemigo e : enemigos) {
               if (e.getFila() == f && e.getColumna() == c) {
                   throw new BadDopoException("El helado fue atrapado por un enemigo.");
               }
           }
           // 2) Cactus con púas
           for (Fruta fruta : frutas) {
               if (fruta instanceof Cactus cactus) {
                   if (cactus.tienePuas() && cactus.getFila() == f && cactus.getColumna() == c) {
                       throw new BadDopoException("El helado murió al tocar un cactus con púas.");
                   }
               }
           }
           //falta agregar en caso de colisionar con una fogata
       }
   }
   /** Devuelve la cantidad de frutas restantes en el nivel 
   */
   public int frutasRestantes() {
       return frutas.size();
   }
   
   /** Actualizar frutas del propio nivel (usa los métodos concretos que ya implementaste) 
   */
   public void actualizarFrutas() throws BadDopoException {
    List<Fruta> copia = new ArrayList<>(frutas);
    long ahora = System.currentTimeMillis();
    for (Fruta f : copia) {
        if (f instanceof Cereza cereza) {
            cereza.actualizar(this);
        }
        if (f instanceof Cactus cactus) {
            cactus.actualizarEstado();
        }
    }
  }
}
