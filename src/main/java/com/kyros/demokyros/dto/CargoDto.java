package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.EstatusCargo;
import com.kyros.demokyros.enums.TipoMensualidadCargo;
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
public class CargoDto {

    private Integer idCargo;
    private TipoMensualidadCargo tipoMensualidadCargo;
    private String conceptoCargo;
    private Double montoTotalCargo;
    private LocalDate fechaVencimientoCargo;
    private EstatusCargo estatusCargo;
    private EstudianteDto estudiante;
    private UsuarioDto usuario;
}
