package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.PreferenciaMercadoPagoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/// Pasarela para demostraciones con una cuenta de MercadoPago de DESARROLLO.
///
/// El sandbox de MercadoPago cobra bien (checkout y consulta de pagos), pero
/// rechaza los reembolsos y las transferencias al anfitrion (Payouts) si la
/// cuenta no esta habilitada como marketplace. Como esas llamadas van dentro de
/// la misma transaccion que el cambio de estado de la reserva, el rechazo
/// deshacia todo: no se podia mostrar una cancelacion con reembolso ni una
/// finalizacion.
///
/// Esta implementacion solo existe si pagos.simulados=true (PAGOS_SIMULADOS en
/// el entorno). Cobrar sigue yendo a MercadoPago tal cual; reembolsar y
/// transferir NO llaman a MercadoPago: se registran en el log y devuelven un
/// comprobante "SIMULADO-...". Las reglas de negocio (porcentajes, estados,
/// montos guardados en el Pago) se ejecutan completas, igual que en produccion.
///
/// Es @Primary: con la propiedad activa, todo lo que inyecta
/// PasarelaDePagoService recibe esta clase; sin la propiedad, esta clase no se
/// crea y se usa MercadoPagoService directo. Nunca activarla con credenciales
/// reales de produccion.
@Slf4j
@Primary
@Service
@ConditionalOnProperty(name = "pagos.simulados", havingValue = "true")
@RequiredArgsConstructor
public class PasarelaSimuladaService implements PasarelaDePagoService {

    private final MercadoPagoService mercadoPago;

    @Override
    public PreferenciaMercadoPagoDTO crearPreferencia(String externalReference, BigDecimal monto,
                                                       String descripcion, String emailComprador) {
        return mercadoPago.crearPreferencia(externalReference, monto, descripcion, emailComprador);
    }

    @Override
    public Map<String, Object> consultarPago(String idPagoExterno) {
        return mercadoPago.consultarPago(idPagoExterno);
    }

    @Override
    public void reembolsar(String idPagoExterno, BigDecimal monto) {
        log.warn("[PAGOS SIMULADOS] Reembolso {} del pago {} NO enviado a MercadoPago (cuenta de desarrollo).",
                monto == null ? "total" : monto, idPagoExterno);
    }

    @Override
    public String crearPayout(String aliasCobroDestino, BigDecimal monto, String referencia) {
        log.warn("[PAGOS SIMULADOS] Transferencia de {} ({}) NO enviada a MercadoPago (cuenta de desarrollo).",
                monto, referencia);
        return "SIMULADO-" + referencia;
    }
}
