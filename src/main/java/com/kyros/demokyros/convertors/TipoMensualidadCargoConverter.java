package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.TipoMensualidadCargo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoMensualidadCargoConverter implements AttributeConverter<TipoMensualidadCargo, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoMensualidadCargo tipoMensualidadCargo) {
        return tipoMensualidadCargo == null ? null : tipoMensualidadCargo.getCodigo();
    }

    @Override
    public TipoMensualidadCargo convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : TipoMensualidadCargo.fromCodigo(codigo);
    }
}
