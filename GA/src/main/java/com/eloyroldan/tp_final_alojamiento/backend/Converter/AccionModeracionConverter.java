/// Guarda ACCIONMODERACION en minuscula en la base (nunca @Enumerated). Mismo
/// patron que TipoUsuarioConverter.
package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ACCIONMODERACION;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AccionModeracionConverter implements AttributeConverter<ACCIONMODERACION, String> {

    @Override
    public String convertToDatabaseColumn(ACCIONMODERACION attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public ACCIONMODERACION convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ACCIONMODERACION.valueOf(dbData.toUpperCase());
    }
}
