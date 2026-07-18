package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum MetodoPago {

    EFECTIVO(1),
    TRANSFERENCIA(2);

    private final int codigo;

    MetodoPago(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static MetodoPago fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(metodo -> metodo.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de método de pago no válido: " + codigo));
    }
}
