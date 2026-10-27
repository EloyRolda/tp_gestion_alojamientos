package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.PreferenciaMercadoPagoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/// Wrapper fino sobre la API REST de MercadoPago (sin el SDK oficial, mismo criterio
/// que EmailService con Brevo: menos dependencias, mas control). Todos los metodos
/// asumen credenciales de PRUEBA (ver application.properties) hasta que conectemos
/// una cuenta real.
///
/// OJO — a revisar cuando conectemos la cuenta real: los endpoints de Payouts
/// tuvieron mas de una version en la documentacion de MercadoPago (v1/payouts vs
/// v1/transaction-intents/process segun la region/version de API). Dejo v1/payouts
/// como mejor apuesta actual, pero hay que confirmarlo contra el panel de developers
/// en el momento de probarlo con credenciales reales.
@Slf4j
@Service
public class MercadoPagoService implements PasarelaDePagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    /// En sandbox, la API de Payouts exige este header para aceptar la operacion
    /// con credenciales de prueba (segun la documentacion de MercadoPago). En
    /// cuentas de produccion no hace falta enviarlo (o directamente se ignora).
    @Value("${mercadopago.modo-prueba:true}")
    private boolean modoPrueba;

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.mercadopago.com")
            .build();

    /// Crea la preferencia de pago (Checkout Pro) para que el cliente pague.
    /// externalReference queda grabado del lado de MercadoPago y es lo que usamos
    /// para reconectar el webhook con nuestro Pago (ver PagoService).
    @Override
    @SuppressWarnings("unchecked")
    public PreferenciaMercadoPagoDTO crearPreferencia(String externalReference, BigDecimal monto,
                                                      String descripcion, String emailComprador) {
        Map<String, Object> body = Map.of(
                "items", List.of(Map.of(
                        "title", descripcion,
                        "quantity", 1,
                        "currency_id", "ARS",
                        "unit_price", monto
                )),
                "payer", Map.of("email", emailComprador),
                "external_reference", externalReference,
                "notification_url", notificationUrl,
                "back_urls", Map.of(
                        "success", frontendUrl + "/pago-resultado?estado=exito",
                        "failure", frontendUrl + "/pago-resultado?estado=error",
                        "pending", frontendUrl + "/pago-resultado?estado=pendiente"
                )
        );

        Map<String, Object> respuesta = restClient.post()
                .uri("/checkout/preferences")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(Map.class);

        if (respuesta == null || respuesta.get("id") == null) {
            throw new ParametroInvalidoException("MercadoPago no devolvio una preferencia valida.");
        }
        return new PreferenciaMercadoPagoDTO((String) respuesta.get("id"), (String) respuesta.get("init_point"));
    }

    /// Consulta el estado real de un pago contra MercadoPago (nunca confiar
    /// ciegamente en lo que manda el webhook: siempre se vuelve a preguntar aca).
    /// Devuelve {status, external_reference, transaction_amount} sin envolver en un
    /// DTO propio para no sumar otra clase solo para esto.
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> consultarPago(String idPagoExterno) {
        Map<String, Object> respuesta = restClient.get()
                .uri("/v1/payments/{id}", idPagoExterno)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);

        if (respuesta == null || respuesta.get("status") == null) {
            throw new ParametroInvalidoException("No se pudo consultar el pago " + idPagoExterno + " en MercadoPago.");
        }
        return respuesta;
    }

    /// Reembolso total (monto null) o parcial (monto informado) sobre un pago ya aprobado.
    @Override
    public void reembolsar(String idPagoExterno, BigDecimal monto) {
        Map<String, Object> body = monto == null ? Map.of() : Map.of("amount", monto);

        restClient.post()
                .uri("/v1/payments/{id}/refunds", idPagoExterno)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                // Determinista (no UUID.randomUUID()): si esta llamada se repite para
                // el mismo pago (reintento de red, o una carrera entre dos requests
                // concurrentes), MercadoPago tiene que poder deduplicarla en vez de
                // ejecutar el reembolso dos veces.
                .header("X-Idempotency-Key", "REEMBOLSO-" + idPagoExterno)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        log.info("Reembolso {} ejecutado para el pago {}", monto == null ? "total" : monto, idPagoExterno);
    }

    /// Transfiere al anfitrion (Payout) usando su alias/CBU/CVU. Devuelve el id
    /// externo de la transferencia para guardarlo en Pago.id_transferencia_externa.
    /// Nota: si esto tambien devuelve "Unauthorized use of live credentials" (como
    /// paso con reembolsar()), es la misma limitacion de sandbox — no un bug de este
    /// codigo. Ver PagoService/discusion sobre limitaciones del entorno de prueba.
    @Override
    @SuppressWarnings("unchecked")
    public String crearPayout(String aliasCobroDestino, BigDecimal monto, String referencia) {
        Map<String, Object> body = Map.of(
                "amount", monto,
                "currency", "ARS",
                "destination", Map.of("account", aliasCobroDestino),
                "external_reference", referencia
        );

        Map<String, Object> respuesta = restClient.post()
                .uri("/v1/payouts")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                // Determinista, derivada de "referencia" (no UUID.randomUUID()): dos
                // llamadas con la misma referencia (reintento, o una carrera entre dos
                // requests concurrentes disparando el mismo payout) tienen que
                // deduplicarse en MercadoPago, no ejecutarse como dos transferencias.
                .header("X-Idempotency-Key", referencia)
                .header("X-test-token", String.valueOf(modoPrueba))
                .body(body)
                .retrieve()
                .body(Map.class);

        if (respuesta == null || respuesta.get("id") == null) {
            throw new ParametroInvalidoException("MercadoPago no devolvio un id de transferencia valido.");
        }
        log.info("Payout creado: {} -> alias/CBU/CVU terminado en ...{}", respuesta.get("id"),
                aliasCobroDestino.length() > 4 ? aliasCobroDestino.substring(aliasCobroDestino.length() - 4) : aliasCobroDestino);
        return String.valueOf(respuesta.get("id"));
    }
}