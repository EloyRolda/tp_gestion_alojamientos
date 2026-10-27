package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.AlojamientoModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.AlojamientoRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ServicioxAlojamientoResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.AlojamientoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.Servicio.ServicioxAlojamientoService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Alojamiento")
@RequiredArgsConstructor
public class AlojamientoController {

    private final AlojamientoService alojamientoService;
    private final UsuarioService usuarioService;
    private final ServicioxAlojamientoService servicioxAlojamientoService;

    @GetMapping("/listar")
    public List<AlojamientoResponseDTO> listar() {
        return alojamientoService.listarTodosDTO();
    }

    @GetMapping("/mostrar/{id}")
    public AlojamientoResponseDTO mostrar(@PathVariable Long id) {
        return alojamientoService.obtenerDTOPorId(id);
    }

    @GetMapping("/listar/propios")
    public List<AlojamientoResponseDTO> listarPropios(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return alojamientoService.listarPorAnfitrionDTO(usuario.getId());
    }

    @PostMapping("/registrar")
    public AlojamientoResponseDTO registrar(@Valid @RequestBody AlojamientoRegistroDTO dto, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return alojamientoService.crearDTO(dto, usuario);
    }

    @PutMapping("/actualizar")
    public AlojamientoResponseDTO actualizar(@Valid @RequestBody AlojamientoModificarDTO dto, Authentication auth) {
        verificarDuenioOAdmin(dto.getIdAlojamiento(), auth);
        return alojamientoService.modificarDTO(dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id, Authentication auth) {
        verificarDuenioOAdmin(id, auth);
        alojamientoService.borrarPorId(id);
    }

    @GetMapping("/{id}/servicios")
    public List<ServicioxAlojamientoResponseDTO> listarServicios(@PathVariable Long id) {
        return servicioxAlojamientoService.listarPorAlojamientoDTO(id);
    }

    @PostMapping("/{id}/servicios")
    public void agregarServicios(@PathVariable Long id, @RequestBody List<String> nombresServicios, Authentication auth) {
        verificarDuenioOAdmin(id, auth);
        Alojamiento alojamiento = alojamientoService.obtenerPorId(id);
        servicioxAlojamientoService.asignarServicios(alojamiento, nombresServicios);
    }

    @DeleteMapping("/{id}/servicios/{idServicio}")
    public void quitarServicio(@PathVariable Long id, @PathVariable Long idServicio, Authentication auth) {
        verificarDuenioOAdmin(id, auth);
        servicioxAlojamientoService.quitarServicio(id, idServicio);
    }

    /// Recibe solo el id (no la entidad) para no tener que navegar Alojamiento.anfitrion
    /// (relacion LAZY) desde el controller, fuera de una transaccion.
    private void verificarDuenioOAdmin(Long idAlojamiento, Authentication auth) {
        Usuario solicitante = usuarioService.obtenerPorIdentificador(auth.getName());
        boolean esAdmin = solicitante.getTipo() == TIPOUSUARIO.ADMIN;
        boolean esDuenio = alojamientoService.obtenerIdAnfitrion(idAlojamiento).equals(solicitante.getId());
        if (!esAdmin && !esDuenio) {
            throw new ParametroInvalidoException("No autorizado: el alojamiento no le pertenece.");
        }
    }
}
