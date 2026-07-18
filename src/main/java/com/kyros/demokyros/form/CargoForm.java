package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.EstatusCargo;
import com.kyros.demokyros.enums.TipoMensualidadCargo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoForm {

    @NotNull(message = "El tipo de mensualidad es obligatorio")
    private TipoMensualidadCargo tipoMensualidadCargo;

    private String conceptoCargo;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser un valor positivo")
    private Double montoTotalCargo;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimientoCargo;

    @NotNull(message = "El estatus del cargo es obligatorio")
    private EstatusCargo estatusCargo;

    @NotNull(message = "El estudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "El usuario es obligatorio")
    private Integer idUsuario;
}
