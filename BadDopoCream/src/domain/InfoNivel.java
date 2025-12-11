package domain;

import presentation.BoardPanel;

public class InfoNivel {

    /**
     * Retorna el mapa del nivel especificado
     */
    public static int[][] getNivel(int numeroNivel, String modo) {
        switch (numeroNivel) {
            case 1:
                return getNivel1(modo);
            case 2:
                return getNivel2(modo);
            case 3:
                return getNivel3(modo);
            default:
                return getNivel1(modo);
        }
    }

    /**
     * Nivel 1 - Basado en la imagen que compartiste
     * Ajustado según el modo de juego
     */
    public static int[][] getNivel1(String modo) {
        int[][] nivel = new int[18][18];

        // Inicializar todo vacío
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                nivel[i][j] = BoardPanel.VACIO;
            }
        }

        // Crear bordes con paredes
        for (int i = 0; i < 18; i++) {
            nivel[i][0] = BoardPanel.PARED_BORDE;
            nivel[i][17] =BoardPanel.PARED_BORDE;
            nivel[0][i] = BoardPanel.PARED_BORDE;
            nivel[17][i] = BoardPanel.PARED_BORDE;
        }

        // Columnas de hielo rompible (azul claro) - Columnas 1 y 16
        for (int i = 1; i < 17; i++) {
            nivel[i][1] = BoardPanel.HIELO_ROMPIBLE;
            nivel[i][16] = BoardPanel.HIELO_ROMPIBLE;
        }

        // Filas de hielo rompible - Filas 1 y 16
        for (int j = 2; j < 16; j++) {
            nivel[1][j] = BoardPanel.HIELO_ROMPIBLE;
            nivel[16][j] = BoardPanel.HIELO_ROMPIBLE;
        }

        // Columna vertical de hielo en el centro izquierdo (columna 5)
        for (int i = 5; i < 13; i++) {
            nivel[i][4] = BoardPanel.HIELO_ROMPIBLE;
        }

        // Columna vertical de hielo en el centro derecho (columna 12)
        for (int i = 5; i < 13; i++) {
            nivel[i][13] = BoardPanel.HIELO_ROMPIBLE;
        }

        for (int i = 4; i < 7; i++){
            nivel[5][i] = BoardPanel.HIELO_ROMPIBLE;
        }

        for (int i = 4; i < 7; i++){
            nivel[12][i] = BoardPanel.HIELO_ROMPIBLE;
        }

        for (int i = 11; i < 13; i++){
            nivel[5][i] = BoardPanel.HIELO_ROMPIBLE;
        }

        for (int i = 11; i < 13; i++){
            nivel[12][i] = BoardPanel.HIELO_ROMPIBLE;
        }
        // Frutas - Esquina superior izquierda (posición aproximada 4,3 y 4,4)
        nivel[4][3] = BoardPanel.FRUTA;
        nivel[5][3] = BoardPanel.FRUTA;
        nivel[4][4] = BoardPanel.FRUTA;

        // Frutas - Esquina superior derecha (posición aproximada 4,13 y 4,14)
        nivel[4][14] = BoardPanel.FRUTA;
        nivel[4][13] = BoardPanel.FRUTA;
        nivel[5][14] = BoardPanel.FRUTA;

        // Frutas - Centro superior
        nivel[6][7] = BoardPanel.FRUTA;
        nivel[6][10] = BoardPanel.FRUTA;

        // Frutas - Centro medio
        nivel[8][6] = BoardPanel.FRUTA;
        nivel[9][6] = BoardPanel.FRUTA;
        nivel[8][11] =  BoardPanel.FRUTA;
        nivel[9][11] = BoardPanel.FRUTA;

        // Frutas - Centro bajo
        nivel[11][7] = BoardPanel.FRUTA;
        nivel[11][10] = BoardPanel.FRUTA;

        // Frutas - Esquina inferior izquierda
        nivel[13][4] = BoardPanel.FRUTA;
        nivel[12][3] = BoardPanel.FRUTA;
        nivel[13][3] = BoardPanel.FRUTA;

        // Frutas - Esquina inferior derecha
        nivel[13][13] = BoardPanel.FRUTA;
        nivel[12][14] = BoardPanel.FRUTA;
        nivel[13][14] = BoardPanel.FRUTA;

        // Jugadores - Centro abajo (posición aproximada fila 14, columnas 8 y 9)
        nivel[14][8] = BoardPanel.JUGADOR1;
        nivel[14][9] = BoardPanel.JUGADOR2;

        return nivel;
    }


    /**
     * Nivel 2 - Más difícil que el nivel 1
     */
    public static int[][] getNivel2(String modo) {
        int[][] nivel = new int[18][18];

        // Inicializar todo vacío
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                nivel[i][j] = BoardPanel.VACIO;
            }
        }

        // Crear bordes
        for (int i = 0; i < 18; i++) {
            nivel[i][0] = BoardPanel.PARED_BORDE;
            nivel[i][17] = BoardPanel.PARED_BORDE;
            nivel[0][i] = BoardPanel.PARED_BORDE;
            nivel[17][i] = BoardPanel.PARED_BORDE;
        }

        // Crear laberinto con hielo
        for (int i = 1; i < 17; i++) {
            nivel[i][1] = BoardPanel.HIELO_ROMPIBLE;
            nivel[i][16] = BoardPanel.HIELO_ROMPIBLE;
        }

        // Paredes internas en forma de cruz
        for (int i = 4; i < 14; i++) {
            if (i != 9) {
                nivel[i][9] = BoardPanel.HIELO_ROMPIBLE;
                nivel[9][i] = BoardPanel.HIELO_ROMPIBLE;
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
            nivel[pos[0]][pos[1]] = BoardPanel.FRUTA;
        }

        // Enemigos
        nivel[5][9] = BoardPanel.ENEMIGO;
        nivel[12][9] = BoardPanel.ENEMIGO;

        // Jugadores según modo
        if (modo == null || modo.equals("Un solo jugador")) {
            nivel[2][2] = BoardPanel.JUGADOR1;
        } else {
            nivel[2][2] = BoardPanel.JUGADOR1;
            nivel[15][15] = BoardPanel.JUGADOR2;
        }

        return nivel;
    }

    /**
     * Nivel 3 - Máxima dificultad
     */
    public static int[][] getNivel3(String modo) {
        int[][] nivel = new int[18][18];

        // Inicializar todo vacío
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 18; j++) {
                nivel[i][j] =BoardPanel.VACIO;
            }
        }

        // Crear bordes
        for (int i = 0; i < 18; i++) {
            nivel[i][0] = BoardPanel.PARED_BORDE;
            nivel[i][17] = BoardPanel.PARED_BORDE;
            nivel[0][i] = BoardPanel.PARED_BORDE;
            nivel[17][i] = BoardPanel.PARED_BORDE;
        }

        // Laberinto complejo
        for (int i = 1; i < 17; i++) {
            nivel[i][1] = BoardPanel.HIELO_ROMPIBLE;
            nivel[i][16] = BoardPanel.HIELO_ROMPIBLE;
        }

        // Cuadrado interior
        for (int i = 5; i < 13; i++) {
            nivel[i][5] = BoardPanel.HIELO_ROMPIBLE;
            nivel[i][12] = BoardPanel.HIELO_ROMPIBLE;
            nivel[5][i] = BoardPanel.HIELO_ROMPIBLE;
            nivel[12][i] = BoardPanel.HIELO_ROMPIBLE;
        }

        // Aberturas en el cuadrado
        nivel[5][9] = BoardPanel.VACIO;
        nivel[12][9] = BoardPanel.VACIO;
        nivel[9][5] = BoardPanel.VACIO;
        nivel[9][12] = BoardPanel.VACIO;

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
            nivel[pos[0]][pos[1]] = BoardPanel.FRUTA;
        }

        // Más enemigos
        nivel[4][4] = BoardPanel.ENEMIGO;
        nivel[4][13] = BoardPanel.ENEMIGO;
        nivel[13][4] = BoardPanel.ENEMIGO;
        nivel[13][13] = BoardPanel.ENEMIGO;
        nivel[8][8] = BoardPanel.ENEMIGO;
        nivel[9][9] = BoardPanel.ENEMIGO;

        // Jugadores según modo
        if (modo == null || modo.equals("Un solo jugador")) {
            nivel[2][2] = BoardPanel.JUGADOR1;
        } else {
            nivel[2][2] = BoardPanel.JUGADOR1;
            nivel[15][15] = BoardPanel.JUGADOR2;
        }

        return nivel;
    }
}