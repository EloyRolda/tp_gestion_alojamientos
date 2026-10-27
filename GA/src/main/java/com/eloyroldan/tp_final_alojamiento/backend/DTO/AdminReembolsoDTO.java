package com.eloyroldan.tp_final_alojamiento.backend.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// Cancelacion con reembolso a medida, para casos que no entran en las 3 reglas
/// automaticas (cliente anticipado/tardio, anfitrion). Solo ADMIN.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminReembolsoDTO {

    @NotNull(message = "Campo obligatorio")
    private Long idReserva;

    /// Cuanto del monto base se le devuelve al cliente (el resto queda para el
    /// anfitrion). 0 = nada para el cliente, todo el monto base para el anfitrion.
    @NotNull(message = "Campo obligatorio")
    @DecimalMin(value = "0.0", message = "No puede ser negativo")
    private BigDecimal montoReembolsoCliente;
}
