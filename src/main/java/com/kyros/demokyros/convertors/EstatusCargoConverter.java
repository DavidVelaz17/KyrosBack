package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.EstatusCargo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstatusCargoConverter implements AttributeConverter<EstatusCargo, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EstatusCargo estatusCargo) {
        return estatusCargo == null ? null : estatusCargo.getCodigo();
    }

    @Override
    public EstatusCargo convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : EstatusCargo.fromCodigo(codigo);
    }
}
