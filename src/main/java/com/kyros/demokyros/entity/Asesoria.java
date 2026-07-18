package com.kyros.demokyros.entity;

import com.kyros.demokyros.convertors.DiaSemanaConverter;
import com.kyros.demokyros.convertors.HoraAsesoriaConverter;
import com.kyros.demokyros.enums.DiaSemana;
import com.kyros.demokyros.enums.HoraAsesoria;
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

@Entity
@Table(name = "asesoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asesoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asesoria", nullable = false, updatable = false)
    private Integer idAsesoria;

    @Convert(converter = DiaSemanaConverter.class)
    @Column(name = "dia_asesoria", nullable = false, columnDefinition = "smallint")
    private DiaSemana diaAsesoria;

    @Convert(converter = HoraAsesoriaConverter.class)
    @Column(name = "hora_asesoria", nullable = false, columnDefinition = "smallint")
    private HoraAsesoria horaAsesoria;
}
