package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPONOTIFICACION;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/// Convierte TIPONOTIFICACION a minúsculas en la base y viceversa.
@Converter
public class TipoNotificacionConverter implements AttributeConverter<TIPONOTIFICACION, String> {

    @Override
    public String convertToDatabaseColumn(TIPONOTIFICACION attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public TIPONOTIFICACION convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TIPONOTIFICACION.valueOf(dbData.toUpperCase());
    }
}
