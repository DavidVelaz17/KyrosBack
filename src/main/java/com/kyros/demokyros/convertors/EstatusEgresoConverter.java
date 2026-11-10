package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.EstatusEgreso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstatusEgresoConverter implements AttributeConverter<EstatusEgreso, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EstatusEgreso estatusEgreso) {
        return estatusEgreso == null ? null : estatusEgreso.getCodigo();
    }

    @Override
    public EstatusEgreso convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : EstatusEgreso.fromCodigo(codigo);
    }
}
