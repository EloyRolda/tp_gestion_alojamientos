/// Guarda MOTIVOREPORTE en minuscula en la base (nunca @Enumerated). Mismo
/// patron que TipoUsuarioConverter.
package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MotivoReporteConverter implements AttributeConverter<MOTIVOREPORTE, String> {

    @Override
    public String convertToDatabaseColumn(MOTIVOREPORTE attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public MOTIVOREPORTE convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MOTIVOREPORTE.valueOf(dbData.toUpperCase());
    }
}
