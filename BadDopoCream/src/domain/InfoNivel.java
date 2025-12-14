package domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que almacena la información estática de cada nivel.
 * Cada nivel puede tener múltiples fases (olas de frutas).
 */
public class InfoNivel {

    /**
     * Clase interna para representar una fase del nivel
     */
    public static class FaseNivel {
        private HashMap<String, Integer> frutasRequeridas;
        private ArrayList<PosicionFruta> posicionesFrutas;

        public FaseNivel() {
            this.frutasRequeridas = new HashMap<>();
            this.posicionesFrutas = new ArrayList<>();
        }

        public void agregarFruta(String tipo, int fila, int col) {
            posicionesFrutas.add(new PosicionFruta(tipo, fila, col));
            frutasRequeridas.put(tipo, frutasRequeridas.getOrDefault(tipo, 0) + 1);
        }

        public HashMap<String, Integer> getFrutasRequeridas() {
            return new HashMap<>(frutasRequeridas);
        }

        public ArrayList<PosicionFruta> getPosicionesFrutas() {
            return new ArrayList<>(posicionesFrutas);
        }
    }

    /**
     * Clase para almacenar posición y tipo de fruta
     */
    public static class PosicionFruta {
        public String tipo;
        public int fila;
        public int columna;

        public PosicionFruta(String tipo, int fila, int columna) {
            this.tipo = tipo;
            this.fila = fila;
            this.columna = columna;
        }
    }

    /**
     * Retorna el mapa BASE del nivel (sin frutas, solo estructura)
     */
    public static String[][] getNivelBase(int numeroNivel) {
        if (numeroNivel == 0) {
            return getNivel0Base();
        } else if (numeroNivel == 1) {
            return getNivel1Base();
        }
        return null;
    }

    /**
     * Retorna todas las fases de un nivel
     */
    public static ArrayList<FaseNivel> getFasesNivel(int numeroNivel) {
        if (numeroNivel == 0) {
            return getFasesNivel0();
        } else if (numeroNivel == 1) {
            return getFasesNivel1();
        }
        return null;
    }

    /**
     * Nivel 1 - Mapa base (sin frutas)
     */
    private static String[][] getNivel0Base() {
        String[][] nivel0 = {
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"},
                {"B", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "H", "H", "V", "V", "V", "V", "H", "H", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "V", "V", "V", "V", "V", "V", "V", "V", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "H", "H", "H", "V", "V", "V", "V", "H", "H", "H", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "V", "V", "V", "T", "V", "V", "V", "V", "V", "V", "V", "V", "V", "V", "H", "B"},
                {"B", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "H", "B"},
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"}
        };
        return nivel0;
    }

    /**
     * Nivel 1 - Fases con posiciones exactas de frutas
     */
    private static ArrayList<FaseNivel> getFasesNivel0() {
        ArrayList<FaseNivel> fases = new ArrayList<>();

        // FASE 1: BANANOS (BF)
        FaseNivel fase1 = new FaseNivel();
        // Posiciones según tu documento
        fase1.agregarFruta("BF", 3, 3);
        fase1.agregarFruta("BF", 3, 4);
        fase1.agregarFruta("BF", 3, 13);
        fase1.agregarFruta("BF", 3, 14);
        fase1.agregarFruta("BF", 4, 3);
        fase1.agregarFruta("BF", 4, 14);
        fase1.agregarFruta("BF", 5, 7);
        fase1.agregarFruta("BF", 5, 10);
        fase1.agregarFruta("BF", 7, 6);
        fase1.agregarFruta("BF", 7, 11);
        fase1.agregarFruta("BF", 8, 6);
        fase1.agregarFruta("BF", 8, 11);
        fase1.agregarFruta("BF", 10, 7);
        fase1.agregarFruta("BF", 10, 10);
        fase1.agregarFruta("BF", 11, 3);
        fase1.agregarFruta("BF", 11, 14);
        fase1.agregarFruta("BF", 12, 3);
        fase1.agregarFruta("BF", 12, 4);
        fase1.agregarFruta("BF", 12, 13);
        fase1.agregarFruta("BF", 12, 14);
        fases.add(fase1);

        // FASE 2: UVAS (U)
        FaseNivel fase2 = new FaseNivel();
        fase2.agregarFruta("U", 2, 2);
        fase2.agregarFruta("U", 2, 3);
        fase2.agregarFruta("U", 2, 14);
        fase2.agregarFruta("U", 2, 15);
        fase2.agregarFruta("U", 3, 2);
        fase2.agregarFruta("U", 3, 15);
        fase2.agregarFruta("U", 5, 5);
        fase2.agregarFruta("U", 5, 12);
        fase2.agregarFruta("U", 10, 5);
        fase2.agregarFruta("U", 10, 12);
        fase2.agregarFruta("U", 13, 2);
        fase2.agregarFruta("U", 13, 15);
        fase2.agregarFruta("U", 14, 2);
        fase2.agregarFruta("U", 14, 3);
        fase2.agregarFruta("U", 14, 14);
        fase2.agregarFruta("U", 14, 15);
        fases.add(fase2);

        return fases;
    }

