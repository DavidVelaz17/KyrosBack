package com.kyros.demokyros.entity;

import com.kyros.demokyros.convertors.TipoMovimientoEgresoConverter;
import com.kyros.demokyros.enums.TipoMovimientoEgreso;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Una corrección, eliminación o restauración de un egreso. Los campos *Anterior/*Nuevo solo se
 *  llenan en una CORRECCION (los cuatro, aunque no todos hayan cambiado). */
@Entity
@Table(name = "egreso_movimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EgresoMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_egreso_movimiento", nullable = false, updatable = false)
    private Integer idEgresoMovimiento;

    @Column(name = "id_egreso", nullable = false)
    private Integer idEgreso;

    @Convert(converter = TipoMovimientoEgresoConverter.class)
    @Column(name = "tipo_movimiento", nullable = false, columnDefinition = "smallint")
    private TipoMovimientoEgreso tipoMovimiento;

    // Hora local de México, igual que Egreso.fechaHoraEgreso.
    @Column(name = "fecha_hora_movimiento", nullable = false)
    private LocalDateTime fechaHoraMovimiento;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "justificacion_movimiento", nullable = false)
    private String justificacionMovimiento;

    @Column(name = "id_tipo_egreso_anterior")
    private Integer idTipoEgresoAnterior;

    @Column(name = "id_tipo_egreso_nuevo")
    private Integer idTipoEgresoNuevo;

    @Column(name = "importe_anterior")
    private Double importeAnterior;

    @Column(name = "importe_nuevo")
    private Double importeNuevo;

    @Column(name = "fecha_hora_anterior")
    private LocalDateTime fechaHoraAnterior;

    @Column(name = "fecha_hora_nueva")
    private LocalDateTime fechaHoraNueva;

    @Column(name = "observaciones_anteriores")
    private String observacionesAnteriores;

    @Column(name = "observaciones_nuevas")
    private String observacionesNuevas;

    @Column(name = "id_plantel_anterior")
    private Integer idPlantelAnterior;

    @Column(name = "id_plantel_nuevo")
    private Integer idPlantelNuevo;
}
