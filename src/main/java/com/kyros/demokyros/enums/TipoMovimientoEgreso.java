package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum TipoMovimientoEgreso {

    CORRECCION(1),
    ELIMINACION(2),
    RESTAURACION(3);

    private final int codigo;

    TipoMovimientoEgreso(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoMovimientoEgreso fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(tipo -> tipo.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de movimiento de egreso no válido: " + codigo));
    }
}
