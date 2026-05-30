package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.UsuarioModificarDTO;
import GestionAlojamiento.DTO.UsuarioRegistroDTO;
import org.springframework.security.access.AccessDeniedException;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<Usuario> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(auth.getName()));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        usuarioService.crear(dto);
        return ResponseEntity.ok("Se registró correctamente");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/listar/clientes")
    public ResponseEntity<List<Usuario>> listarClientes() {
        return ResponseEntity.ok(usuarioService.listarClientes());
    }

    @GetMapping("/listar/anfitriones")
    public ResponseEntity<List<Usuario>> listarAnfitriones() {
        return ResponseEntity.ok(usuarioService.listarAnfitriones());
    }

    @GetMapping("/listar/administradores")
    public ResponseEntity<List<Usuario>> listarAdministradores() {
        return ResponseEntity.ok(usuarioService.listarAdministradores());
    }


    //  Solo un ADMIN puede ver cualquier usuario. Un usuario común (CLIENTE o ANFITRION) solo puede ver su propio perfil.


    @GetMapping("/mostrar/{id}")
    public ResponseEntity<Usuario> mostrar(@PathVariable Long id, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorEmail(auth.getName());

        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin && !solicitante.getId().equals(id)) {
            throw new AccessDeniedException("No tenés permiso para ver el perfil de otro usuario.");
        }

        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PostMapping("/registrar/administrador")
    public ResponseEntity<?> registrarAdministrador(@Valid @RequestBody UsuarioRegistroDTO dto) {
        usuarioService.crearAdministrador(dto);
        return ResponseEntity.ok("Administrador registrado correctamente");
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Usuario> actualizar(@Valid @RequestBody UsuarioModificarDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioService.borrarPorId(id);
        return ResponseEntity.ok("Usuario desactivado correctamente");
    }
}