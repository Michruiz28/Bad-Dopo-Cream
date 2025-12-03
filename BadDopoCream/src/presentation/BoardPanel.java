package presentation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private static final int FILAS = 18;
    private static final int COLUMNAS = 18;
    private static final int TAMAÑO_CELDA = 38; // Ajustado para ventana 800x800

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

    public BoardPanel() {
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
                // Sin borde para que las imágenes se vean mejor
                celdas[i][j].setBorder(null);
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
        String[] rutasPosibles = {
                "Presentation/images/",
                "src/Presentation/images/",
                "./Presentation/images/",
                "images/",
                "../images/"
        };

        String rutaBase = null;
        File carpetaImagenes = null;

        System.out.println("=== Buscando carpeta de imágenes ===");
        System.out.println("Directorio actual: " + System.getProperty("user.dir"));

        // Buscar la carpeta en diferentes ubicaciones
        for (String ruta : rutasPosibles) {
            File carpeta = new File(ruta);
            System.out.println("Probando ruta: " + ruta + " → " + carpeta.getAbsolutePath());
            if (carpeta.exists() && carpeta.isDirectory()) {
                rutaBase = ruta;
                carpetaImagenes = carpeta;
                System.out.println("✓ Carpeta encontrada!");
                break;
            }
        }

        if (carpetaImagenes == null) {
            System.err.println("✗ ERROR: No se encontró la carpeta de imágenes");
            System.err.println("  Buscadas en:");
            for (String ruta : rutasPosibles) {
                System.err.println("    - " + ruta);
            }
            System.err.println("  Usando colores y emojis como fallback");
            return;
        }

        System.out.println("Ruta base seleccionada: " + rutaBase);
        System.out.println();

        // Filtrar solo archivos PNG/JPG
        File[] archivos = carpetaImagenes.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png") ||
                        name.toLowerCase().endsWith(".jpg") ||
                        name.toLowerCase().endsWith(".jpeg")
        );

        if (archivos == null || archivos.length == 0) {
            System.err.println("✗ No se encontraron imágenes PNG/JPG en: " + rutaBase);
            System.err.println("  Archivos en la carpeta:");
            File[] todosArchivos = carpetaImagenes.listFiles();
            if (todosArchivos != null) {
                for (File f : todosArchivos) {
                    System.err.println("    - " + f.getName());
                }
            }
            return;
        }

        System.out.println("Encontrados " + archivos.length + " archivos de imagen");
        System.out.println();

        // Cargar cada imagen encontrada
        for (File archivo : archivos) {
            // Obtener el nombre sin extensión para usar como clave
            String nombreArchivo = archivo.getName();
            String clave = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));

            // Cargar y escalar la imagen
            cargarYEscalarImagen(clave, archivo.getAbsolutePath());
        }

        System.out.println("====================================");
        System.out.println("Total de imágenes cargadas: " + imagenes.size());
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

            Image img = icono.getImage();

            Image imgEscalada = img.getScaledInstance(TAMAÑO_CELDA, TAMAÑO_CELDA, Image.SCALE_SMOOTH);
            imagenes.put(clave, new ImageIcon(imgEscalada));

            System.out.println("✓ Imagen cargada: " + clave + " (" + icono.getIconWidth() + "x" + icono.getIconHeight() + " px → " + TAMAÑO_CELDA + "x" + TAMAÑO_CELDA + " px)");

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
        celda.setFont(new Font("Arial", Font.BOLD, 18));

        ImageIcon imagen = null;
        Color colorFondo = Color.WHITE;
        String textoFallback = "";

        switch (tipo) {
            case VACIO:
                colorFondo = Color.WHITE;
                break;

            case PARED_BORDE:
                imagen = imagenes.get("Borde");
                colorFondo = new Color(0, 51, 102); // Azul oscuro
                textoFallback = "";
                break;

            case HIELO_ROMPIBLE:
                imagen = imagenes.get("BloqueDeHielo");
                colorFondo = new Color(173, 216, 230); // Azul claro
                textoFallback = "";
                break;

            case FRUTA:
                imagen = imagenes.get("platano");
                colorFondo = Color.WHITE;
                textoFallback = "🍇";
                break;

            case JUGADOR1:
            case JUGADOR2:
                if (saborHelado != null) {
                    String claveImagen = saborHelado + "Abajo";
                    imagen = imagenes.get(claveImagen);

                    System.out.println("    Buscando imagen: '" + claveImagen + "' - " +
                            (imagen != null ? "ENCONTRADA" : "NO ENCONTRADA"));

                    if (imagen == null) {
                        System.out.println("    Imágenes disponibles: " + imagenes.keySet());
                    }
                }
                colorFondo = Color.WHITE;
                textoFallback = "🍦";
                break;

            case ENEMIGO:
                imagen = imagenes.get("Iglu");
                colorFondo = Color.WHITE;
                textoFallback = "🐧";
                break;

            case BLOQUE_HIELO:
                imagen = imagenes.get("BloqueDeHielo");
                colorFondo = new Color(135, 206, 250); // Azul cielo
                textoFallback = "";
                break;

            default:
                colorFondo = Color.WHITE;
        }

        // Si hay imagen, mostrarla; si no, usar fallback
        if (imagen != null && imagen.getIconWidth() > 0) {
            celda.setIcon(imagen);
            celda.setBackground(Color.WHITE);
        } else {
            celda.setBackground(colorFondo);
            if (!textoFallback.isEmpty()) {
                celda.setText(textoFallback);
            }
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

        System.out.println("=== Cargando nivel con sabores ===");
        System.out.println("Sabor jugador 1: " + sabor1);
        System.out.println("Sabor jugador 2: " + sabor2);

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                int elemento = nivelMapa[i][j];

                if (elemento == JUGADOR1) {
                    System.out.println("  Colocando JUGADOR1 en (" + i + "," + j + ") con sabor: " + sabor1);
                    setElementoConSabor(i, j, JUGADOR1, sabor1);
                } else if (elemento == JUGADOR2 && sabor2 != null) {
                    System.out.println("  Colocando JUGADOR2 en (" + i + "," + j + ") con sabor: " + sabor2);
                    setElementoConSabor(i, j, JUGADOR2, sabor2);
                } else {
                    setElemento(i, j, elemento);
                }
            }
        }

        System.out.println("================================");
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
     * Mueve un elemento de una posición a otra con dirección
     */
    public void moverElemento(int filaOrigen, int colOrigen, int filaDestino, int colDestino, String sabor, String direccion) {
        if (esValido(filaOrigen, colOrigen) && esValido(filaDestino, colDestino)) {
            int elemento = mapa[filaOrigen][colOrigen];

            // Si hay fruta en destino, recolectarla
            if (mapa[filaDestino][colDestino] == FRUTA) {
                // TODO: Incrementar puntaje
                System.out.println("¡Fruta recolectada!");
            }

            // Limpiar origen
            setElemento(filaOrigen, colOrigen, VACIO);

            // Colocar en destino con nueva dirección
            if (elemento == JUGADOR1 || elemento == JUGADOR2) {
                setElementoConSaborYDireccion(filaDestino, colDestino, elemento, sabor, direccion);
            } else {
                setElemento(filaDestino, colDestino, elemento);
            }
        }
    }

    /**
     * Cambia solo la dirección del jugador sin moverlo
     */
    public void cambiarDireccion(int fila, int col, String sabor, String direccion) {
        if (esValido(fila, col)) {
            int elemento = mapa[fila][col];
            if (elemento == JUGADOR1 || elemento == JUGADOR2) {
                setElementoConSaborYDireccion(fila, col, elemento, sabor, direccion);
            }
        }
    }

    /**
     * Establece un elemento con sabor y dirección específica
     */
    private void setElementoConSaborYDireccion(int fila, int columna, int tipo, String sabor, String direccion) {
        if (!esValido(fila, columna)) {
            return;
        }

        mapa[fila][columna] = tipo;
        actualizarCeldaConDireccion(fila, columna, sabor, direccion);
    }

    /**
     * Actualiza celda con dirección específica
     */
    private void actualizarCeldaConDireccion(int fila, int columna, String sabor, String direccion) {
        JLabel celda = celdas[fila][columna];
        int tipo = mapa[fila][columna];

        celda.setIcon(null);
        celda.setText("");

        if (tipo == JUGADOR1 || tipo == JUGADOR2) {
            // Construir clave con dirección
            String claveImagen = sabor + direccion; // Ej: "ChocolateAbajo", "VainillaDerecha"
            ImageIcon imagen = imagenes.get(claveImagen);

            if (imagen != null && imagen.getIconWidth() > 0) {
                celda.setIcon(imagen);
                celda.setBackground(Color.WHITE);
            } else {
                celda.setBackground(Color.WHITE);
                celda.setText("🍦");
                celda.setFont(new Font("Arial", Font.BOLD, 18));
            }
        }
    }

    /**
     * Crea un bloque de hielo en la posición especificada
     */
    public void crearBloqueHielo(int fila, int col) {
        if (!esValido(fila, col)) {
            return;
        }

        int elemento = mapa[fila][col];

        // Solo puede crear hielo en espacios vacíos o sobre hielo rompible
        if (elemento == VACIO) {
            setElemento(fila, col, BLOQUE_HIELO);
            System.out.println("Bloque de hielo creado en (" + fila + "," + col + ")");
        } else if (elemento == HIELO_ROMPIBLE) {
            setElemento(fila, col, VACIO);
            System.out.println("Hielo rompible destruido en (" + fila + "," + col + ")");
        } else if (elemento == BLOQUE_HIELO) {
            setElemento(fila, col, VACIO);
            System.out.println("Bloque de hielo destruido en (" + fila + "," + col + ")");
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