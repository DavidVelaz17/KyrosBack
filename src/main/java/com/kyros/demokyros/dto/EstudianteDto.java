package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.Horario;
import com.kyros.demokyros.enums.IngresoA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDto {

    private Integer idEstudiante;
    private String matricula;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Short edad;
    private Long numeroTelefonico;
    private String escuelaProcedencia;
    private String gradoEscolar;
    private String nombreTutor;
    private String telefonoTutor;
    private String direccion;
    private String foto;
    private String notas;
    private LocalDate fechaInscripcion;
    private Horario horario;
    private IngresoA ingresoA;
    private GrupoDto grupo;
}
