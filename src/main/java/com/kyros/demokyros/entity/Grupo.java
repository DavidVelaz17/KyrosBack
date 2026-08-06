package com.kyros.demokyros.entity;

import com.kyros.demokyros.convertors.HorarioConverter;
import com.kyros.demokyros.enums.Horario;
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
@Table(name = "grupo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo", nullable = false, updatable = false)
    private Integer idGrupo;

    @Column(name = "nombre_grupo", nullable = false)
    private String nombreGrupo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "nombre_plantel", nullable = false)
    private String nombrePlantel;

    @Convert(converter = HorarioConverter.class)
    @Column(name = "horario", nullable = false, columnDefinition = "smallint")
    private Horario horario;
}
