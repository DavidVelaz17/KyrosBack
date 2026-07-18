package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.EstatusEstudiante;
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
public class EstudianteEstatusForm {

    @NotNull(message = "El estatus del alumno es obligatorio")
    private EstatusEstudiante estatus;
}
