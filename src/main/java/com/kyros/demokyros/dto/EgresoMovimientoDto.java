package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.TipoMovimientoEgreso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Los campos *Anterior/*Nuevo solo vienen en una CORRECCION. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgresoMovimientoDto {

    private Integer idEgresoMovimiento;
    private Integer idEgreso;
    private TipoMovimientoEgreso tipoMovimiento;
    private LocalDateTime fechaHoraMovimiento;
    private Integer idUsuario;
    private String nombreUsuario;
    private String justificacionMovimiento;
    private TipoEgresoDto tipoEgresoAnterior;
    private TipoEgresoDto tipoEgresoNuevo;
    private Double importeAnterior;
    private Double importeNuevo;
    private LocalDateTime fechaHoraAnterior;
    private LocalDateTime fechaHoraNueva;
    private String observacionesAnteriores;
    private String observacionesNuevas;
    private PlantelDto plantelAnterior;
    private PlantelDto plantelNuevo;
}
