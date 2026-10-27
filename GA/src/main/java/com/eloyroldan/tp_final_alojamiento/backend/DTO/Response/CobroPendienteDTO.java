package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/// Reservas ya pagadas por el cliente, que todavia esperan liquidarse
/// (se liquidan recien cuando la reserva pasa a FINALIZADA).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CobroPendienteDTO {
    private Long idReserva;
    private String tituloAlojamiento;
    private BigDecimal montoAcobrar;
    private LocalDate fechaFinEstadia;
}
