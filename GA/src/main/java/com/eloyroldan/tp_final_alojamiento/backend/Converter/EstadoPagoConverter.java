package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/// Mismo problema y misma solucion que TipoUsuarioConverter, para
/// reserva.estado_pago (ENUM('pendiente','pagado','reembolsado')).
@Converter
public class EstadoPagoConverter implements AttributeConverter<ESTADOPAGO, String> {

    @Override
    public String convertToDatabaseColumn(ESTADOPAGO attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public ESTADOPAGO convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ESTADOPAGO.valueOf(dbData.toUpperCase());
    }
}
