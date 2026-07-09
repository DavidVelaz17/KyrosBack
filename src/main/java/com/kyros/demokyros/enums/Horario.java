package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum Horario {

    ESCOLARIZADO(1),
    SABATINO(2),
    VIRTUAL(3);

    private final int codigo;

    Horario(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static Horario fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(horario -> horario.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de horario no válido: " + codigo));
    }
}
