package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// DTO generico para que el admin fusione dos entradas de un catalogo
/// (Servicio o TipoAlojamiento): todo lo que referenciaba a idOrigen pasa
/// a referenciar idDestino, y idOrigen se elimina.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReemplazoCatalogoDTO {

    @NotNull(message = "Campo obligatorio")
    private Long idOrigen;

    @NotNull(message = "Campo obligatorio")
    private Long idDestino;
}
