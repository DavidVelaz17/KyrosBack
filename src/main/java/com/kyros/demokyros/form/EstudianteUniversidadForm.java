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
public class EstudianteUniversidadForm {

    @NotNull(message = "El estudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "La universidad es obligatoria")
    private Integer idUniversidad;
}
