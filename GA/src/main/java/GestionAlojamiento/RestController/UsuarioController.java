package GestionAlojamiento.RestController;

import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.UsuarioService;
import jakarta.websocket.OnError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/mostrar/{id}")
    public List<Usuario> listarActivos() {
        return usuarioService.listarActivos();
    }

    @PostMapping("/crear")
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.crear(usuario);
    }

    @DeleteMapping("/borrar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        usuarioService.borrarPorId(id);
    }


}
