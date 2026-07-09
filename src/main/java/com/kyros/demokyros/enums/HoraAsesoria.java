package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum HoraAsesoria {

    DE_4_A_5(1, "4-5"),
    DE_5_A_6(2, "5-6"),
    DE_6_A_7(3, "6-7"),
    DE_7_A_8(4, "7-8");

    private final int codigo;
    private final String descripcion;

    HoraAsesoria(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static HoraAsesoria fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(horaAsesoria -> horaAsesoria.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de hora de asesoría no válido: " + codigo));
    }
}
