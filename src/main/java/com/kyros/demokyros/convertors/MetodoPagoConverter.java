package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.MetodoPago;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MetodoPagoConverter implements AttributeConverter<MetodoPago, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MetodoPago metodoPago) {
        return metodoPago == null ? null : metodoPago.getCodigo();
    }

    @Override
    public MetodoPago convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : MetodoPago.fromCodigo(codigo);
    }
}
