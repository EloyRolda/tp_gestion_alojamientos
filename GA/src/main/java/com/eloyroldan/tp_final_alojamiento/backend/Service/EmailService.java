package com.eloyroldan.tp_final_alojamiento.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/// Envia correos transaccionales usando la API HTTP de Brevo (antes Sendinblue).
/// No usa SMTP: pega directo a https://api.brevo.com/v3/smtp/email con la api-key
/// como header, que es lo que corresponde al tipo de credencial que se nos dio.
@Slf4j
@Service
public class EmailService {

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.remitente.email}")
    private String remitenteEmail;

    @Value("${brevo.remitente.nombre}")
    private String remitenteNombre;

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.brevo.com/v3")
            .build();

    /// Lanza una excepcion sin control (RuntimeException) si Brevo rechaza el envio;
    /// quien lo llama decide si eso debe frenar el flujo o solo quedar logueado
    /// (ver VerificacionService, que no bloquea el registro por esto).
    public void enviar(String destinatarioEmail, String destinatarioNombre, String asunto, String htmlContent) {
        Map<String, Object> body = Map.of(
                "sender", Map.of("name", remitenteNombre, "email", remitenteEmail),
                "to", List.of(Map.of("email", destinatarioEmail, "name", destinatarioNombre)),
                "subject", asunto,
                "htmlContent", htmlContent
        );

        restClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .retrieve()
                .toBodilessEntity();

        log.info("Correo enviado a {} (asunto: {})", destinatarioEmail, asunto);
    }
}
