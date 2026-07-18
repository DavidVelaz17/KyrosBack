package com.kyros.demokyros.form;

import com.kyros.demokyros.enums.DiaSemana;
import com.kyros.demokyros.enums.HoraAsesoria;
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
public class AsesoriaForm {

    @NotNull(message = "El día de la asesoría es obligatorio")
    private DiaSemana diaAsesoria;

    @NotNull(message = "La hora de la asesoría es obligatoria")
    private HoraAsesoria horaAsesoria;
}
