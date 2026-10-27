package com.eloyroldan.tp_final_alojamiento.backend.Service.Cancelacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Service.PasarelaDePagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

/// El admin resuelve una cancelacion a medida (casos que no entran en ninguna
/// regla automatica): indica a mano cuanto se le reembolsa al cliente, y el
/// resto queda retenido para el anfitrion (sin Payout automatico — esa plata
/// retenida se liquida igual que cualquier otra en el cierre de cuentas, no
/// hace falta transferirla aparte en este mismo paso).
@Component
@RequiredArgsConstructor
public class ReembolsoManualAdminPolicy implements PoliticaCancelacion {

    private final PasarelaDePagoService pasarelaDePagoService;

    @Override
    public Set<MOTIVOCANCELACION> motivosQueAplica() {
        return Set.of(MOTIVOCANCELACION.ADMIN);
    }

    @Override
    public void aplicar(Pago pago, Reserva reserva, BigDecimal montoReembolsoManual) {
        BigDecimal montoBase = pago.getMontoBase();

        if (montoReembolsoManual == null) {
            throw new ParametroInvalidoException("El monto a reembolsar es obligatorio para una cancelacion de tipo ADMIN.");
        }
        if (montoReembolsoManual.compareTo(montoBase) > 0) {
            throw new ParametroInvalidoException(
                    "El monto a reembolsar (" + montoReembolsoManual + ") no puede superar el monto base de la reserva (" + montoBase + ").");
        }

        if (montoReembolsoManual.compareTo(BigDecimal.ZERO) > 0) {
            pasarelaDePagoService.reembolsar(pago.getIdPagoExterno(), montoReembolsoManual);
        }

        pago.setMontoReembolsadoCliente(montoReembolsoManual);
        pago.setMontoRetenidoAnfitrion(montoBase.subtract(montoReembolsoManual));
        pago.setEstado(montoReembolsoManual.compareTo(montoBase) >= 0
                ? ESTADOPAGO.REEMBOLSADO_TOTAL : ESTADOPAGO.REEMBOLSADO_PARCIAL);
    }
}
