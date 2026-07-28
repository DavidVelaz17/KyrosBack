package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.Horario;
import com.kyros.demokyros.enums.IngresoA;
import jakarta.validation.constraints.NotBlank;
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

    @Positive(message = "La edad debe ser un valor positivo")
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

    private Integer idGrupo;
}
