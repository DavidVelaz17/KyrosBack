package com.kyros.demokyros.responses;

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
public class JwtResponse {

    private String token;
    private Integer idUsuario;
    private String nombreUsuario;
    private String usuario;
    private RolUsuario rol;
}
