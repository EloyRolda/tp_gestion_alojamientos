/// Guarda ESTADOREPORTE en minuscula en la base (nunca @Enumerated). Mismo
/// patron que TipoUsuarioConverter.
package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOREPORTE;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoReporteConverter implements AttributeConverter<ESTADOREPORTE, String> {

    @Override
    public String convertToDatabaseColumn(ESTADOREPORTE attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public ESTADOREPORTE convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ESTADOREPORTE.valueOf(dbData.toUpperCase());
    }
}
