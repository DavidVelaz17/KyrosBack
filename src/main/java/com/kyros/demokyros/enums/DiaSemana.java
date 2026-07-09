package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum DiaSemana {

    LUNES(1),
    MARTES(2),
    MIERCOLES(3),
    JUEVES(4),
    VIERNES(5),
    SABADO(6);

    private final int codigo;

    DiaSemana(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static DiaSemana fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(diaSemana -> diaSemana.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de día no válido: " + codigo));
    }
}
