package presentation;

import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel central que renderiza el tablero del juego Bad DOPO Cream
 */
public class BoardPanel extends JPanel {

    private BadDopoCream juego;
    private HashMap<String, Image> imageCache;
    private static final int CELL_SIZE = 40; // Tamaño de cada celda en píxeles

    private int filas;
    private int columnas;

    // Colores del tablero
    private static final Color COLOR_FONDO = new Color(230, 240, 255);
    private static final Color COLOR_BORDE = new Color(100, 149, 237);

    public BoardPanel() {
        this.imageCache = new HashMap<>();
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(800, 600));
        cargarImagenes();
    }

    /**
     * Inicializa el juego con la configuración dada
     */
    public void inicializarJuego(int nivel, String modo, String sabor1, String sabor2,
                                 String nombre1, String nombre2, String perfil1, String perfil2) {
        try {
            this.juego = new BadDopoCream(nivel, modo, sabor1, sabor2,
                    nombre1, nombre2, perfil1, perfil2);
            this.juego.iniciarJuego();

            int[] dimensiones = juego.getDimensionesTablero();
            this.filas = dimensiones[0];
            this.columnas = dimensiones[1];

            setPreferredSize(new Dimension(columnas * CELL_SIZE, filas * CELL_SIZE));
            revalidate();
            repaint();

        } catch (BadDopoException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar el juego: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga todas las imágenes necesarias en caché
     */
    private void cargarImagenes() {
        try {
            // Cargar imágenes de helados
            cargarImagenSiExiste("VH_ABAJO", "src/presentation/images/VainillaAbajo.png");
            cargarImagenSiExiste("VH_ARRIBA", "src/presentation/images/VainillaDetras.png");
            cargarImagenSiExiste("VH_IZQUIERDA", "src/presentation/images/VainillaIzquierda.png");
            cargarImagenSiExiste("VH_DERECHA", "src/presentation/images/VainillaDerecha.png");

            cargarImagenSiExiste("CH_ABAJO", "src/presentation/images/ChocolateAbajo.png");
            cargarImagenSiExiste("CH_ARRIBA", "src/presentation/images/ChocolateDetras.png");
            cargarImagenSiExiste("CH_IZQUIERDA", "src/presentation/images/ChocolateIzquierda.png");
            cargarImagenSiExiste("CH_DERECHA", "src/presentation/images/ChocolateDerecha.png");

            cargarImagenSiExiste("F_ABAJO", "src/presentation/images/FresaAbajo.png");
            cargarImagenSiExiste("F_ARRIBA", "src/presentation/images/FresaDetras.png");
            cargarImagenSiExiste("F_IZQUIERDA", "src/presentation/images/FresaIzquierda.png");
            cargarImagenSiExiste("F_DERECHA", "src/presentation/images/FresaDerecha.png");

            // Cargar imágenes de frutas
            cargarImagenSiExiste("UVA", "src/presentation/images/Uva.png");
            cargarImagenSiExiste("BANANA", "src/presentation/images/platano.png");
            cargarImagenSiExiste("CEREZA", "src/presentation/images/Cereza.png");
            cargarImagenSiExiste("PINA", "src/presentation/images/Piña.png");

            // Cargar imágenes de enemigos
            cargarImagenSiExiste("TROLL", "src/presentation/images/TrollAbajo.png");
            cargarImagenSiExiste("MACETA", "src/presentation/images/MacetaAbajo.png");
            cargarImagenSiExiste("CALAMAR", "src/presentation/images/CalamarAbajo.png");

            // Cargar imágenes de obstáculos
            cargarImagenSiExiste("HIELO", "src/presentation/images/BloqueDeHielo.png");
            cargarImagenSiExiste("BORDE", "src/presentation/images/Borde.png");

        } catch (Exception e) {
            System.err.println("Error cargando imágenes: " + e.getMessage());
        }
    }

    /**
     * Carga una imagen si existe, si no, crea un placeholder
     */
    private void cargarImagenSiExiste(String key, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                imageCache.put(key, img.getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            } else {
                // Crear placeholder si la imagen no existe
                imageCache.put(key, crearPlaceholder(key));
            }
        } catch (Exception e) {
            imageCache.put(key, crearPlaceholder(key));
        }
    }

    /**
     * Crea una imagen placeholder cuando no existe la imagen real
     */
    private Image crearPlaceholder(String texto) {
        BufferedImage img = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Fondo según el tipo
        if (texto.contains("HELADO") || texto.contains("VH") || texto.contains("CH") || texto.contains("F")) {
            g2d.setColor(new Color(255, 200, 200));
        } else if (texto.contains("FRUTA") || texto.contains("UVA") || texto.contains("BANANA")) {
            g2d.setColor(new Color(200, 255, 200));
        } else if (texto.contains("ENEMIGO") || texto.contains("TROLL") || texto.contains("MACETA")) {
            g2d.setColor(new Color(255, 150, 150));
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
        }

        g2d.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, CELL_SIZE - 1, CELL_SIZE - 1);

        // Texto
        g2d.setFont(new Font("Arial", Font.BOLD, 8));
        FontMetrics fm = g2d.getFontMetrics();
        String label = texto.length() > 3 ? texto.substring(0, 3) : texto;
        int x = (CELL_SIZE - fm.stringWidth(label)) / 2;
        int y = (CELL_SIZE + fm.getAscent()) / 2;
        g2d.drawString(label, x, y);

        g2d.dispose();
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (juego == null || !juego.isJuegoIniciado()) {
            dibujarMensajeEspera(g);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar tablero base
        dibujarTablero(g2d);

        // Dibujar elementos del juego
        dibujarObstaculos(g2d);
        dibujarFrutas(g2d);
        dibujarEnemigos(g2d);
        try {
            dibujarHelados(g2d);
        } catch (BadDopoException e) {
            throw new RuntimeException(e);
        }

        // Dibujar información del juego
        dibujarHUD(g2d);
    }

    /**
     * Dibuja el mensaje de espera inicial
     */
    private void dibujarMensajeEspera(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String mensaje = "Configurando juego...";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
        int y = getHeight() / 2;
        g.drawString(mensaje, x, y);
    }

    /**
     * Dibuja la cuadrícula del tablero
     */
    private void dibujarTablero(Graphics2D g2d) {
        String[][] representacion = juego.getRepresentacionTablero();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                // Fondo de celda
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Borde de celda
                g2d.setColor(COLOR_BORDE);
                g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Dibuja los obstáculos (hielo, fogatas, baldosas calientes, bordes)
     */
    private void dibujarObstaculos(Graphics2D g2d) {
        if (juego.getTablero() == null) return;

        String[][] representacion = juego.getRepresentacionTablero();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String tipo = representacion[i][j];
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                Image img = null;

                switch (tipo) {
                    case "H": // Hielo
                        img = imageCache.get("HIELO");
                        break;
                    case "B": // Borde
                        img = imageCache.get("BORDE");
                        break;
                    case "V": // Vacío
                        continue; //Se dibuja fondo blanco
                }

                if (img != null) {
                    g2d.drawImage(img, x, y, this);
                }
            }
        }
    }

    /**
     * Dibuja las frutas en el tablero
     */
    private void dibujarFrutas(Graphics2D g2d) {
        HashMap<String, Fruta> posicionesFrutas = juego.getPosicionesFrutas();
        for (Map.Entry<String, Fruta> entry : posicionesFrutas.entrySet()) {
            String tipoKey = entry.getKey();
            // tipoKey: e.g. "BANANA_4_5" -> tipo = "BANANA"
            String tipo = tipoKey.contains("_") ? tipoKey.split("_")[0] : tipoKey;
            Fruta fruta = entry.getValue();
            int fila = fruta.getFila();
            int col = fruta.getColumna();

            int x = col * CELL_SIZE;
            int y = fila * CELL_SIZE;

            Image img = null;

            switch (tipo.toUpperCase()) {
                case "UVA":
                    img = imageCache.get("UVA");
                    break;
                case "BANANA":
                case "PLATANO":
                    img = imageCache.get("BANANA");
                    break;
                case "CEREZA":
                    img = imageCache.get("CEREZA");
                    break;
                case "PINA":
                case "PIÑA":
                    img = imageCache.get("PINA");
                    break;
            }

            if (img != null) {
                g2d.drawImage(img, x, y, this);
            } else {
                // Dibujar placeholder si no hay imagen
                g2d.setColor(Color.GREEN);
                g2d.fillOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
            }
        }
    }

    /**
     * Dibuja los enemigos en el tablero
     */
    private void dibujarEnemigos(Graphics2D g2d) {
        HashMap<String, Enemigo> posicionesEnemigos = juego.getPosicionesEnemigos();
        for (Map.Entry<String, Enemigo> entry : posicionesEnemigos.entrySet()) {
            String tipoKey = entry.getKey();
            String tipo = tipoKey.contains("_") ? tipoKey.split("_")[0] : tipoKey;
            Enemigo enemigo = entry.getValue();
            int fila = enemigo.getFila();
            int col = enemigo.getColumna();

            int x = col * CELL_SIZE;
            int y = fila * CELL_SIZE;

            Image img = null;

            switch (tipo.toUpperCase()) {
                case "TROLL":
                    img = imageCache.get("TROLL");
                    break;
                case "MACETA":
                    img = imageCache.get("MACETA");
                    break;
                case "CALAMAR":
                    img = imageCache.get("CALAMAR");
                    break;
                case "NARVAL":
                    img = imageCache.get("NARVAL");
                    break;
            }

            if (img != null) {
                g2d.drawImage(img, x, y, this);
            } else {
                // Dibujar placeholder si no hay imagen
                g2d.setColor(Color.RED);
                g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            }
        }
    }

    /**
     * Dibuja los helados en el tablero
     */
    private void dibujarHelados(Graphics2D g2d) throws BadDopoException {
        HashMap<String, int[]> posicionesHelados = juego.getPosicionesHelados();

        // Dibujar helado 1
        if (posicionesHelados.containsKey("helado1")) {
            int[] pos1 = posicionesHelados.get("helado1");
            Helado helado1 = juego.getHelado1();
            dibujarHelado(g2d, helado1, pos1[0], pos1[1]);
        }

        // Dibujar helado 2
        if (posicionesHelados.containsKey("helado2")) {
            int[] pos2 = posicionesHelados.get("helado2");
            Helado helado2 = juego.getHelado2();
            dibujarHelado(g2d, helado2, pos2[0], pos2[1]);
        }
    }

    /**
     * Dibuja un helado específico
     */
    private void dibujarHelado(Graphics2D g2d, Helado helado, int fila, int col) {
        if (helado == null) return;

        int x = col * CELL_SIZE;
        int y = fila * CELL_SIZE;

        String sabor = helado.getSabor();
        String direccion = helado.getUltimaDireccion() != null ?
                helado.getUltimaDireccion() : "ABAJO";

        String key = sabor + "_" + direccion;
        Image img = imageCache.get(key);

        if (img != null) {
            g2d.drawImage(img, x, y, this);
        } else {
            // Placeholder para helado
            Color color = sabor.equals("VH") ? new Color(255, 255, 200) :
                    sabor.equals("CH") ? new Color(139, 69, 19) :
                            new Color(255, 182, 193);
            g2d.setColor(color);
            g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    /**
     * Dibuja el HUD (información del juego)
     */
    private void dibujarHUD(Graphics2D g2d) {
        int hudY = filas * CELL_SIZE + 10;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        // Tiempo
        String tiempo = "Tiempo: " + juego.getTiempoRestanteFormato();
        g2d.drawString(tiempo, 10, hudY);

        // Puntajes
        String puntaje1 = "P1: " + juego.getPuntajeJugador1();
        g2d.drawString(puntaje1, 150, hudY);

        if (juego.getHelado2() != null) {
            String puntaje2 = "P2: " + juego.getPuntajeJugador2();
            g2d.drawString(puntaje2, 250, hudY);
        }

        // Mensaje de estado
        if (juego.getMensajeEstado() != null) {
            g2d.setFont(new Font("Arial", Font.ITALIC, 12));
            g2d.drawString(juego.getMensajeEstado(), 350, hudY);
        }

        // Progreso de frutas
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        int progressY = hudY + 20;
    }

    /**
     * Obtiene la referencia al juego
     */
    public BadDopoCream getJuego() {
        return juego;
    }

    /**
     * Actualiza el panel (llama a repaint)
     */
    public void actualizar() {
        repaint();
    }

    public void inicializarJuego(int nivelActual, String modo, String sabor1, String sabor2) {

    }
}