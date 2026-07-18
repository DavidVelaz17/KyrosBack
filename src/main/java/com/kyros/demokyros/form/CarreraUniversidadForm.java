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
public class CarreraUniversidadForm {

    @NotNull(message = "La carrera es obligatoria")
    private Integer idCarrera;

    @NotNull(message = "La universidad es obligatoria")
    private Integer idUniversidad;
}
