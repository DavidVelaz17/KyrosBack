package com.kyros.demokyros.form;

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
public class AsesoriaMateriaForm {

    @NotNull(message = "La asesoría es obligatoria")
    private Integer idAsesoria;

    @NotNull(message = "La materia es obligatoria")
    private Integer idMateria;
}
