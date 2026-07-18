package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.EstatusCargo;
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
public class CargoEstatusForm {

    @NotNull(message = "El estatus del cargo es obligatorio")
    private EstatusCargo estatusCargo;
}
