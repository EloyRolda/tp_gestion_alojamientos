package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Resultado de fusionar dos entradas de un catalogo (TipoAlojamiento o Servicio):
/// cual se elimino, cual se conservo y cuantos alojamientos pasaron de una a otra.
/// Permite que la pantalla del admin confirme el efecto real, no solo "salio bien".
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoFusionDTO {

    private String eliminado;
    private String conservado;
    private int alojamientosMovidos;
}
