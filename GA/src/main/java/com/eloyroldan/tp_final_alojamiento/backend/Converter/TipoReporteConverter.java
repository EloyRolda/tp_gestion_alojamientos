/// Guarda TIPOREPORTE en minuscula en la base (nunca @Enumerated). Mismo patron
/// que TipoUsuarioConverter.
package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOREPORTE;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoReporteConverter implements AttributeConverter<TIPOREPORTE, String> {

    @Override
    public String convertToDatabaseColumn(TIPOREPORTE attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public TIPOREPORTE convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TIPOREPORTE.valueOf(dbData.toUpperCase());
    }
}
