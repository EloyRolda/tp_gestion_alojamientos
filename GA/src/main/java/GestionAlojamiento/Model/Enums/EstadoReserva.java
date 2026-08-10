package GestionAlojamiento.Model.Enums;

/// Maquina de estados de una reserva.
/// SOLICITADA      -> el cliente pidio la reserva, esperando respuesta del anfitrion.
/// RECHAZADA       -> el anfitrion no acepto la solicitud (estado final).
/// ACEPTADA        -> el anfitrion acepto, el cliente tiene 48hs para pagar (fechaLimitePago).
/// VENCIDA         -> se cumplieron las 48hs de la aceptacion sin pago (estado final).
/// CANCELADA       -> el cliente cancelo la solicitud o la reserva aceptada antes de pagar (estado final).
/// PAGADA          -> el cliente pago mediante Mercado Pago, la estadia queda confirmada.
/// FINALIZADA      -> la estadia ya se llevo a cabo y fue cerrada por el anfitrion o el sistema (estado final).
public enum EstadoReserva {
    SOLICITADA,
    RECHAZADA,
    ACEPTADA,
    VENCIDA,
    CANCELADA,
    PAGADA,
    FINALIZADA
}
