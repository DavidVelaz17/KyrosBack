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
@Table(name = "curso_verano")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoVerano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso_verano", nullable = false, updatable = false)
    private Integer idCursoVerano;

    @Column(name = "nombre_curso_verano", nullable = false)
    private String nombreCursoVerano;
}
