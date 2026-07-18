package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.Horario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class HorarioConverter implements AttributeConverter<Horario, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Horario horario) {
        return horario == null ? null : horario.getCodigo();
    }

    @Override
    public Horario convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : Horario.fromCodigo(codigo);
    }
}
