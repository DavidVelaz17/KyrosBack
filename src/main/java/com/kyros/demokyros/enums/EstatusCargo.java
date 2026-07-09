package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum EstatusCargo {

    PENDIENTE(1),
    PARCIAL(2),
    PAGADO(3),
    VENCIDO(4);

    private final int codigo;

    EstatusCargo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static EstatusCargo fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(estatus -> estatus.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de estatus de cargo no válido: " + codigo));
    }
}
