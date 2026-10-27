package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Fila del resumen que ve un anfitrion de UNO de sus alojamientos (ver
/// EstadisticasController.resumenAnfitrion).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenAlojamientoAnfitrionDTO {
    private Long idAlojamiento;
    private String titulo;
    private BigDecimal ingresosTotales;
    private Long cantidadReservas;
    private Double ratingPromedio;
}
