package Presentation;

public class Niveles {

    /**
     * Retorna el mapa del nivel especificado
     */
    public static int[][] getNivel(int numeroNivel) {
        switch (numeroNivel) {
            case 1:
                return getNivel1();
            case 2:
                return getNivel2();
            case 3:
                return getNivel3();
            default:
                return getNivel1();
        }
    }

    /**
     * Nivel 1 - Basado en la imagen que compartiste
     */
    public static int[][] getNivel1() {
        int[][] nivel = new int[18][18];

        // Inicializar todo vacío
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                nivel[i][j] = MapPanel.VACIO;
            }
        }

        // Crear bordes con paredes
        for (int i = 0; i < 18; i++) {
            nivel[i][0] = MapPanel.PARED_BORDE;
            nivel[i][17] = MapPanel.PARED_BORDE;
            nivel[0][i] = MapPanel.PARED_BORDE;
            nivel[17][i] = MapPanel.PARED_BORDE;
        }

        // Columnas de hielo rompible (azul claro) - Columnas 1 y 16
        for (int i = 1; i < 17; i++) {
            nivel[i][1] = MapPanel.HIELO_ROMPIBLE;
            nivel[i][16] = MapPanel.HIELO_ROMPIBLE;
        }

        // Filas de hielo rompible - Filas 1 y 16
        for (int j = 2; j < 16; j++) {
            nivel[1][j] = MapPanel.HIELO_ROMPIBLE;
            nivel[16][j] = MapPanel.HIELO_ROMPIBLE;
        }

        // Columna vertical de hielo en el centro izquierdo (columna 5)
        for (int i = 4; i < 14; i++) {
            nivel[i][5] = MapPanel.HIELO_ROMPIBLE;
        }

        // Columna vertical de hielo en el centro derecho (columna 12)
        for (int i = 4; i < 14; i++) {
            nivel[i][12] = MapPanel.HIELO_ROMPIBLE;
        }

        // Frutas - Esquina superior izquierda (posición aproximada 4,3 y 4,4)
        nivel[4][3] = MapPanel.FRUTA;
        nivel[4][4] = MapPanel.FRUTA;

        // Frutas - Esquina superior derecha (posición aproximada 4,13 y 4,14)
        nivel[4][13] = MapPanel.FRUTA;
        nivel[4][14] = MapPanel.FRUTA;
        nivel[5][14] = MapPanel.FRUTA;

        // Frutas - Centro izquierdo (posición aproximada 7,6)
        nivel[7][6] = MapPanel.FRUTA;

        // Frutas - Centro (posición aproximada 7,9)
        nivel[7][9] = MapPanel.FRUTA;

        // Frutas - Centro derecho espejo
        nivel[7][11] = MapPanel.FRUTA;

        // Frutas - Más al centro
        nivel[8][6] = MapPanel.FRUTA;
        nivel[9][6] = MapPanel.FRUTA;

        nivel[8][11] = MapPanel.FRUTA;
        nivel[9][11] = MapPanel.FRUTA;

        // Frutas - Más centro
        nivel[11][6] = MapPanel.FRUTA;
        nivel[11][9] = MapPanel.FRUTA;

        // Frutas - Esquina inferior izquierda
        nivel[13][3] = MapPanel.FRUTA;
        nivel[13][4] = MapPanel.FRUTA;

        // Frutas - Esquina inferior derecha
        nivel[13][13] = MapPanel.FRUTA;
        nivel[13][14] = MapPanel.FRUTA;

        // Jugadores - Centro abajo (posición aproximada fila 14, columnas 8 y 9)
        nivel[14][8] = MapPanel.JUGADOR1;
        nivel[14][9] = MapPanel.JUGADOR2;

        return nivel;
    }

    /**
     * Nivel 2 - Más difícil que el nivel 1
     */
    public static int[][] getNivel2() {
        int[][] nivel = new int[18][18];

        // Inicializar todo vacío
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                nivel[i][j] = MapPanel.VACIO;
            }
        }

        // Crear bordes
        for (int i = 0; i < 18; i++) {
            nivel[i][0] = MapPanel.PARED_BORDE;
            nivel[i][17] = MapPanel.PARED_BORDE;
            nivel[0][i] = MapPanel.PARED_BORDE;
            nivel[17][i] = MapPanel.PARED_BORDE;
        }

        // Crear laberinto con hielo
        for (int i = 1; i < 17; i++) {
            nivel[i][1] = MapPanel.HIELO_ROMPIBLE;
            nivel[i][16] = MapPanel.HIELO_ROMPIBLE;
        }

        // Paredes internas en forma de cruz
        for (int i = 4; i < 14; i++) {
            if (i != 9) { // Dejar un hueco en el centro
                nivel[i][9] = MapPanel.HIELO_ROMPIBLE;
                nivel[9][i] = MapPanel.HIELO_ROMPIBLE;
            }
        }

        // Distribuir frutas
        int[][] posFrutas = {
                {3, 3}, {3, 14}, {14, 3}, {14, 14},
                {5, 5}, {5, 12}, {12, 5}, {12, 12},
                {7, 7}, {7, 10}, {10, 7}, {10, 10},
                {9, 4}, {9, 13}, {4, 9}, {13, 9}
        };

        for (int[] pos : posFrutas) {
            nivel[pos[0]][pos[1]] = MapPanel.FRUTA;
        }

        // Enemigos
        nivel[5][9] = MapPanel.ENEMIGO;
        nivel[12][9] = MapPanel.ENEMIGO;

        // Jugadores
        nivel[2][2] = MapPanel.JUGADOR1;
        nivel[15][15] = MapPanel.JUGADOR2;

        return nivel;
    }

    /**
     * Nivel 3 - Máxima dificultad
     */
    public static int[][] getNivel3() {
        int[][] nivel = new int[18][18];

        // Inicializar todo vacío
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                nivel[i][j] = MapPanel.VACIO;
            }
        }

        // Crear bordes
        for (int i = 0; i < 18; i++) {
            nivel[i][0] = MapPanel.PARED_BORDE;
            nivel[i][17] = MapPanel.PARED_BORDE;
            nivel[0][i] = MapPanel.PARED_BORDE;
            nivel[17][i] = MapPanel.PARED_BORDE;
        }

        // Laberinto complejo
        for (int i = 1; i < 17; i++) {
            nivel[i][1] = MapPanel.HIELO_ROMPIBLE;
            nivel[i][16] = MapPanel.HIELO_ROMPIBLE;
        }

        // Cuadrado interior
        for (int i = 5; i < 13; i++) {
            nivel[i][5] = MapPanel.HIELO_ROMPIBLE;
            nivel[i][12] = MapPanel.HIELO_ROMPIBLE;
            nivel[5][i] = MapPanel.HIELO_ROMPIBLE;
            nivel[12][i] = MapPanel.HIELO_ROMPIBLE;
        }

        // Aberturas en el cuadrado
        nivel[5][9] = MapPanel.VACIO;
        nivel[12][9] = MapPanel.VACIO;
        nivel[9][5] = MapPanel.VACIO;
        nivel[9][12] = MapPanel.VACIO;

        // Muchas frutas distribuidas
        int[][] posFrutas = {
                {2, 3}, {2, 14}, {15, 3}, {15, 14},
                {3, 8}, {3, 9}, {14, 8}, {14, 9},
                {6, 6}, {6, 11}, {11, 6}, {11, 11},
                {7, 8}, {7, 9}, {10, 8}, {10, 9},
                {8, 7}, {8, 10}, {9, 7}, {9, 10},
                {8, 3}, {8, 14}, {9, 3}, {9, 14}
        };

        for (int[] pos : posFrutas) {
            nivel[pos[0]][pos[1]] = MapPanel.FRUTA;
        }

        // Más enemigos
        nivel[4][4] = MapPanel.ENEMIGO;
        nivel[4][13] = MapPanel.ENEMIGO;
        nivel[13][4] = MapPanel.ENEMIGO;
        nivel[13][13] = MapPanel.ENEMIGO;
        nivel[8][8] = MapPanel.ENEMIGO;
        nivel[9][9] = MapPanel.ENEMIGO;

        // Jugadores en esquinas opuestas
        nivel[2][2] = MapPanel.JUGADOR1;
        nivel[15][15] = MapPanel.JUGADOR2;

        return nivel;
    }
}
