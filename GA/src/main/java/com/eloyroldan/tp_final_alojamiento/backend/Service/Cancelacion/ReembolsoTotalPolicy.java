package com.eloyroldan.tp_final_alojamiento.backend.Service.Cancelacion;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Service.PasarelaDePagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

/// Reembolso 100% al cliente, sin retencion para el anfitrion. Aplica cuando
/// cancela el anfitrion (siempre sin penalidad para el cliente) o cuando
/// cancela el cliente con suficiente anticipacion al check-in.
@Component
@RequiredArgsConstructor
public class ReembolsoTotalPolicy implements PoliticaCancelacion {

    private final PasarelaDePagoService pasarelaDePagoService;

    @Override
    public Set<MOTIVOCANCELACION> motivosQueAplica() {
        return Set.of(MOTIVOCANCELACION.ANFITRION, MOTIVOCANCELACION.CLIENTE_ANTICIPADA);
    }

    @Override
    public void aplicar(Pago pago, Reserva reserva, BigDecimal montoReembolsoManual) {
        BigDecimal montoBase = pago.getMontoBase();
        pasarelaDePagoService.reembolsar(pago.getIdPagoExterno(), montoBase);
        pago.setMontoReembolsadoCliente(montoBase);
        pago.setMontoRetenidoAnfitrion(BigDecimal.ZERO);
        pago.setEstado(ESTADOPAGO.REEMBOLSADO_TOTAL);
    }
}
