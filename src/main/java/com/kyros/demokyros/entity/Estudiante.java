package com.kyros.demokyros.entity;

import com.kyros.demokyros.convertors.EstatusEstudianteConverter;
import com.kyros.demokyros.convertors.HorarioConverter;
import com.kyros.demokyros.convertors.IngresoAConverter;
import com.kyros.demokyros.enums.EstatusEstudiante;
import com.kyros.demokyros.enums.Horario;
import com.kyros.demokyros.enums.IngresoA;
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
@Table(name = "estudiante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante", nullable = false, updatable = false)
    private Integer idEstudiante;

    @Column(name = "matricula", unique = true, nullable = false)
    private String matricula;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @Column(name = "edad")
    private Short edad;

    @Column(name = "numero_telefonico")
    private Long numeroTelefonico;

    @Column(name = "escuela_procedencia")
    private String escuelaProcedencia;

    @Column(name = "grado_escolar", nullable = false)
    private String gradoEscolar;

    @Column(name = "nombre_tutor")
    private String nombreTutor;

    @Column(name = "telefono_tutor")
    private String telefonoTutor;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "foto")
    private String foto;

    @Column(name = "notas")
    private String notas;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @Convert(converter = HorarioConverter.class)
    @Column(name = "horario", nullable = false, columnDefinition = "smallint")
    private Horario horario;

    @Convert(converter = IngresoAConverter.class)
    @Column(name = "ingreso_a", nullable = false, columnDefinition = "smallint")
    private IngresoA ingresoA;

    @Column(name = "id_grupo")
    private Integer idGrupo;

    @Convert(converter = EstatusEstudianteConverter.class)
    @Column(name = "estatus", nullable = false, columnDefinition = "smallint")
    private EstatusEstudiante estatus;
}
