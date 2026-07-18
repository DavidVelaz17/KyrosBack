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
public class CursoVeranoForm {

    @NotBlank(message = "El nombre del curso de verano es obligatorio")
    private String nombreCursoVerano;
}
