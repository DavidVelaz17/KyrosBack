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
@Table(name = "asesoria_materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsesoriaMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asesoria_materia", nullable = false, updatable = false)
    private Integer idAsesoriaMateria;

    @Column(name = "id_asesoria", nullable = false)
    private Integer idAsesoria;

    @Column(name = "id_materia", nullable = false)
    private Integer idMateria;
}
