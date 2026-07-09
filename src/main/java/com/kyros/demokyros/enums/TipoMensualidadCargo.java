package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum TipoMensualidadCargo {

    PAGO_COMPLETO(1),
    BIMESTRE(2),
    MENSUALIDAD(3),
    POR_HORA(4);

    private final int codigo;

    TipoMensualidadCargo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoMensualidadCargo fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(tipo -> tipo.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de tipo de mensualidad no válido: " + codigo));
    }
}
