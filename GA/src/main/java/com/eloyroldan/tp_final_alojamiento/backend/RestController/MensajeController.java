/// Endpoints REST para la mensajería dentro de una reserva.
/// Solo CLIENTE y ANFITRION pueden entrar; el detalle de si participan en la
/// reserva lo valida MensajeService.
package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.MensajeRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ConversacionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MensajeDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Service.MensajeService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Mensaje")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;
    private final UsuarioService usuarioService;

    @GetMapping("/reserva/{idReserva}")
    public ConversacionDTO conversacion(@PathVariable Long idReserva,
                                        Authentication auth) {
        return mensajeService.conversacion(idReserva, usuarioService.obtenerPorIdentificador(auth.getName()).getId());
    }

    @PostMapping("/reserva/{idReserva}")
    public ResponseEntity<MensajeDTO> enviar(@PathVariable Long idReserva,
                                             @Valid @RequestBody MensajeRegistroDTO dto,
                                             Authentication auth) {
        MensajeDTO mensaje = mensajeService.enviar(idReserva, usuarioService.obtenerPorIdentificador(auth.getName()).getId(), dto.getTexto());
        return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
    }

    @PostMapping("/reserva/{idReserva}/leidos")
    public ResponseEntity<Void> marcarLeidos(@PathVariable Long idReserva,
                                             Authentication auth) {
        mensajeService.marcarLeidos(idReserva, usuarioService.obtenerPorIdentificador(auth.getName()).getId());
        return ResponseEntity.noContent().build();
    }
}
