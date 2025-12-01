package domain;

import java.util.List;

public class Hungry implements EstrategiaMovimiento {

    @Override
    public Direccion calcularMovimiento(Helado helado, Nivel nivel) throws BadDopoException {

        List<Fruta> frutas = nivel.getFrutas();
        if (frutas.isEmpty()) return Direccion.NINGUNA;

        Fruta objetivo = frutas.get(0);

        int hf = helado.getFila();
        int hc = helado.getColumna();
        int ff = objetivo.getFila();
        int fc = objetivo.getColumna();

        if (ff < hf) return Direccion.ARRIBA;
        if (ff > hf) return Direccion.ABAJO;
        if (fc < hc) return Direccion.IZQUIERDA;
        if (fc > hc) return Direccion.DERECHA;

        return Direccion.NINGUNA;
    }
}
