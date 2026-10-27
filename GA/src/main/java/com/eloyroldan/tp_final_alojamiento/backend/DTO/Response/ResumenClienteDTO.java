package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Historial de gasto de un cliente (ver PagoRepository.resumenGastoCliente).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenClienteDTO {
    private Long cantidadReservas;
    private BigDecimal montoTotalGastado;
    private Double montoPromedioPorEstadia;
}
