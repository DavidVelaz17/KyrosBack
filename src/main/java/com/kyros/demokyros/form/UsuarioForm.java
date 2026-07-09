package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioForm {

    @NotBlank(message = "El nombre del usuario es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El usuario (login) es obligatorio")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotBlank(message = "La dirección del usuario es obligatoria")
    private String direccionUsuario;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;
}
