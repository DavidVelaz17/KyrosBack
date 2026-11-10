package com.kyros.demokyros.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Cuerpo de eliminar y restaurar un egreso. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgresoJustificacionRequest {

    @NotBlank(message = "La justificación es obligatoria")
    @Size(max = 500, message = "La justificación no puede pasar de 500 caracteres")
    private String justificacion;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
