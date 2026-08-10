package GestionAlojamiento.RestController;

import GestionAlojamiento.Model.Notificacion;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.NotificacionService;
import GestionAlojamiento.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<Notificacion> listar(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorEmail(auth.getName());
        return notificacionService.listarPorUsuario(usuario.getId());
    }

    @GetMapping("/no-leidas/cantidad")
    public Map<String, Long> contarNoLeidas(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorEmail(auth.getName());
        return Map.of("cantidad", notificacionService.contarNoLeidas(usuario.getId()));
    }

    @PatchMapping("/{id}/marcar-leida")
    public Notificacion marcarLeida(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorEmail(auth.getName());
        return notificacionService.marcarLeida(id, usuario.getId());
    }

    @PatchMapping("/marcar-todas-leidas")
    public void marcarTodasLeidas(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorEmail(auth.getName());
        notificacionService.marcarTodasLeidas(usuario.getId());
    }
}
