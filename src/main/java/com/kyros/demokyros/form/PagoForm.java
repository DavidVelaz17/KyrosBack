package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.MetodoPago;
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
public class PagoForm {

    @NotNull(message = "El monto pagado es obligatorio")
    @Positive(message = "El monto pagado debe ser un valor positivo")
    private Double montoPagadoPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    @NotNull(message = "El cargo es obligatorio")
    private Integer idCargo;

    @NotNull(message = "El usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "Indica si el pago requiere factura")
    private Boolean requiereFactura;

    private String conceptoPago;

    private String notasPago;
}
