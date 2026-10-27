package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/// Mismo problema y misma solucion que TipoUsuarioConverter, para
/// reserva.estado_reserva (ENUM('solicitada','confirmada','cancelada','finalizada')).
@Converter
public class EstadoReservaConverter implements AttributeConverter<ESTADORESERVA, String> {

    @Override
    public String convertToDatabaseColumn(ESTADORESERVA attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public ESTADORESERVA convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ESTADORESERVA.valueOf(dbData.toUpperCase());
    }
}
