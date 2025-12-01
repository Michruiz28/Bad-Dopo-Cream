package Presentation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapPanel extends JPanel {
    private static final int FILAS = 18;
    private static final int COLUMNAS = 18;
    private static final int TAMAÑO_CELDA = 35; // Tamaño en píxeles de cada celda

    private JLabel[][] celdas; // Matriz de labels para mostrar imágenes
    private int[][] mapa; // Matriz que representa el estado del juego

    // Mapa de imágenes cargadas
    private Map<String, ImageIcon> imagenes;

    // Constantes para tipos de celda
    public static final int VACIO = 0;
    public static final int PARED_BORDE = 1;      // Pared azul oscura del borde
    public static final int HIELO_ROMPIBLE = 2;   // Hielo azul claro que se puede romper
    public static final int FRUTA = 3;            // Fruta naranja a recolectar
    public static final int JUGADOR1 = 4;         // Helado jugador 1
    public static final int JUGADOR2 = 5;         // Helado jugador 2
    public static final int ENEMIGO = 6;          // Enemigo
    public static final int BLOQUE_HIELO = 7;     // Bloque de hielo creado por jugador

    public MapPanel() {
        setLayout(new GridLayout(FILAS, COLUMNAS, 0, 0));
        setPreferredSize(new Dimension(COLUMNAS * TAMAÑO_CELDA, FILAS * TAMAÑO_CELDA));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        celdas = new JLabel[FILAS][COLUMNAS];
        mapa = new int[FILAS][COLUMNAS];
        imagenes = new HashMap<>();

        inicializarCeldas();
        cargarImagenes();
    }

    /**
     * Inicializa todas las celdas del tablero
     */
    private void inicializarCeldas() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                celdas[i][j] = new JLabel();
                celdas[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                celdas[i][j].setVerticalAlignment(SwingConstants.CENTER);
                celdas[i][j].setOpaque(true);
                celdas[i][j].setBackground(Color.WHITE);
                celdas[i][j].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
                add(celdas[i][j]);

                mapa[i][j] = VACIO;
            }
        }
    }

    /**
     * Carga todas las imágenes necesarias para el juego
     * Recorre automáticamente la carpeta de imágenes y carga todos los PNG
     */
    private void cargarImagenes() {
        // IMPORTANTE: Ajusta esta ruta según la ubicación de tus imágenes
        String rutaBase = "Presentation/images/";

        System.out.println("=== Cargando imágenes del juego ===");
        System.out.println("Ruta base: " + rutaBase);

        File carpetaImagenes = new File(rutaBase);

        // Verificar que la carpeta existe
        if (!carpetaImagenes.exists() || !carpetaImagenes.isDirectory()) {
            System.err.println("✗ ERROR: La carpeta de imágenes no existe: " + rutaBase);
            System.err.println("  Usando colores y emojis como fallback");
            return;
        }

        // Filtrar solo archivos PNG
        File[] archivos = carpetaImagenes.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png") ||
                        name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".jpeg")
        );

        if (archivos == null || archivos.length == 0) {
            System.err.println("✗ No se encontraron imágenes en: " + rutaBase);
            return;
        }

        // Cargar cada imagen encontrada
        for (File archivo : archivos) {
            // Obtener el nombre sin extensión para usar como clave
            String nombreArchivo = archivo.getName();
            String clave = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));

            // Cargar y escalar la imagen
            cargarYEscalarImagen(clave, archivo.getPath());
        }

        System.out.println("====================================");
    }

    /**
     * Carga y escala una imagen al tamaño de celda
     */
    private void cargarYEscalarImagen(String clave, String ruta) {
        try {
            // Intentar cargar la imagen
            ImageIcon icono = new ImageIcon(ruta);

            // Verificar si la imagen se cargó correctamente
            if (icono.getIconWidth() <= 0) {
                System.out.println("✗ ERROR: No se pudo cargar '" + clave + "' desde: " + ruta);
                imagenes.put(clave, null);
                return;
            }

            // Escalar la imagen al tamaño de celda
            Image img = icono.getImage().getScaledInstance(TAMAÑO_CELDA, TAMAÑO_CELDA, Image.SCALE_SMOOTH);
            imagenes.put(clave, new ImageIcon(img));

            System.out.println("✓ Imagen cargada: " + clave + " (" + icono.getIconWidth() + "x" + icono.getIconHeight() + " px)");

        } catch (Exception e) {
            System.out.println("✗ EXCEPCIÓN al cargar '" + clave + "': " + e.getMessage());
            imagenes.put(clave, null);
        }
    }

    /**
     * Establece un elemento en una posición específica
     */
    public void setElemento(int fila, int columna, int tipo) {
        if (!esValido(fila, columna)) {
            return;
        }

        mapa[fila][columna] = tipo;
        actualizarCelda(fila, columna, null);
    }

    /**
     * Establece un elemento con sabor específico (para jugadores)
     */
    public void setElementoConSabor(int fila, int columna, int tipo, String sabor) {
        if (!esValido(fila, columna)) {
            return;
        }

        mapa[fila][columna] = tipo;
        actualizarCelda(fila, columna, sabor);
    }

    /**
     * Actualiza la visualización de una celda
     */
    private void actualizarCelda(int fila, int columna, String saborHelado) {
        JLabel celda = celdas[fila][columna];
        int tipo = mapa[fila][columna];

        celda.setIcon(null);
        celda.setText("");

        ImageIcon imagen = null;
        Color colorFondo = Color.WHITE;

        switch (tipo) {
            case VACIO:
                colorFondo = Color.WHITE;
                break;

            case PARED_BORDE:
                imagen = imagenes.get("pared_borde");
                colorFondo = new Color(0, 51, 102); // Azul oscuro
                if (imagen == null) {
                    celda.setBackground(colorFondo);
                }
                break;

            case HIELO_ROMPIBLE:
                imagen = imagenes.get("hielo_rompible");
                colorFondo = new Color(173, 216, 230); // Azul claro
                if (imagen == null) {
                    celda.setBackground(colorFondo);
                }
                break;

            case FRUTA:
                imagen = imagenes.get("fruta");
                colorFondo = Color.WHITE;
                if (imagen == null) {
                    celda.setBackground(Color.WHITE);
                    celda.setText("🍊");
                    celda.setFont(new Font("Arial", Font.PLAIN, 20));
                }
                break;

            case JUGADOR1:
            case JUGADOR2:
                if (saborHelado != null) {
                    String claveImagen = "helado_" + saborHelado.toLowerCase();
                    imagen = imagenes.get(claveImagen);
                }
                colorFondo = Color.WHITE;
                if (imagen == null) {
                    celda.setBackground(Color.WHITE);
                    celda.setText("🍦");
                    celda.setFont(new Font("Arial", Font.PLAIN, 20));
                }
                break;

            case ENEMIGO:
                imagen = imagenes.get("enemigo");
                colorFondo = Color.WHITE;
                if (imagen == null) {
                    celda.setBackground(Color.WHITE);
                    celda.setText("👾");
                    celda.setFont(new Font("Arial", Font.PLAIN, 20));
                }
                break;

            case BLOQUE_HIELO:
                imagen = imagenes.get("bloque_hielo");
                colorFondo = new Color(135, 206, 250); // Azul cielo
                if (imagen == null) {
                    celda.setBackground(colorFondo);
                }
                break;

            default:
                colorFondo = Color.WHITE;
        }

        if (imagen != null) {
            celda.setIcon(imagen);
            celda.setBackground(Color.WHITE);
        } else {
            celda.setBackground(colorFondo);
        }
    }

    /**
     * Carga un nivel completo desde una matriz
     */
    public void cargarNivel(int[][] nivelMapa, String sabor1, String sabor2) {
        if (nivelMapa == null || nivelMapa.length != FILAS || nivelMapa[0].length != COLUMNAS) {
            System.err.println("Error: El mapa debe ser de 18x18");
            return;
        }

        limpiarTablero();

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                int elemento = nivelMapa[i][j];

                if (elemento == JUGADOR1) {
                    setElementoConSabor(i, j, JUGADOR1, sabor1);
                } else if (elemento == JUGADOR2 && sabor2 != null) {
                    setElementoConSabor(i, j, JUGADOR2, sabor2);
                } else {
                    setElemento(i, j, elemento);
                }
            }
        }
    }

    /**
     * Limpia todo el tablero
     */
    public void limpiarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                setElemento(i, j, VACIO);
            }
        }
    }

    /**
     * Mueve un elemento de una posición a otra
     */
    public void moverElemento(int filaOrigen, int colOrigen, int filaDestino, int colDestino, String sabor) {
        if (esValido(filaOrigen, colOrigen) && esValido(filaDestino, colDestino)) {
            int elemento = mapa[filaOrigen][colOrigen];
            setElemento(filaOrigen, colOrigen, VACIO);

            if (elemento == JUGADOR1 || elemento == JUGADOR2) {
                setElementoConSabor(filaDestino, colDestino, elemento, sabor);
            } else {
                setElemento(filaDestino, colDestino, elemento);
            }
        }
    }

    /**
     * Verifica si una posición es válida
     */
    private boolean esValido(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    /**
     * Obtiene el tipo de elemento en una posición
     */
    public int getElemento(int fila, int columna) {
        if (esValido(fila, columna)) {
            return mapa[fila][columna];
        }
        return -1;
    }

    /**
     * Verifica si una posición está vacía o tiene una fruta
     */
    public boolean esTransitable(int fila, int columna) {
        if (!esValido(fila, columna)) {
            return false;
        }
        int elemento = mapa[fila][columna];
        return elemento == VACIO || elemento == FRUTA;
    }

    /**
     * Cuenta cuántas frutas quedan en el tablero
     */
    public int contarFrutas() {
        int contador = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (mapa[i][j] == FRUTA) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public int getFilas() {
        return FILAS;
    }

    public int getColumnas() {
        return COLUMNAS;
    }
}