package com.eloyroldan.tp_final_alojamiento.backend.Service.Cancelacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;

import java.math.BigDecimal;
import java.util.Set;

/// Strategy: cada MOTIVOCANCELACION (salvo VENCIMIENTO_PAGO, que ni llega a este
/// punto — se resuelve antes, en PagoService.ejecutarCancelacion, porque un pago
/// nunca pagado no tiene nada para reembolsar/retener) tiene su propia regla de
/// cuanto se reembolsa al cliente y cuanto retiene/recibe el anfitrion. Antes
/// esto era un solo switch gigante en PagoService; separarlo en una politica por
/// motivo deja that PagoService solo orquesta (buscar el Pago, decidir cuando
/// aplica, guardar) sin conocer la matematica de cada caso, y agregar un motivo
/// nuevo es agregar una clase, no editar un switch ya existente (OCP).
public interface PoliticaCancelacion {

    /// Motivos de cancelacion a los que aplica esta politica (ej: ANFITRION y
    /// CLIENTE_ANTICIPADA comparten la misma regla: reembolso 100%).
    Set<MOTIVOCANCELACION> motivosQueAplica();

    /// Aplica la regla sobre un Pago ya PAGADO: deja seteados en el propio Pago
    /// el estado final, lo reembolsado/retenido y, si corresponde, los datos del
    /// Payout — sin persistir (PagoService.ejecutarCancelacion hace el save).
    /// montoReembolsoManual solo lo usa la politica de ADMIN; las demas lo ignoran.
    void aplicar(Pago pago, Reserva reserva, BigDecimal montoReembolsoManual);
}
