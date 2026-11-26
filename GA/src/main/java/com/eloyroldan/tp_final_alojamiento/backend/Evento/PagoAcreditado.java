/// Evento publicado cuando MercadoPago confirma el pago de una reserva
/// (PagoService.procesarNotificacionPago, rama approved).
package com.eloyroldan.tp_final_alojamiento.backend.Evento;

public record PagoAcreditado(
        Long idReserva,
        Long idCliente,
        Long idAnfitrion,
        String tituloAlojamiento
) {}
