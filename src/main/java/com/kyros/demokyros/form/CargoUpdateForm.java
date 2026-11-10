package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.TipoMensualidadCargo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Lo que se puede corregir de un cargo desde "Registrar pago → Cargo pendiente". */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoUpdateForm {

    @NotNull(message = "El tipo de mensualidad es obligatorio")
    private TipoMensualidadCargo tipoMensualidadCargo;

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser un valor positivo")
    private Double montoTotalCargo;
}
