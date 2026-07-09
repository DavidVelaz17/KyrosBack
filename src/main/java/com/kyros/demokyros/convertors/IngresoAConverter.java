package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.IngresoA;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IngresoAConverter implements AttributeConverter<IngresoA, Integer> {

    @Override
    public Integer convertToDatabaseColumn(IngresoA ingresoA) {
        return ingresoA == null ? null : ingresoA.getCodigo();
    }

    @Override
    public IngresoA convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : IngresoA.fromCodigo(codigo);
    }
}
