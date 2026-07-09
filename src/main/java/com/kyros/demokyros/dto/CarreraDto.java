package com.kyros.demokyros.dto;

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
public class CarreraDto {

    private Integer idCarrera;
    private String nombreCarrera;
    private AreaDto area;
}
