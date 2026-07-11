package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.Horario;
import com.kyros.demokyros.enums.IngresoA;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class EstudianteForm {

    private String matricula;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    private String apellidoMaterno;

    @NotNull(message = "La edad es obligatoria")
    @Positive(message = "La edad debe ser un valor positivo")
    private Short edad;

    @NotNull(message = "El número telefónico es obligatorio")
    private Long numeroTelefonico;

    @NotBlank(message = "La escuela de procedencia es obligatoria")
    private String escuelaProcedencia;

    @NotBlank(message = "El grado escolar es obligatorio")
    private String gradoEscolar;

    private String nombreTutor;

    private String telefonoTutor;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String foto;

    private String notas;

    @NotNull(message = "La fecha de inscripción es obligatoria")
    private LocalDate fechaInscripcion;

    @NotNull(message = "El horario es obligatorio")
    private Horario horario;

    @NotNull(message = "El campo ingresoA es obligatorio")
    private IngresoA ingresoA;

    private Integer idGrupo;
}
