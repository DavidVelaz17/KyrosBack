package com.kyros.demokyros.dto;

import com.kyros.demokyros.enums.RolUsuario;
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
public class UsuarioDto {

    private Integer idUsuario;
    private String nombreUsuario;
    private String usuario;
    private String direccionUsuario;
    private RolUsuario rol;
}
