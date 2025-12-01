package domain;

import java.util.List;

public class Fearful implements EstrategiaMovimiento {

    @Override
    public Direccion calcularMovimiento(Helado helado, Nivel nivel) throws BadDopoException {

        List<Enemigo> enemigos = nivel.getEnemigos();
        if (enemigos.isEmpty()) return Direccion.NINGUNA;

        Enemigo e = enemigos.get(0);

        int hf = helado.getFila();
        int hc = helado.getColumna();
        int ef = e.getFila();
        int ec = e.getColumna();

        if (ef < hf) return Direccion.ABAJO;
        if (ef > hf) return Direccion.ARRIBA;
        if (ec < hc) return Direccion.DERECHA;
        if (ec > hc) return Direccion.IZQUIERDA;

        return Direccion.NINGUNA;
    }
}
