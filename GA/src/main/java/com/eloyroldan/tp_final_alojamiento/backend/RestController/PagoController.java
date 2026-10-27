package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.AdminReembolsoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.PagoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReservaResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Reserva;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.PagoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReservaService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/Pago")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    /// El cliente dueño, el anfitrion dueño o un admin pueden ver el detalle del
    /// pago (incluida la urlCheckout para pagar). Reusa el chequeo de acceso que
    /// ya tiene ReservaService para no duplicar la logica de permisos.
    @GetMapping("/reserva/{idReserva}")
    public PagoResponseDTO obtenerPorReserva(@PathVariable Long idReserva, Authentication auth) {
        ReservaResponseDTO reserva = reservaService.obtenerDTOVerificandoAcceso(idReserva, auth.getName());
        if (reserva.getPago() == null) {
            throw new ParametroInvalidoException("Esta reserva todavia no tiene un pago asociado (falta confirmarla).");
        }
        return reserva.getPago();
    }

    /// Endpoint publico: MercadoPago pega aca cuando un pago cambia de estado.
    /// Nunca confia en el body/query de la notificacion: PagoService vuelve a
    /// preguntarle a MercadoPago el estado real antes de tocar nada.
    /// Acepta tanto el formato nuevo (type=payment&data.id=X) como el viejo
    /// (topic=payment&id=X), que es como MercadoPago manda estas notificaciones.
    /// Al ser publico y sin autenticacion, cualquiera puede pegarle con basura
    /// (adrede o por error) — por eso todo el procesamiento queda envuelto en un
    /// try/catch amplio: pase lo que pase, este endpoint responde 200 y nunca
    /// se cae con un 500. Los problemas reales quedan en el log, no en la respuesta.
    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestParam Map<String, String> parametros) {
        log.info("Webhook de MercadoPago recibido: {}", parametros);
        try {
            String idPago = parametros.getOrDefault("data.id", parametros.get("id"));
            String tipo = parametros.getOrDefault("type", parametros.get("topic"));

            if (!"payment".equals(tipo) || idPago == null || idPago.isBlank()) {
                // Puede ser otro tipo de notificacion (merchant_order, etc), o
                // simplemente un llamado invalido: se ignora sin error.
                return ResponseEntity.ok("ignorado");
            }

            pagoService.procesarNotificacionPago(idPago);
            return ResponseEntity.ok("procesado");
        } catch (Exception e) {
            log.error("Error procesando webhook de MercadoPago (parametros: {}): {}", parametros, e.getMessage(), e);
            // 200 igual: si devolvemos error, MercadoPago reintenta este mismo
            // webhook varias veces, y si el problema es un dato malformado (no
            // transitorio), reintentar no soluciona nada.
            return ResponseEntity.ok("recibido con errores, ver logs del servidor");
        }
    }

    /// Cancelacion con reembolso a medida (casos que no entran en las reglas
    /// automaticas). Solo ADMIN.
    @PostMapping("/reembolso-admin")
    public ReservaResponseDTO reembolsoAdmin(@Valid @RequestBody AdminReembolsoDTO dto, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        if (solicitante.getTipo() != TIPOUSUARIO.ADMIN) {
            throw new ParametroInvalidoException("Solo un administrador puede ejecutar un reembolso a medida.");
        }
        return reservaService.cancelarComoAdminDTO(dto.getIdReserva(), dto.getMontoReembolsoCliente());
    }
}
