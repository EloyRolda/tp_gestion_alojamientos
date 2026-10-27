package com.eloyroldan.tp_final_alojamiento.backend.Service.Cancelacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Service.PasarelaDePagoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.MontoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/// El cliente cancela con menos de 48hs de anticipacion al check-in: se le
/// reembolsa el resto, y el porcentaje retenido se transfiere (Payout) al
/// anfitrion de inmediato — aunque la estadia nunca haya ocurrido, ya perdio
/// esa disponibilidad por una cancelacion tardia.
@Component
@RequiredArgsConstructor
public class RetencionTardiaPolicy implements PoliticaCancelacion {

    private final PasarelaDePagoService pasarelaDePagoService;

    /// Porcentaje del monto base que se retiene para el anfitrion. Config, no
    /// secreto: igual se deja overrideable por entorno, mismo criterio que las
    /// comisiones en PagoService.
    @Value("${pago.porcentaje-retencion-tardia:0.35}")
    private BigDecimal porcentajeRetencion;

    @Override
    public Set<MOTIVOCANCELACION> motivosQueAplica() {
        return Set.of(MOTIVOCANCELACION.CLIENTE_TARDIA);
    }

    @Override
    public void aplicar(Pago pago, Reserva reserva, BigDecimal montoReembolsoManual) {
        BigDecimal montoBase = pago.getMontoBase();
        BigDecimal retenido = MontoUtil.redondear(montoBase.multiply(porcentajeRetencion));
        BigDecimal reembolsado = montoBase.subtract(retenido);

        pasarelaDePagoService.reembolsar(pago.getIdPagoExterno(), reembolsado);
        String idTransferencia = pasarelaDePagoService.crearPayout(
                reserva.getAlojamiento().getAnfitrion().getAliasCobro(), retenido,
                "CANCELACION-TARDIA-RESERVA-" + reserva.getIdReserva());

        pago.setMontoReembolsadoCliente(reembolsado);
        pago.setMontoRetenidoAnfitrion(retenido);
        pago.setIdTransferenciaExterna(idTransferencia);
        pago.setFechaTransferencia(LocalDateTime.now());
        pago.setEstado(ESTADOPAGO.REEMBOLSADO_PARCIAL);
    }
}
