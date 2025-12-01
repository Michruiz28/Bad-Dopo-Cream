package domain;

import java.util.List;

public class ExpertIA implements EstrategiaMovimiento {

    @Override
    public Direccion calcularMovimiento(Helado helado, Nivel nivel) throws BadDopoException {

        List<Enemigo> enemigos = nivel.getEnemigos();
        List<Fruta> frutas = nivel.getFrutas();

        int hf = helado.getFila();
        int hc = helado.getColumna();

        for (Enemigo e : enemigos) {
            int df = Math.abs(e.getFila() - hf);
            int dc = Math.abs(e.getColumna() - hc);

            if (df + dc <= 2) {
                if (e.getFila() < hf) return Direccion.ABAJO;
                if (e.getFila() > hf) return Direccion.ARRIBA;
                if (e.getColumna() < hc) return Direccion.DERECHA;
                return Direccion.IZQUIERDA;
            }
        }

        if (!frutas.isEmpty()) {
            Fruta objetivo = frutas.get(0);
            int ff = objetivo.getFila();
            int fc = objetivo.getColumna();

            if (ff < hf) return Direccion.ARRIBA;
            if (ff > hf) return Direccion.ABAJO;
            if (fc < hc) return Direccion.IZQUIERDA;
            if (fc > hc) return Direccion.DERECHA;
        }

        return Direccion.NINGUNA;
    }
}
