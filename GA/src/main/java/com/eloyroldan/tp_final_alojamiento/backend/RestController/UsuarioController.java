package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.UsuarioResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AliasCobroInfoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.AliasCobroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.JwtService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication auth) {
        return ResponseEntity.ok(UsuarioResponseDTO.desde(usuarioService.obtenerPorIdentificador(auth.getName())));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        usuarioService.crear(dto);
        return ResponseEntity.ok("Registro exitoso. Te enviamos un correo para verificar tu cuenta (valido por 24 horas).");
    }

    @PostMapping("/registrar/administrador")
    public ResponseEntity<?> registrarAdministrador(@Valid @RequestBody UsuarioRegistroDTO dto) {
        usuarioService.crearAdministrador(dto);
        return ResponseEntity.ok("Administrador registrado correctamente");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos().stream().map(UsuarioResponseDTO::desde).toList());
    }

    @GetMapping("/listar/clientes")
    public ResponseEntity<List<UsuarioResponseDTO>> listarClientes() {
        return ResponseEntity.ok(usuarioService.listarClientes().stream().map(UsuarioResponseDTO::desde).toList());
    }

    @GetMapping("/listar/anfitriones")
    public ResponseEntity<List<UsuarioResponseDTO>> listarAnfitriones() {
        return ResponseEntity.ok(usuarioService.listarAnfitriones().stream().map(UsuarioResponseDTO::desde).toList());
    }

    @GetMapping("/listar/administradores")
    public ResponseEntity<List<UsuarioResponseDTO>> listarAdministradores() {
        return ResponseEntity.ok(usuarioService.listarAdministradores().stream().map(UsuarioResponseDTO::desde).toList());
    }

    @GetMapping("/mostrar/{id}")
    public ResponseEntity<UsuarioResponseDTO> mostrar(@PathVariable Long id, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        boolean esAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin && !solicitante.getId().equals(id)) {
            throw new ParametroInvalidoException("No tiene permiso para ver el perfil de otro usuario.");
        }
        return ResponseEntity.ok(UsuarioResponseDTO.desde(usuarioService.obtenerPorId(id)));
    }

    /// El propio usuario actualiza sus datos (el id se toma de la sesion, no del body).
    /// Si cambia su propio email, el JWT que todavia tiene en el browser quedo con
    /// el email VIEJO como subject (CustomUserDetailsService siempre usa el email
    /// como username interno) — sin reemitirlo aca, el usuario queda deslogueado
    /// a mitad de sesion en el siguiente request (el subject del token ya no
    /// matchea ningun usuario). Se devuelve el token nuevo en el header
    /// "X-New-Token" para que el front lo reemplace de forma transparente.
    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@Valid @RequestBody UsuarioModificarDTO dto, Authentication auth) {
        Usuario usuarioLogueado = usuarioService.obtenerPorIdentificador(auth.getName());
        String emailAnterior = usuarioLogueado.getEmail();
        dto.setId(usuarioLogueado.getId());

        Usuario actualizado = usuarioService.actualizarPropio(dto);

        ResponseEntity.BodyBuilder respuesta = ResponseEntity.ok();
        if (!actualizado.getEmail().equalsIgnoreCase(emailAnterior)) {
            UserDetails nuevoUserDetails = userDetailsService.loadUserByUsername(actualizado.getEmail());
            respuesta.header("X-New-Token", jwtService.generateToken(nuevoUserDetails));
        }
        return respuesta.body(UsuarioResponseDTO.desde(actualizado));
    }

    /// El admin puede editar a cualquier usuario por id, incluyendo activar/desactivar.
    @PutMapping("/actualizar/admin")
    public ResponseEntity<UsuarioResponseDTO> actualizarPorAdmin(@Valid @RequestBody UsuarioModificarDTO dto) {
        return ResponseEntity.ok(UsuarioResponseDTO.desde(usuarioService.actualizarComoAdmin(dto)));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioService.borrarPorId(id);
        return ResponseEntity.ok("Usuario desactivado correctamente");
    }

    //---------------------------------------- ALIAS DE COBRO (Payouts) ----------------------------------------

    /// Paso 1: "pongo el alias -> le doy aceptar -> muestra banco, etc". No guarda nada.
    @PostMapping("/alias-cobro/validar")
    public ResponseEntity<AliasCobroInfoDTO> validarAliasCobro(@Valid @RequestBody AliasCobroDTO dto) {
        return ResponseEntity.ok(usuarioService.validarAliasCobro(dto.getValor()));
    }

    /// Paso 2: "confirmar cuenta -> esta seguro? -> confirmar -> se guarda".
    @PostMapping("/alias-cobro/confirmar")
    public ResponseEntity<AliasCobroInfoDTO> confirmarAliasCobro(@Valid @RequestBody AliasCobroDTO dto, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return ResponseEntity.ok(usuarioService.confirmarAliasCobro(usuario.getId(), dto.getValor()));
    }
}
