package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.EstatusEgreso;
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
public class EgresoDto {

    private Integer idEgreso;
    private TipoEgresoDto tipoEgreso;
    private Double importeEgreso;
    private LocalDateTime fechaHoraEgreso;
    private String observacionesEgreso;
    /** Solo id y nombre de quien lo registró (no el UsuarioDto completo con su dirección). */
    private Integer idUsuario;
    private String nombreUsuario;
    /** Null en egresos registrados antes de que existiera el plantel. */
    private PlantelDto plantel;
    private EstatusEgreso estatusEgreso;
    private LocalDateTime fechaRegistroEgreso;
    /** La última corrección/eliminación/restauración (quién, cuándo y por qué), o null si está
     *  SIN_CAMBIOS. El historial completo se pide aparte (GET /api/egresos/{id}/movimientos). */
    private EgresoMovimientoDto ultimoMovimiento;
}
