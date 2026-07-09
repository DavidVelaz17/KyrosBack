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
@Table(name = "carrera_universidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarreraUniversidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrera_universidad", nullable = false, updatable = false)
    private Integer idCarreraUniversidad;

    @Column(name = "id_carrera", nullable = false)
    private Integer idCarrera;

    @Column(name = "id_universidad", nullable = false)
    private Integer idUniversidad;
}
