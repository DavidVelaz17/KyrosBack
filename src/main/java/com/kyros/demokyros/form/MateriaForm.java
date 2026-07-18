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
public class MateriaForm {

    @NotBlank(message = "El nombre de la materia es obligatorio")
    private String nombreMateria;
}
