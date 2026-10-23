package com.kyros.demokyros.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgresoForm {

    @NotNull(message = "El tipo de egreso es obligatorio")
    private Integer idTipoEgreso;

    @NotNull(message = "El importe es obligatorio")
    @Positive(message = "El importe debe ser un valor positivo")
    private Double importeEgreso;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHoraEgreso;

    @Size(max = 500, message = "Las observaciones no pueden pasar de 500 caracteres")
    private String observacionesEgreso;
}
