package com.kyros.demokyros.form;

import jakarta.validation.constraints.NotBlank;
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
public class SecundariaForm {

    @NotBlank(message = "El nombre de la secundaria es obligatorio")
    private String nombreSecundaria;
}
