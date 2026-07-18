package com.kyros.demokyros.form;

import jakarta.validation.constraints.NotNull;
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
public class EstudianteCursoVeranoForm {

    @NotNull(message = "El estudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "El curso de verano es obligatorio")
    private Integer idCursoVerano;
}
