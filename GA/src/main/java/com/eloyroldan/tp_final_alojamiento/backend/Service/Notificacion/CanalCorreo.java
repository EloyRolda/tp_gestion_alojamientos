package com.eloyroldan.tp_final_alojamiento.backend.Service.Notificacion;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.EmailService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/// Canal de correo del Strategy de notificaciones.
/// Agregar un canal nuevo es agregar una clase que implemente CanalNotificacion.
@Component
@RequiredArgsConstructor
@Slf4j
public class CanalCorreo implements CanalNotificacion {

    private final UsuarioService usuarioService;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void enviar(Aviso aviso) {
        if (!aviso.porCorreo()) {
            return;
        }

        Usuario usuario = usuarioService.obtenerPorId(aviso.idDestinatario());
        String nombre = usuario.getNombre();
        String texto = aviso.texto();
        String enlace = aviso.enlace();

        String nombreEscapado = HtmlUtils.htmlEscape(nombre);
        String textoEscapado = HtmlUtils.htmlEscape(texto);

        String asunto = asuntoPara(aviso.tipo().name());
        String html = construirHtml(
                nombreEscapado,
                textoEscapado,
                enlace,
                frontendUrl
        );

        // Si Brevo falla, el aviso interno ya quedo guardado por CanalInterno:
        // solo se deja constancia en el log, sin datos del destinatario.
        try {
            emailService.enviar(usuario.getEmail(), nombre, asunto, html);
        } catch (RuntimeException e) {
            log.warn("No se pudo enviar el correo del aviso {} al usuario {}: {}", aviso.tipo(), aviso.idDestinatario(), e.getMessage());
        }
    }

    private String asuntoPara(String tipo) {
        return switch (tipo) {
            case "RESERVA_CONFIRMADA" -> "Tu reserva fue confirmada";
            case "RESERVA_CANCELADA" -> "Se canceló una reserva";
            case "PAGO_ACREDITADO" -> "Pago acreditado";
            case "RESERVA_FINALIZADA" -> "Tu estadía finalizó";
            default -> "Novedades de tu cuenta en AlojaME";
        };
    }

    private String construirHtml(String nombre, String texto, String enlace, String frontendUrl) {
        String urlCompleta = frontendUrl + enlace;
        return """
            <div style="font-family: Arial, sans-serif; line-height: 1.5;">
                <h2>¡Hola %s!</h2>
                <p>%s</p>
                <p>
                    <a href="%s" style="background:#2563eb;color:#ffffff;padding:10px 20px;
                    text-decoration:none;border-radius:6px;display:inline-block;">
                        Ver en AlojaME
                    </a>
                </p>
                <p style="color:#666;font-size:12px;">Recibís este correo porque tenés una cuenta en AlojaME.</p>
            </div>
            """.formatted(nombre, texto, urlCompleta);
    }
}
