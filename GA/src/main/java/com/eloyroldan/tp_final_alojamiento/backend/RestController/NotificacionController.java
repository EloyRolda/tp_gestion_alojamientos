package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.NotificacionDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.NotificacionService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// API de notificaciones internas del usuario autenticado.
@RestController
@RequestMapping("/Notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;

    @GetMapping("/mias")
    public List<NotificacionDTO> listarMias(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return notificacionService.listarMias(usuario.getId());
    }

    @GetMapping("/sin-leer")
    public long contarSinLeer(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return notificacionService.contarSinLeer(usuario.getId());
    }

    @PostMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        notificacionService.marcarLeida(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leidas")
    public ResponseEntity<Void> marcarTodas(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        notificacionService.marcarTodasLeidas(usuario.getId());
        return ResponseEntity.noContent().build();
    }
}