    /**
     * Nivel 2 - Mapa base
     */
    private static String[][] getNivel1Base() {
        String[][] nivel1 = {
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"},
                {"B", "V", "V", "V", "V", "V", "V", "B", "V", "V", "V", "V", "V", "V", "B"},
                {"B", "V", "M", "V", "V", "V", "V", "B", "V", "V", "V", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "V", "V", "V", "B", "V", "V", "V", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "H", "H", "H", "H", "H", "H", "H", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "H", "H", "H", "H", "H", "H", "H", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "H", "H", "H", "H", "H", "H", "H", "V", "V", "V", "B"},
                {"B", "B", "B", "B", "H", "H", "H", "C", "H", "H", "H", "B", "B", "B", "B"},
                {"B", "V", "V", "V", "H", "H", "H", "H", "H", "H", "H", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "H", "H", "H", "H", "H", "H", "H", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "H", "H", "H", "H", "H", "H", "H", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "V", "V", "V", "B", "V", "V", "V", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "V", "V", "V", "B", "V", "V", "V", "V", "V", "V", "B"},
                {"B", "V", "V", "V", "V", "V", "V", "B", "V", "V", "V", "V", "V", "V", "B"},
                {"B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B", "B"}
        };
        return nivel1;
    }

    /**
     * Nivel 2 - Fases
     */
    private static ArrayList<FaseNivel> getFasesNivel1() {
        ArrayList<FaseNivel> fases = new ArrayList<>();

        // FASE 1: CEREZAS
        FaseNivel fase1 = new FaseNivel();
        fase1.agregarFruta("CF", 1, 9);
        fase1.agregarFruta("CF", 1, 11);
        fase1.agregarFruta("CF", 1, 13);
        fase1.agregarFruta("CF", 2, 8);
        fase1.agregarFruta("CF", 2, 10);
        fase1.agregarFruta("CF", 2, 12);
        fase1.agregarFruta("CF", 3, 9);
        fase1.agregarFruta("CF", 3, 11);
        fase1.agregarFruta("CF", 3, 13);
        fase1.agregarFruta("CF", 4, 12);
        fase1.agregarFruta("CF", 5, 11);
        fase1.agregarFruta("CF", 5, 13);
        fase1.agregarFruta("CF", 8, 2);
        fase1.agregarFruta("CF", 9, 1);
        fase1.agregarFruta("CF", 9, 3);
        fase1.agregarFruta("CF", 10, 2);
        fase1.agregarFruta("CF", 11, 1);
        fase1.agregarFruta("CF", 11, 3);
        fase1.agregarFruta("CF", 11, 5);
        fase1.agregarFruta("CF", 12, 2);
        fase1.agregarFruta("CF", 12, 4);
        fase1.agregarFruta("CF", 12, 6);
        fase1.agregarFruta("CF", 13, 1);
        fase1.agregarFruta("CF", 13, 3);
        fase1.agregarFruta("CF", 13, 5);
        fases.add(fase1);

        // FASE 2: PIÑAS
        FaseNivel fase2 = new FaseNivel();
        fase2.agregarFruta("P", 1, 1);
        fase2.agregarFruta("P", 1, 3);
        fase2.agregarFruta("P", 1, 5);
        fase2.agregarFruta("P", 2, 2);
        fase2.agregarFruta("P", 2, 4);
        fase2.agregarFruta("P", 2, 6);
        fase2.agregarFruta("P", 3, 1);
        fase2.agregarFruta("P", 3, 3);
        fase2.agregarFruta("P", 3, 5);
        fase2.agregarFruta("P", 4, 2);
        fase2.agregarFruta("P", 4, 4);
        fase2.agregarFruta("P", 5, 1);
        fase2.agregarFruta("P", 5, 3);
        fase2.agregarFruta("P", 6, 2);
        fase2.agregarFruta("P", 6, 4);
        fase2.agregarFruta("P", 8, 12);
        fase2.agregarFruta("P", 9, 11);
        fase2.agregarFruta("P", 9, 13);
        fase2.agregarFruta("P", 10, 12);
        fase2.agregarFruta("P", 11, 9);
        fase2.agregarFruta("P", 11, 11);
        fase2.agregarFruta("P", 11, 13);
        fase2.agregarFruta("P", 12, 8);
        fase2.agregarFruta("P", 12, 10);
        fase2.agregarFruta("P", 12, 12);
        fase2.agregarFruta("P", 13, 9);
        fase2.agregarFruta("P", 13, 11);
        fase2.agregarFruta("P", 13, 13);
        fases.add(fase2);

        return fases;
    }

    /**
     * Posiciones iniciales de los helados
     */
    public static int[][] getPosicionesHelados(int nivel) {
        if (nivel == 0) {
            return new int[][] {
                    {12, 8},
                    {12, 9}
            };
        } else if (nivel == 1) {
            return new int[][] {
                    {12, 12},
                    {12, 10}
            };
        }
        return null;
    }

    // ===== MÉTODOS DE COMPATIBILIDAD (deprecated) =====

    /**
     * @deprecated Usar getNivelBase() y getFasesNivel()
     */
    @Deprecated
    public static String[][] getNivel(int numeroNivel) {
        return getNivelBase(numeroNivel);
    }

    /**
     * @deprecated Usar getFasesNivel()
     */
    @Deprecated
    public static HashMap<String, Integer> getFrutasNivel(int nivel) {
        ArrayList<FaseNivel> fases = getFasesNivel(nivel);
        if (fases == null || fases.isEmpty()) return null;

        HashMap<String, Integer> totalFrutas = new HashMap<>();
        for (FaseNivel fase : fases) {
            HashMap<String, Integer> frutasFase = fase.getFrutasRequeridas();
            for (String tipo : frutasFase.keySet()) {
                totalFrutas.put(tipo, totalFrutas.getOrDefault(tipo, 0) + frutasFase.get(tipo));
            }
        }
        return totalFrutas;
    }
}