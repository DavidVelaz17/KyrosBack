package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.IngresoA;
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
public class EstudianteDestinoDto {

    private Integer idRelacion;
    private Integer idDestino;
    private String nombreDestino;
    private IngresoA tipo;
}
