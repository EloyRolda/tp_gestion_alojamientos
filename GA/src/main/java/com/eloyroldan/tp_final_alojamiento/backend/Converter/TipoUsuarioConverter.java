package com.eloyroldan.tp_final_alojamiento.backend.Converter;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/// La columna usuario.tipo es un ENUM('admin','cliente','anfitrion') en minuscula,
/// pero el enum de Java (por convencion) esta en mayuscula. @Enumerated(STRING) manda
/// el nombre tal cual ("CLIENTE") y la base lo rechaza si la colacion es case-sensitive
/// ("Data truncated for column 'tipo'"). Este converter traduce en los dos sentidos
/// sin tener que tocar el esquema.
@Converter
public class TipoUsuarioConverter implements AttributeConverter<TIPOUSUARIO, String> {

    @Override
    public String convertToDatabaseColumn(TIPOUSUARIO attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public TIPOUSUARIO convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TIPOUSUARIO.valueOf(dbData.toUpperCase());
    }
}
