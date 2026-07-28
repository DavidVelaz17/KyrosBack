package com.kyros.demokyros.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportEstudianteErrorDto {

    /** Número de fila en el Excel (1-based, contando la fila de encabezado como fila 1). */
    private int fila;
    private String mensaje;
}
