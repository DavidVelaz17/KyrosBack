package com.kyros.demokyros.enums;

import java.util.Arrays;

public enum RolUsuario {

    ADMIN(1),
    COORDINADOR(2),
    SECRETARIO(3),
    PROFESOR(4);

    private final int codigo;

    RolUsuario(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static RolUsuario fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(rol -> rol.codigo == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de rol no válido: " + codigo));
    }
}
