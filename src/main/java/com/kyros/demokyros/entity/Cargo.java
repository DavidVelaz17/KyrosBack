package com.kyros.demokyros.entity;

import com.kyros.demokyros.convertors.EstatusCargoConverter;
import com.kyros.demokyros.convertors.TipoMensualidadCargoConverter;
import com.kyros.demokyros.enums.EstatusCargo;
import com.kyros.demokyros.enums.TipoMensualidadCargo;
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

import java.time.LocalDate;

@Entity
@Table(name = "cargo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo", nullable = false, updatable = false)
    private Integer idCargo;

    @Convert(converter = TipoMensualidadCargoConverter.class)
    @Column(name = "tipo_mensualidad_cargo", nullable = false, columnDefinition = "smallint")
    private TipoMensualidadCargo tipoMensualidadCargo;

    @Column(name = "concepto_cargo")
    private String conceptoCargo;

    @Column(name = "monto_total_cargo", nullable = false)
    private Double montoTotalCargo;

    @Column(name = "fecha_vencimiento_cargo", nullable = false)
    private LocalDate fechaVencimientoCargo;

    @Convert(converter = EstatusCargoConverter.class)
    @Column(name = "estatus_cargo", nullable = false, columnDefinition = "smallint")
    private EstatusCargo estatusCargo;

    @Column(name = "id_estudiante", nullable = false)
    private Integer idEstudiante;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;
}
