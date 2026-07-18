package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.DiaSemana;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DiaSemanaConverter implements AttributeConverter<DiaSemana, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DiaSemana diaSemana) {
        return diaSemana == null ? null : diaSemana.getCodigo();
    }

    @Override
    public DiaSemana convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : DiaSemana.fromCodigo(codigo);
    }
}
