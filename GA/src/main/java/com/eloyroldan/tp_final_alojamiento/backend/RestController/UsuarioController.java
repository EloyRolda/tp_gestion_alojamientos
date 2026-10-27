package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.UsuarioResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AliasCobroInfoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.AliasCobroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
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
    @PutMapping("/actualizar")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@Valid @RequestBody UsuarioModificarDTO dto, Authentication auth) {
        Usuario usuarioLogueado = usuarioService.obtenerPorIdentificador(auth.getName());
        dto.setId(usuarioLogueado.getId());
        return ResponseEntity.ok(UsuarioResponseDTO.desde(usuarioService.actualizarPropio(dto)));
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
