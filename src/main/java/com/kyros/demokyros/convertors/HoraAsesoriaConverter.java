package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.HoraAsesoria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class HoraAsesoriaConverter implements AttributeConverter<HoraAsesoria, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HoraAsesoria horaAsesoria) {
        return horaAsesoria == null ? null : horaAsesoria.getCodigo();
    }

    @Override
    public HoraAsesoria convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : HoraAsesoria.fromCodigo(codigo);
    }
}
