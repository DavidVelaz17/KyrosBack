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
public class EstudianteBachilleratoDto {

    private Integer idEstudianteBachillerato;
    private Integer idEstudiante;
    private BachilleratoDto bachillerato;
}
