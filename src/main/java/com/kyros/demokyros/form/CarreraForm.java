package com.kyros.demokyros.form;

import jakarta.validation.constraints.NotBlank;
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
public class CarreraForm {

    @NotBlank(message = "El nombre de la carrera es obligatorio")
    private String nombreCarrera;

    @NotNull(message = "El área es obligatoria")
    private Integer idArea;
}
