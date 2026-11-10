package com.kyros.demokyros.enums;

import java.util.Arrays;

/** Refleja el último movimiento del egreso (ver EgresoMovimiento). Solo ELIMINADO lo saca de los
 *  totales y del reporte; los demás cuentan igual. */
public enum EstatusEgreso {

    SIN_CAMBIOS(1),
    CORREGIDO(2),
    ELIMINADO(3),
    RESTAURADO(4);

    private final int codigo;

    EstatusEgreso(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static EstatusEgreso fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(estatus -> estatus.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de estatus de egreso no válido: " + codigo));
    }
}
