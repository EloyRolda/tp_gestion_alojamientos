package com.eloyroldan.tp_final_alojamiento.backend.Enum;

/// Por que se cancelo una reserva ya confirmada/pagada. Determina la regla de
/// reembolso/retencion que se aplica (ver PagoService).
public enum MOTIVOCANCELACION {
    /// Cliente cancela con 48hs o mas de anticipacion al check-in: reembolso 100%.
    CLIENTE_ANTICIPADA,
    /// Cliente cancela con menos de 48hs de anticipacion: retencion del 35% para el anfitrion.
    CLIENTE_TARDIA,
    /// Anfitrion cancela una reserva ya paga: reembolso 100% al cliente, sin penalidad.
    ANFITRION,
    /// Se vencieron las 48hs de plazo de pago sin que el cliente pagara. No hay plata
    /// movida, no aplica reembolso (la reserva nunca llego a pagarse).
    VENCIMIENTO_PAGO,
    /// Cancelacion resuelta manualmente por un admin (reembolso a medida).
    ADMIN
}
