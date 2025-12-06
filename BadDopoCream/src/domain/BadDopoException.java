package domain;
public class BadDopoException extends Exception {
    public static final String SABOR_INVALIDO = "El sabor del helado no puede ser nulo o vacío";
    public static final String TABLERO_NULO = "No se puede asignar un tablero nulo al helado";
    public static final String GRAFO_NULO = "No se puede asignar un grafo nulo al helado";
    public static final String TABLERO_NO_CONFIGURADO = "Debe configurar el tablero antes de realizar esta operación";
    public static final String GRAFO_NO_CONFIGURADO = "Debe configurar el grafo antes de realizar esta operación";
    public static final String CONFIGURACION_INCOMPLETA = "El helado no está completamente configurado. Faltan tablero o grafo";
    public static final String POSICION_FUERA_DE_RANGO = "La posición está fuera de los límites del tablero";
    public static final String POSICION_NO_TRANSITABLE = "La posición no es transitable";
    public static final String DIRECCION_INVALIDA = "La dirección no puede ser nula o vacía";
    public static final String DIRECCION_DESCONOCIDA = "La dirección no es válida. Use: ARRIBA, ABAJO, DERECHA, IZQUIERDA";
    public static final String MOVIMIENTO_INVALIDO = "No se puede realizar el movimiento. La celda destino no es válida";
    public static final String NO_HAY_HIELO_PARA_ROMPER = "No hay bloques de hielo adyacentes para romper";
    public static final String ERROR_AL_ROMPER_HIELO = "Ocurrió un error al intentar romper el hielo";
    public static final String CELDA_INVALIDA = "La celda no existe en el tablero";
    public static final String NO_SE_PUEDE_CREAR_HIELO = "Solo se puede crear hielo en celdas vacías";
    public static final String FRUTA_NULA = "No se puede comer una fruta nula";
    public static final String FRUTA_FUERA_DE_ALCANCE = "La fruta no está en la misma posición que el helado";
    public static final String GANANCIA_NULA = "No se puede tomar una ganancia nula";
    public static final String GANANCIA_FUERA_DE_ALCANCE = "La ganancia no está en la misma posición que el helado";
    public static final String NIVEL_INVALIDO = "El nivel debe ser mayor o igual a 1";
    public static final String PUNTAJE_INVALIDO = "El puntaje no puede ser negativo";
    public static final String POSICION_INVALIDA = "Fila o columna ingresadas inválidas";
    public static final String TIPO_NO_IDENTIFICADO = "Tipo de elemento no identificado";
    public BadDopoException(String message) {
        super(message);
    }
}