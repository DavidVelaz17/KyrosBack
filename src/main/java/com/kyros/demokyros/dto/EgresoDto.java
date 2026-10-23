package com.kyros.demokyros.dto;

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
}
