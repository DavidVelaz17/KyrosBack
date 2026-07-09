package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.MetodoPago;
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
public class PagoDto {

    private Integer idPago;
    private Double montoPagadoPago;
    private LocalDate fechaPago;
    private MetodoPago metodoPago;
    private CargoDto cargo;
}
