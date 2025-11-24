package domain;
// Nodo.java
import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final Celda celda;
    private final List<Nodo> vecinos = new ArrayList<>();

    public Nodo(Celda celda) {
        this.celda = celda;
    }

    public Celda getCelda() { return celda; }
    public List<Nodo> getVecinos() { return vecinos; }

    public void agregarVecino(Nodo n) {
        if (n != null && !vecinos.contains(n)) vecinos.add(n);
    }

    @Override
    public String toString() {
        return "Nodo " + celda.toString();
    }
}
