package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.DiaSemana;
import com.kyros.demokyros.enums.HoraAsesoria;
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
public class AsesoriaDto {

    private Integer idAsesoria;
    private DiaSemana diaAsesoria;
    private HoraAsesoria horaAsesoria;
}
