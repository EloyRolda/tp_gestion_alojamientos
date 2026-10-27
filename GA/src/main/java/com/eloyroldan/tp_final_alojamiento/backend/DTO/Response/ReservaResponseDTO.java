package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/// No incluye la Review anidada (evita el ciclo Reserva<->Review); si hace falta,
/// se consulta aparte via /Review/mostrar o /Review/alojamiento.
/// "pago" es null si la reserva todavia no llego a CONFIRMADA (no se creo el Pago).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {

    private Long idReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal precioNochePactado;
    private BigDecimal precioTotal;
    private ESTADORESERVA estadoReserva;
    private AlojamientoResumenDTO alojamiento;
    private UsuarioResumenDTO cliente;
    private boolean tieneReview;
    private PagoResponseDTO pago;

    public static ReservaResponseDTO desde(Reserva r, Pago pago) {
        return new ReservaResponseDTO(
                r.getIdReserva(), r.getFechaInicio(), r.getFechaFin(),
                r.getPrecioNochePactado(), r.getPrecioTotal(),
                r.getEstadoReserva(),
                AlojamientoResumenDTO.desde(r.getAlojamiento()),
                UsuarioResumenDTO.desde(r.getCliente()),
                r.getReview() != null,
                pago == null ? null : PagoResponseDTO.desde(pago));
    }
}
