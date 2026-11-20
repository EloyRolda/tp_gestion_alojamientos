package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.FavoritoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// Endpoints de favoritos del cliente autenticado (rol CLIENTE, ver SecurityConfig).
/// El usuario sale del JWT: el cliente solo opera sobre sus propios favoritos, nunca
/// sobre los de otro cambiando un id.
@RestController
@RequestMapping("/Favorito")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;
    private final UsuarioService usuarioService;

    @GetMapping("/mios")
    public ResponseEntity<List<AlojamientoResponseDTO>> listarMios(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return ResponseEntity.ok(favoritoService.listarMiosDTO(usuario.getId()));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> idsDeMisFavoritos(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return ResponseEntity.ok(favoritoService.idsDeMisFavoritos(usuario.getId()));
    }

    @PostMapping("/{idAlojamiento}")
    public ResponseEntity<Void> guardar(@PathVariable Long idAlojamiento, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        favoritoService.guardar(usuario, idAlojamiento);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idAlojamiento}")
    public ResponseEntity<Void> quitar(@PathVariable Long idAlojamiento, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        favoritoService.quitar(usuario.getId(), idAlojamiento);
        return ResponseEntity.ok().build();
    }
}
