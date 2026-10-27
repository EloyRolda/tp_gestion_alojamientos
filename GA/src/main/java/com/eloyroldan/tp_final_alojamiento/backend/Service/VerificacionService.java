package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.TokenVerificacion;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.TokenVerificacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/// Orquesta el alta/consumo de tokens de verificacion de correo. El token dura
/// 24 horas y es de un solo uso.
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificacionService {

    private static final long HORAS_EXPIRACION_TOKEN = 24;

    private final TokenVerificacionRepository tokenVerificacionRepository;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    /// Genera un token nuevo y dispara el mail. Si el envio de correo falla (ej:
    /// Brevo caido, api-key mal configurada), no se aborta el registro: el token
    /// ya quedo guardado, y el usuario puede pedir un reenvio desde el front.
    @Transactional
    public void generarYEnviar(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        TokenVerificacion tokenVerificacion = TokenVerificacion.builder()
                .token(token)
                .fechaExpiracion(LocalDateTime.now().plusHours(HORAS_EXPIRACION_TOKEN))
                .usado(false)
                .usuario(usuario)
                .build();
        tokenVerificacionRepository.save(tokenVerificacion);

        try {
            String link = frontendUrl + "/verificar-cuenta?token=" + token;
            emailService.enviar(
                    usuario.getEmail(),
                    usuario.getNombre(),
                    "Confirma tu cuenta en AlojaME",
                    construirHtml(usuario.getNombre(), link)
            );
        } catch (Exception e) {
            log.warn("No se pudo enviar el correo de verificacion a {}: {}", usuario.getEmail(), e.getMessage());
        }
    }

    /// Valida el token, marca el usuario como verificado. Falla si el token no
    /// existe, ya fue usado, o vencio (24hs).
    @Transactional
    public void verificarToken(String token) {
        TokenVerificacion tokenVerificacion = tokenVerificacionRepository.findByToken(token)
                .orElseThrow(() -> new ParametroInvalidoException("El enlace de verificacion no es valido."));

        if (tokenVerificacion.getUsado()) {
            throw new ParametroInvalidoException("Este enlace de verificacion ya fue utilizado.");
        }
        if (tokenVerificacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ParametroInvalidoException("El enlace de verificacion vencio. Solicite uno nuevo.");
        }

        tokenVerificacion.setUsado(true);
        tokenVerificacionRepository.save(tokenVerificacion);

        usuarioService.marcarEmailVerificado(tokenVerificacion.getUsuario().getId());
    }

    /// Genera y envia un nuevo token, por si el original vencio o no llego.
    @Transactional
    public void reenviarVerificacion(String email) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(email);
        if (usuario.getEmailVerificado()) {
            throw new ParametroInvalidoException("Esta cuenta ya fue verificada.");
        }
        generarYEnviar(usuario);
    }

    private String construirHtml(String nombre, String link) {
        return """
                <div style="font-family: Arial, sans-serif; line-height: 1.5;">
                    <h2>Hola %s!</h2>
                    <p>Gracias por registrarte en AlojaME. Para activar tu cuenta, haz click en el siguiente boton:</p>
                    <p>
                        <a href="%s" style="background:#2563eb;color:#ffffff;padding:10px 20px;
                        text-decoration:none;border-radius:6px;display:inline-block;">
                            Verificar mi cuenta
                        </a>
                    </p>
                    <p>Este enlace vence en 24 horas.</p>
                    <p style="color:#666;font-size:12px;">Si no te registraste en AlojaME, podes ignorar este correo.</p>
                </div>
                """.formatted(nombre, link);
    }
}
