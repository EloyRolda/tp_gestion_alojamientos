/// Evento publicado cuando una reserva se cancela. `idQuienCancelo` es el id del
/// cliente o anfitrion que cancelo, o null si la cancelo el sistema (vencimiento de pago)
/// o un administrador.
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record ReservaCancelada(
        Long idReserva,
        Long idCliente,
        Long idAnfitrion,
        String tituloAlojamiento,
        Long idQuienCancelo
) {}
