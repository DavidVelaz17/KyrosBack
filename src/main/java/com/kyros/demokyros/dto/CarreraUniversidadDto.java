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
public class CarreraUniversidadDto {

    private Integer idCarreraUniversidad;
    private CarreraDto carrera;
    private UniversidadDto universidad;
}
