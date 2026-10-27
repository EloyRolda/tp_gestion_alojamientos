package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Un punto de la serie mensual de facturacion (ver PagoService.ingresosMensuales).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngresoMensualDTO {
    private Integer anio;
    private Integer mes;
    private BigDecimal totalFacturado;
}
