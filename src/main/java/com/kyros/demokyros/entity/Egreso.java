package com.kyros.demokyros.entity;

import jakarta.persistence.Column;
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

@Entity
@Table(name = "egreso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_egreso", nullable = false, updatable = false)
    private Integer idEgreso;

    @Column(name = "id_tipo_egreso", nullable = false)
    private Integer idTipoEgreso;

    @Column(name = "importe_egreso", nullable = false)
    private Double importeEgreso;

    // Hora local de México (el JVM corre con TZ=America/Mexico_City, ver docker-compose), igual que
    // la captura el usuario en el formulario.
    @Column(name = "fecha_hora_egreso", nullable = false)
    private LocalDateTime fechaHoraEgreso;

    @Column(name = "observaciones_egreso")
    private String observacionesEgreso;

    // Quién lo registró: se toma del usuario autenticado (ver EgresoService.createEgreso), no del
    // cuerpo de la petición.
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;
}
