package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.Horario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoDto {

    private Integer idGrupo;
    private String nombreGrupo;
    private LocalDate fechaInicio;
    private String nombrePlantel;
    private Horario horario;
}
