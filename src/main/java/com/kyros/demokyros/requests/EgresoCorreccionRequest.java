package com.kyros.demokyros.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Los mismos campos y validaciones que EgresoForm, más la justificación y la contraseña. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgresoCorreccionRequest {

    @NotNull(message = "El tipo de egreso es obligatorio")
    private Integer idTipoEgreso;

    @NotNull(message = "El plantel es obligatorio")
    private Integer idPlantel;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser un valor positivo")
    private Double importeEgreso;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHoraEgreso;

    @Size(max = 500, message = "Las observaciones no pueden pasar de 500 caracteres")
    private String observacionesEgreso;

    @NotBlank(message = "La justificación es obligatoria")
    @Size(max = 500, message = "La justificación no puede pasar de 500 caracteres")
    private String justificacion;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
