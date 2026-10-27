package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/// Mismo problema y misma solucion que TipoUsuarioConverter, para
/// pago.motivo_cancelacion (ENUM en minuscula en la BD).
@Converter
public class MotivoCancelacionConverter implements AttributeConverter<MOTIVOCANCELACION, String> {

    @Override
    public String convertToDatabaseColumn(MOTIVOCANCELACION attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public MOTIVOCANCELACION convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MOTIVOCANCELACION.valueOf(dbData.toUpperCase());
    }
}
