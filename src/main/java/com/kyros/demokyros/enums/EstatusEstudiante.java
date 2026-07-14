package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum EstatusEstudiante {

    ACTIVO(1),
    BAJA(2);

    private final int codigo;

    EstatusEstudiante(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static EstatusEstudiante fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(estatus -> estatus.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de estatus de estudiante no válido: " + codigo));
    }
}
