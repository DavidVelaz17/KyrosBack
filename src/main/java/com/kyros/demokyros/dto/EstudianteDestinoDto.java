package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.DiaSemana;
import com.kyros.demokyros.enums.HoraAsesoria;
import com.kyros.demokyros.enums.IngresoA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDestinoDto {

    private Integer idRelacion;
    private Integer idDestino;
    private String nombreDestino;
    private IngresoA tipo;
    /** Sólo aplica para tipo UNIVERSIDAD: carreras que ofrece esa universidad (vía carrera_universidad), con su área. */
    private List<CarreraDto> carreras;
    /** Sólo aplica para tipo ASESORIAS: materias vinculadas a esa asesoría (vía asesoria_materia). */
    private List<MateriaDto> materias;
    /** Sólo aplica para tipo ASESORIAS: día y hora de la asesoría, para poder reconstruirla al editar. */
    private DiaSemana dia;
    private HoraAsesoria hora;
}
