package presentation;

import domain.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Panel central que renderiza el tablero del juego Bad DOPO Cream
 * Ahora con soporte para múltiples fases por nivel
 */
public class BoardPanel extends JPanel {

    private BadDopoCream juego;
    private HashMap<String, Image> imageCache;
    private static final int CELL_SIZE = 35;

    private int filas;
    private int columnas;

    // ===== NUEVO: Control de fases =====
    private int ultimaFaseRenderizada = -1;
    private String mensajeFaseCambiada = null;
    private long tiempoMensajeFase = 0;
    private static final long DURACION_MENSAJE_FASE = 3000; // 3 segundos

    private MovementController movementController;
    private boolean cambiandoNivel = false; 

    private static final Color COLOR_FONDO = new Color(230, 240, 255);
    private static final Color COLOR_BORDE = new Color(100, 149, 237);

    
    private boolean mostrarPantallaFinal = false;

    public BoardPanel() {
        this.imageCache = new HashMap<>();
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(650, 950));
        cargarImagenes();
    }

    public void setMovementController(MovementController controller) {
        if (this.movementController != null) {
            this.removeKeyListener(this.movementController);
        }
        this.movementController = controller;
        this.addKeyListener(controller);
        this.setFocusable(true);
        System.out.println("[BOARD] MovementController configurado");
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

            // Resetear control de fases
            this.ultimaFaseRenderizada = juego.getFaseActual();
            this.mensajeFaseCambiada = null;

            setPreferredSize(new Dimension(columnas * CELL_SIZE, filas * CELL_SIZE + 60));
            revalidate();
            repaint();

            System.out.println("[BOARD] Juego inicializado - Fase: " + juego.getFaseActual());

        } catch (BadDopoException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar el juego: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sobrecarga del método para compatibilidad
     */
    public void inicializarJuego(int nivel, String modo, String sabor1, String sabor2) {
        inicializarJuego(nivel, modo, sabor1, sabor2, "Jugador 1", "Jugador 2", null, null);
    }

    
    public void mostrarPantallaFinal() {
        this.mostrarPantallaFinal = true;
        repaint();
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
            cargarImagenSiExiste("BANANO", "src/presentation/images/platano.png");
            cargarImagenSiExiste("BANANA", "src/presentation/images/platano.png");
            cargarImagenSiExiste("PLATANO", "src/presentation/images/platano.png");
            cargarImagenSiExiste("CEREZA", "src/presentation/images/Cereza.png");
            cargarImagenSiExiste("PINA", "src/presentation/images/Piña.png");

            // Cargar imágenes de enemigos
            cargarImagenSiExiste("TROLL_ABAJO", "src/presentation/images/TrollAbajo.png");
            cargarImagenSiExiste("TROLL_ARRIBA", "src/presentation/images/TrollDetras.png");
            cargarImagenSiExiste("TROLL_IZQUIERDA", "src/presentation/images/TrolIzquierda.png");
            cargarImagenSiExiste("TROLL_DERECHA", "src/presentation/images/TrollDerecha.png");

            cargarImagenSiExiste("MACETA_ABAJO", "src/presentation/images/MacetaAbajo.png");
            cargarImagenSiExiste("MACETA_ARRIBA", "src/presentation/images/MacetaDetras.png");
            cargarImagenSiExiste("MACETA_IZQUIERDA", "src/presentation/images/MacetaIzquierda.png");
            cargarImagenSiExiste("MACETA_DERECHA", "src/presentation/images/MacetaDerecha.png");

            cargarImagenSiExiste("CALAMAR_ABAJO", "src/presentation/images/CalamarAbajo.png");
            cargarImagenSiExiste("CALAMAR_ARRIBA", "src/presentation/images/CalamarDetras.png");
            cargarImagenSiExiste("CALAMAR_IZQUIERDA", "src/presentation/images/CalamarIzquierda.png");
            cargarImagenSiExiste("CALAMAR_DERECHA", "src/presentation/images/CalamarDerecha.png");

            // Cargar imágenes de obstáculos
            cargarImagenSiExiste("HIELO", "src/presentation/images/BloqueDeHielo.png");
            cargarImagenSiExiste("BORDE", "src/presentation/images/Borde.png");

        } catch (Exception e) {
            System.err.println("Error cargando imágenes: " + e.getMessage());
        }
    }

    private void cargarImagenSiExiste(String key, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                imageCache.put(key, img.getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            } else {
                imageCache.put(key, crearPlaceholder(key));
            }
        } catch (Exception e) {
            imageCache.put(key, crearPlaceholder(key));
        }
    }

    private Image crearPlaceholder(String texto) {
        BufferedImage img = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        if (texto.contains("HELADO") || texto.contains("VH") || texto.contains("CH") || texto.contains("F")) {
            g2d.setColor(new Color(255, 200, 200));
        } else if (texto.contains("FRUTA") || texto.contains("UVA") || texto.contains("BANANA") || texto.contains("BANANO")) {
            g2d.setColor(new Color(200, 255, 200));
        } else if (texto.contains("ENEMIGO") || texto.contains("TROLL") || texto.contains("MACETA")) {
            g2d.setColor(new Color(255, 150, 150));
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
        }

        g2d.fillRect(0, 0, CELL_SIZE, CELL_SIZE);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, CELL_SIZE - 1, CELL_SIZE - 1);

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
        if (mostrarPantallaFinal) {
            dibujarPantallaVictoria(g);
            return;
        }
        
        super.paintComponent(g);

        if (juego != null && juego.isJuegoGanado()) {
            dibujarPantallaVictoria(g);
            return;
}
        if (juego == null ||
            cambiandoNivel ||
            juego.getTablero() == null ||
            juego.getRepresentacionTablero() == null) {

            dibujarMensajeEspera(g);
            return;
        }

        // ===== NUEVO: Detectar cambio de fase =====
        verificarCambioDeFase();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarTablero(g2d);
        dibujarObstaculos(g2d);
        dibujarFrutas(g2d);
        dibujarEnemigos(g2d);

        try {
            dibujarHelados(g2d);
        } catch (BadDopoException e) {
            System.err.println("Error dibujando helados: " + e.getMessage());
        }

        dibujarHUD(g2d);

        // ===== NUEVO: Mostrar mensaje de cambio de fase =====
        dibujarMensajeFase(g2d);
    }

    /**
     * NUEVO: Detecta si cambió la fase y muestra mensaje
     */
    private void verificarCambioDeFase() {
        if (juego == null) return;

        int faseActual = juego.getFaseActual();

        if (faseActual != ultimaFaseRenderizada) {
            System.out.println("[BOARD] ¡Cambio de fase detectado! " + ultimaFaseRenderizada + " -> " + faseActual);

            // Configurar mensaje
            if (faseActual > 0) { // No mostrar mensaje en la fase inicial
                mensajeFaseCambiada = "¡NUEVA FASE! (" + (faseActual + 1) + "/" + juego.getTotalFases() + ")";
                tiempoMensajeFase = System.currentTimeMillis();
            }

            ultimaFaseRenderizada = faseActual;

            // Forzar actualización visual
            revalidate();
            repaint();
        }
    }

    /**
     * NUEVO: Dibuja mensaje de cambio de fase (temporal)
     */
    private void dibujarMensajeFase(Graphics2D g2d) {
        if (mensajeFaseCambiada == null) return;

        long tiempoTranscurrido = System.currentTimeMillis() - tiempoMensajeFase;

        if (tiempoTranscurrido > DURACION_MENSAJE_FASE) {
            mensajeFaseCambiada = null;
            return;
        }

        // Calcular opacidad (fade out)
        float opacidad = 1.0f;
        if (tiempoTranscurrido > DURACION_MENSAJE_FASE - 1000) {
            opacidad = (DURACION_MENSAJE_FASE - tiempoTranscurrido) / 1000.0f;
        }

        // Configurar composite para transparencia
        Composite originalComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad));

        // Dibujar fondo del mensaje
        int mensajeWidth = 300;
        int mensajeHeight = 60;
        int mensajeX = (columnas * CELL_SIZE - mensajeWidth) / 2;
        int mensajeY = (filas * CELL_SIZE - mensajeHeight) / 2;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(mensajeX, mensajeY, mensajeWidth, mensajeHeight, 20, 20);

        g2d.setColor(new Color(255, 215, 0)); // Dorado
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(mensajeX, mensajeY, mensajeWidth, mensajeHeight, 20, 20);

        // Dibujar texto
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = mensajeX + (mensajeWidth - fm.stringWidth(mensajeFaseCambiada)) / 2;
        int textY = mensajeY + (mensajeHeight + fm.getAscent()) / 2;
        g2d.drawString(mensajeFaseCambiada, textX, textY);

        // Restaurar composite original
        g2d.setComposite(originalComposite);
    }

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
     * NUEVO: Dibuja la pantalla de victoria con imagen o mensaje
     */
    private void dibujarPantallaVictoria(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ===== FONDO AZUL DEGRADADO =====
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(30, 60, 150),
                0, getHeight(), new Color(100, 160, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int centerX = getWidth() / 2;
        int y = 80;

        // ===== TEXTO SUPERIOR =====
        g2d.setFont(new Font("Arial", Font.BOLD, 56));
        g2d.setColor(new Color(255, 215, 0)); // dorado
        String titulo = "¡FELICIDADES!";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(titulo, centerX - fm.stringWidth(titulo) / 2, y);

        y += 40;

        // ===== SUBTÍTULO =====
        g2d.setFont(new Font("Arial", Font.BOLD, 26));
        g2d.setColor(Color.WHITE);
        String subtitulo = "Completaste el juego";
        fm = g2d.getFontMetrics();
        g2d.drawString(subtitulo, centerX - fm.stringWidth(subtitulo) / 2, y);

        // ===== IMAGEN DE VICTORIA =====
        Image img = cargarImagenVictoria();
        if (img != null) {
            int imgX = centerX - img.getWidth(this) / 2;
            int imgY = y + 20;
            g2d.drawImage(img, imgX, imgY, this);
            y = imgY + img.getHeight(this) + 30;
        } else {
            y += 80;
        }

        // ===== PUNTAJES =====
        dibujarInformacionFinal(g2d, y);
    }

    
    /**
     * Intenta cargar la imagen de victoria
     */
    private Image cargarImagenVictoria() {
        try {
            File file = new File("src/presentation/images/FinJuego.png");
            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                // Escalar si es necesario
                int maxWidth = getWidth() - 100;
                int maxHeight = getHeight() - 200;
                
                if (img.getWidth() > maxWidth || img.getHeight() > maxHeight) {
                    double scale = Math.min(
                        (double) maxWidth / img.getWidth(),
                        (double) maxHeight / img.getHeight()
                    );
                    int newWidth = (int) (img.getWidth() * scale);
                    int newHeight = (int) (img.getHeight() * scale);
                    return img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                }
                return img;
            } else {
                System.out.println("[BOARD] Imagen de victoria no encontrada: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("[BOARD] Error al cargar imagen de victoria: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Dibuja mensaje de victoria en texto si no hay imagen
     */
    private void dibujarMensajeVictoriaTexto(Graphics2D g2d) {
        // Título principal
        g2d.setColor(new Color(255, 215, 0)); // Dorado
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        String titulo = "¡VICTORIA!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(titulo)) / 2;
        int y = getHeight() / 2 - 100;
        
        // Sombra del texto
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(titulo, x + 3, y + 3);
        
        // Texto principal
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(titulo, x, y);
        
        // Subtítulo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String subtitulo = "¡Has completado todos los niveles!";
        fm = g2d.getFontMetrics();
        x = (getWidth() - fm.stringWidth(subtitulo)) / 2;
        g2d.drawString(subtitulo, x, y + 60);
        
        // Estrellas decorativas
        dibujarEstrellas(g2d);
    }
    
    /**
     * Dibuja estrellas decorativas alrededor del mensaje
     */
    private void dibujarEstrellas(Graphics2D g2d) {
        g2d.setColor(new Color(255, 215, 0));
        int[][] posiciones = {
            {100, 150}, {getWidth() - 100, 150},
            {150, getHeight() - 150}, {getWidth() - 150, getHeight() - 150},
            {getWidth() / 2, 80}
        };
        
        for (int[] pos : posiciones) {
            dibujarEstrella(g2d, pos[0], pos[1], 20);
        }
    }
    
    /**
     * Dibuja una estrella de 5 puntas
     */
    private void dibujarEstrella(Graphics2D g2d, int x, int y, int radio) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];
        
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 2 + i * Math.PI / 5;
            int r = (i % 2 == 0) ? radio : radio / 2;
            xPoints[i] = x + (int) (r * Math.cos(angle));
            yPoints[i] = y + (int) (r * Math.sin(angle));
        }
        
        g2d.fillPolygon(xPoints, yPoints, 10);
    }
    
    private void dibujarInformacionFinal(Graphics2D g2d, int y) {
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.setColor(Color.WHITE);

        String p1 = "Jugador 1: " + juego.getPuntajeJugador1() + " puntos";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(p1)) / 2;
        g2d.drawString(p1, x, y);

        if (juego.getHelado2() != null) {
            y += 35;
            String p2 = "Jugador 2: " + juego.getPuntajeJugador2() + " puntos";
            x = (getWidth() - fm.stringWidth(p2)) / 2;
            g2d.drawString(p2, x, y);

            y += 35;
            g2d.setColor(new Color(255, 215, 0));
            String ganador = "Ganador: " + juego.getGanador();
            x = (getWidth() - fm.stringWidth(ganador)) / 2;
            g2d.drawString(ganador, x, y);
        }

        // ===== MENSAJE DE SALIDA =====
        y += 50;
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        g2d.setColor(new Color(230, 230, 230));
        String salir = "Presiona ESC o cierra la ventana para salir";
        x = (getWidth() - g2d.getFontMetrics().stringWidth(salir)) / 2;
        g2d.drawString(salir, x, y);
    }


    private void dibujarTablero(Graphics2D g2d) {
        String[][] representacion = juego.getRepresentacionTablero();
        int filas = representacion.length;
        int columnas = representacion[0].length;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                g2d.setColor(Color.WHITE);
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                g2d.setColor(COLOR_BORDE);
                g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void dibujarObstaculos(Graphics2D g2d) {
        if (juego.getTablero() == null) return;

        String[][] representacion = juego.getRepresentacionTablero();
        int filas = representacion.length;
        int columnas = representacion[0].length;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String tipo = representacion[i][j];
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                Image img = null;

                switch (tipo) {
                    case "H":
                        img = imageCache.get("HIELO");
                        break;
                    case "B":
                        img = imageCache.get("BORDE");
                        break;
                    case "V":
                        continue;
                }

                if (img != null) {
                    g2d.drawImage(img, x, y, this);
                }
            }
        }
    }

    /**
     * Dibuja las frutas usando las claves precargadas en imageCache
     */
    private void dibujarFrutas(Graphics2D g2d) {
        ArrayList<Fruta> frutas = juego.getFrutasEnJuego();
        
        if (frutas.size() > 0 && frutas.size() <= 5) {
            System.out.println("[BOARD] Dibujando " + frutas.size() + " frutas");
        }

        for (Fruta fruta : frutas) {
            int fila = fruta.getFila();
            int col = fruta.getColumna();

            int x = col * CELL_SIZE;
            int y = fila * CELL_SIZE;

            // Obtener el nombre de la clase y convertir a mayúsculas para buscar en imageCache
            String nombreClase = fruta.getClass().getSimpleName();
            String clave = nombreClase.toUpperCase(); // "UVA", "BANANO", "CEREZA", "PINA"
            Image img = imageCache.get(clave);

            if (img != null) {
                g2d.drawImage(img, x, y, this);
            } else {
                // Placeholder si no se encuentra imagen
                Color colorFruta = Color.GREEN;
                String inicial = nombreClase.substring(0, 1);

                if (nombreClase.equals("Uva")) {
                    colorFruta = new Color(128, 0, 128); // Púrpura
                } else if (nombreClase.equals("Banano")) {
                    colorFruta = new Color(255, 255, 0); // Amarillo
                } else if (nombreClase.equals("Cereza")) {
                    colorFruta = new Color(220, 20, 60); // Rojo
                } else if (nombreClase.equals("Pina")) {
                    colorFruta = new Color(255, 165, 0); // Naranja
                }

                g2d.setColor(colorFruta);
                g2d.fillOval(x + 8, y + 8, CELL_SIZE - 16, CELL_SIZE - 16);

                // Dibujar inicial de la fruta
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = x + (CELL_SIZE - fm.stringWidth(inicial)) / 2;
                int textY = y + (CELL_SIZE + fm.getAscent()) / 2 - 2;
                g2d.drawString(inicial, textX, textY);
            }
        }
    }

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

            Image img = imageCache.get(tipo.toUpperCase());

            String direccion = enemigo.getUltimaDireccion() != null ? enemigo.getUltimaDireccion() : "ABAJO";
            String key = tipo.toUpperCase() + "_" + direccion;
            img = imageCache.get(key);
            if (img == null) {
                // Fallback a la imagen genérica por tipo
                img = imageCache.get(tipo.toUpperCase());
            }

            if (img != null) {
                g2d.drawImage(img, x, y, this);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
            }
        }
    }

    private void dibujarHelados(Graphics2D g2d) throws BadDopoException {
        HashMap<String, int[]> posicionesHelados = juego.getPosicionesHelados();

        if (posicionesHelados.containsKey("helado1")) {
            int[] pos1 = posicionesHelados.get("helado1");
            Helado helado1 = juego.getHelado1();
            dibujarHelado(g2d, helado1, pos1[0], pos1[1]);
        }

        if (posicionesHelados.containsKey("helado2")) {
            int[] pos2 = posicionesHelados.get("helado2");
            Helado helado2 = juego.getHelado2();
            dibujarHelado(g2d, helado2, pos2[0], pos2[1]);
        }
    }

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
            Color color = sabor.equals("VH") ? new Color(255, 255, 200) :
                    sabor.equals("CH") ? new Color(139, 69, 19) :
                            new Color(255, 182, 193);
            g2d.setColor(color);
            g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    /**
     * Dibuja el HUD - MEJORADO con información de fase
     */
    private void dibujarHUD(Graphics2D g2d) {
        int hudY = filas * CELL_SIZE + 15;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        // Línea 1: Tiempo y Puntajes
        String tiempo = "Tiempo: " + juego.getTiempoRestanteFormato();
        g2d.drawString(tiempo, 10, hudY);

        String puntaje1 = "P1: " + juego.getPuntajeJugador1();
        g2d.drawString(puntaje1, 150, hudY);

        if (juego.getHelado2() != null) {
            String puntaje2 = "P2: " + juego.getPuntajeJugador2();
            g2d.drawString(puntaje2, 250, hudY);
        }

        // ===== NUEVO: Información de fase =====
        String infoFase = "Fase: " + (juego.getFaseActual() + 1) + "/" + juego.getTotalFases();
        g2d.setColor(new Color(0, 100, 200));
        g2d.drawString(infoFase, 350, hudY);

        // Línea 2: Mensaje de estado y progreso de frutas
        hudY += 20;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));

        if (juego.getMensajeEstado() != null) {
            g2d.drawString(juego.getMensajeEstado(), 10, hudY);
        }

        // Mostrar progreso de frutas de la fase actual
        HashMap<String, Integer> frutasRequeridas = juego.getFrutasRequeridas();
        HashMap<String, Integer> frutasRecolectadas = juego.getFrutasRecolectadas();

        if (!frutasRequeridas.isEmpty()) {
            StringBuilder progreso = new StringBuilder("Frutas: ");
            for (String tipo : frutasRequeridas.keySet()) {
                int recolectadas = frutasRecolectadas.getOrDefault(tipo, 0);
                int requeridas = frutasRequeridas.get(tipo);
                progreso.append(tipo).append(" ").append(recolectadas).append("/").append(requeridas).append("  ");
            }
            g2d.drawString(progreso.toString(), 200, hudY);
        }
    }

    public BadDopoCream getJuego() {
        return juego;
    }

    /**
     * Actualiza el panel - llamado desde GameLoop
     */

    public void actualizar() {
        if (juego == null) return;
        repaint();
    }

}