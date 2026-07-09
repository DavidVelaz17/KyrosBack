package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum IngresoA {

    UNIVERSIDAD(1),
    BACHILLERATO(2),
    SECUNDARIA(3),
    ASESORIAS(4),
    CURSO_VERANO(5);

    private final int codigo;

    IngresoA(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static IngresoA fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(ingresoA -> ingresoA.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de ingresoA no válido: " + codigo));
    }
}
