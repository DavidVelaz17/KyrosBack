package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.TipoMovimientoEgreso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoMovimientoEgresoConverter implements AttributeConverter<TipoMovimientoEgreso, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoMovimientoEgreso tipoMovimiento) {
        return tipoMovimiento == null ? null : tipoMovimiento.getCodigo();
    }

    @Override
    public TipoMovimientoEgreso convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : TipoMovimientoEgreso.fromCodigo(codigo);
    }
}
