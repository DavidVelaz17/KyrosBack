package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.EstatusEstudiante;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstatusEstudianteConverter implements AttributeConverter<EstatusEstudiante, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EstatusEstudiante estatusEstudiante) {
        return estatusEstudiante == null ? null : estatusEstudiante.getCodigo();
    }

    @Override
    public EstatusEstudiante convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : EstatusEstudiante.fromCodigo(codigo);
    }
}
