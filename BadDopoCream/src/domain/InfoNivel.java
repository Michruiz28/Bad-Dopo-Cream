package domain;

import presentation.BoardPanel;

import java.util.HashMap;

public class InfoNivel {

    /**
     * Retorna el mapa del nivel especificado
     */
    public static String[][] getNivel(int numeroNivel) {
        if (numeroNivel == 1) {
            return getNivel1();
        } else if (numeroNivel == 2){
            return getNivel2();
        }else {
            return null;
        }
    }

    /**
     * Nivel 1 - Basado en la imagen que compartiste
     * Ajustado según el modo de juego
     */
    public static String[][] getNivel1() {
        String[][] nivel1 = {
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"},
                {"B", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "BF", "BF", "V", "V", "V", "V", "V", "V", "V", "V", "BF", "BF", "V", "H", "B"},
                {"B", "H", "V", "BF", "H", "H", "H", "V", "V", "V", "V", "H", "H", "H", "BF", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "BF", "V", "V", "BF", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "BF", "V", "V", "V", "V", "BF", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "BF", "V", "V", "V", "V", "BF", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "BF", "V", "V", "BF", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "BF", "H", "H", "H", "V", "V", "V", "V", "H", "H", "H", "BF", "V", "H", "B"},
                {"B", "H", "V", "BF", "BF", "V", "V", "V", "V", "V", "V", "V", "V", "BF", "BF", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "T", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "B"},
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"}
        };

        return nivel1;
    }

    public static HashMap<String, Integer> getFrutasNivel(int nivel){
        if (nivel == 1){
            return getFrutasNivel1();
        } else if(nivel == 2){
            return getFrutasNivel2();
        }
        return null;
    }

    //public static HashMap<Integer, String> getEnemigosNivel(int nivel){
        //    if (nivel == 1){
            //        return getEnemigosNivel1();
            //    } else if(nivel == 2){
            //        return getEnemigosNivel2();
    //    }
    //    return null;
    //}

    public static HashMap<String, Integer> getFrutasNivel1(){
        HashMap<String, Integer> frutas =  new HashMap<>();

        frutas.put("Platano", 20);
        frutas.put("Uva", 16);
        return  frutas;
    }

    public static int[][] getPosicionesHelados(int nivel){
        if (nivel == 1){
            return getPosicionesHelados1();
        } else if(nivel == 2){
            return null;
        } else {
            return null;
        }
    }

    public static int[][] getPosicionesHelados1() {
        return new int[][] {
                {15, 7},  
                {15, 8}
        };
    }


    public static HashMap<String, Integer> getFrutasNivel2(){
        HashMap<String, Integer> frutas =  new HashMap<>();
        return frutas;
    }

    /**
     * Nivel 2 - Más difícil que el nivel 1
     */
    public static String[][] getNivel2() {
        String[][] nivel2 = {
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"},
                {"B", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "BF", "BF", "V", "V", "V", "V", "V", "V", "V", "V", "BF", "BF", "V", "H", "B"},
                {"B", "H", "V", "BF", "H", "H", "H", "V", "V", "V", "V", "H", "H", "H", "BF", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "BF", "V", "V", "BF", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H"},
                {"B", "H", "V", "V", "H", "V", "BF", "V", "V", "V", "V", "BF", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "BF", "V", "V", "V", "V", "BF", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "BF", "V", "V", "BF", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "BF", "H", "H", "H", "V", "V", "V", "V", "H", "H", "H", "BF", "V", "H", "B"},
                {"B", "H", "V", "BF", "BF", "V", "V", "V", "V", "V", "V", "V", "V", "BF", "BF", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "T", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "B"},
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"}
        };

        return nivel2;
    }
}