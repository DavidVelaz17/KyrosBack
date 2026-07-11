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

@Entity
@Table(name = "estudiante_universidad_carrera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteUniversidadCarrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante_universidad_carrera", nullable = false, updatable = false)
    private Integer idEstudianteUniversidadCarrera;

    @Column(name = "id_estudiante_universidad", nullable = false)
    private Integer idEstudianteUniversidad;

    @Column(name = "id_carrera", nullable = false)
    private Integer idCarrera;
}
