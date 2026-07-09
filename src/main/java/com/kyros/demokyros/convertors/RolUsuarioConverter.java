package com.kyros.demokyros.convertors;

import com.kyros.demokyros.enums.RolUsuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolUsuarioConverter implements AttributeConverter<RolUsuario, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RolUsuario rolUsuario) {
        return rolUsuario == null ? null : rolUsuario.getCodigo();
    }

    @Override
    public RolUsuario convertToEntityAttribute(Integer codigo) {
        return codigo == null ? null : RolUsuario.fromCodigo(codigo);
    }
}
