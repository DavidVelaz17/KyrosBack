package com.kyros.demokyros.form;

import jakarta.validation.constraints.NotBlank;
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
public class BachilleratoForm {

    @NotBlank(message = "El nombre del bachillerato es obligatorio")
    private String nombreBachillerato;
}
