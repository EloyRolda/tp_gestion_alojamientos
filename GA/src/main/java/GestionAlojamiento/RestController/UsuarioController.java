package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.UsuarioRegistroDTO;
import GestionAlojamiento.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /*
    //El login lo maneja spring security
        @PostMapping("/login")
        public ResponseEntity<?> Login(@Valid @RequestBody LoginDTO loginDTO, HttpSession httpSession) {
            Usuario usuario = usuarioService.login(loginDTO);
            httpSession.setAttribute("user_id", usuario.getId());
            httpSession.setAttribute("user_rol", usuario.getTipoUsuario());
            return ResponseEntity.ok().body("Se inicio sesion");
        }
    */
    @PostMapping("/registrar")
    public ResponseEntity<?> registro(@Valid @RequestBody UsuarioRegistroDTO usuarioRegistroDTO, HttpSession httpSession) {
        usuarioService.crear(usuarioRegistroDTO);
        return ResponseEntity.ok().body("Se registro correctamente");
    }

}
