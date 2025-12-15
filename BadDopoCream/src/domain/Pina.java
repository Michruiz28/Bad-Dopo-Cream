 
package domain;
 
 /**
  * Clase que representa una Pina en el juego Bad Dopo Cream.
  * Es una fruta en movimiento que se desplaza automáticamente por el tablero.
  */
public class Pina extends FrutaEnMovimiento {
   public static final int GANANCIA_PINA = 200;
   private long ultimoTeletransporte;
   private java.util.Random random = new java.util.Random();
   private static final String imagen = "src/presentation/images/Piña.png";
   private static final String codigo = "P";

   private int fila;
   private int columna;

   public Pina(int fila, int columna) throws BadDopoException {
      super(fila, columna);
      this.ultimoTeletransporte = System.currentTimeMillis();
      this.fila = fila;
      this.columna = columna;
   }

   @Override
   public String getCodigo() {
      return codigo;
   }

   @Override
   public void actualizar(long timpoActual) throws BadDopoException {
      // No necesita actualización por cada frame; teletransporte está gestionado por el Grafo/Tablero
   }

   @Override
   public void aumentarPuntaje(int puntaje) {
      // No aplica
   }

   @Override
   public int getGanancia() {
      return GANANCIA_PINA;
   }

   @Override
   public void actualizarImagen(String ultimaDireccion) {
      // No aplica
   }

   @Override
   public void romperHielo(Celda celdaARomper, CreadorElemento creador) throws BadDopoException {
      // No aplica
   }

   @Override
   public void crearHielo(Celda celdaACrear, CreadorElemento creador) throws BadDopoException {
      // No aplica
   }

   @Override
   public void mover(String direccion) throws BadDopoException {
      // No aplica para piña (teletransporte en vez de movimiento por dirección)
   }

   @Override
   public void moverConPosicion(int filaNueva, int columnaNueva) {
      this.fila = filaNueva;
      this.columna = columnaNueva;
      setFila(filaNueva);
      setColumna(columnaNueva);
   }

   @Override
   public int[] calcularPosicionesMovimieto(int limiteInferior, int limiteSuperior) {
      return new int[]{getFila(), getColumna()};
   }

   public int[] calcularPosicionAleatoria(java.util.ArrayList<int[]> posicionesDisponibles) {
      if (posicionesDisponibles == null || posicionesDisponibles.isEmpty()) return null;
      int idx = random.nextInt(posicionesDisponibles.size());
      int[] p = posicionesDisponibles.get(idx);
      this.fila = p[0];
      this.columna = p[1];
      System.out.println("[PINA] Nueva posición calculada: (" + p[0] + "," + p[1] + ")");
      return p;
   }

}
