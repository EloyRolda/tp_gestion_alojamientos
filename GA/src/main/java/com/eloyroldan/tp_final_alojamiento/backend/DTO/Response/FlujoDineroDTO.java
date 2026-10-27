package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Panel financiero del admin para un rango de fechas (o todo el historico si
/// desde/hasta vienen null).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlujoDineroDTO {
    private BigDecimal totalFacturado;
    private BigDecimal totalComisionPlataforma;
    private BigDecimal totalComisionMercadopago;
    private BigDecimal totalLiquidadoAnfitriones;
    private BigDecimal totalReembolsadoClientes;
    private Long cantidadPagos;
}
