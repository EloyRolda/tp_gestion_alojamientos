package com.eloyroldan.tp_final_alojamiento.backend.RestController;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReservaModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.ReservaRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.ReservaResponseDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Service.ReservaService;
import com.eloyroldan.tp_final_alojamiento.backend.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Reserva")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<ReservaResponseDTO> listar() {
        return reservaService.listarTodosDTO();
    }

    @GetMapping("/mostrar/{id}")
    public ReservaResponseDTO mostrar(@PathVariable Long id, Authentication auth) {
        return reservaService.obtenerDTOVerificandoAcceso(id, auth.getName());
    }

    @GetMapping("/listar/propios")
    public List<ReservaResponseDTO> listarPropios(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return reservaService.listarPorClienteDTO(usuario.getId());
    }

    @GetMapping("/listar/anfitrion")
    public List<ReservaResponseDTO> listarDeMisAlojamientos(Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return reservaService.listarPorAnfitrionDTO(usuario.getId());
    }

    @PostMapping("/registrar")
    public ReservaResponseDTO registrar(@Valid @RequestBody ReservaRegistroDTO dto, Authentication auth) {
        Usuario usuario = usuarioService.obtenerPorIdentificador(auth.getName());
        return reservaService.crearDTO(dto, usuario);
    }

    /// Reprogramar fechas: solo el cliente dueño, y solo mientras este SOLICITADA.
    @PutMapping("/actualizar")
    public ReservaResponseDTO actualizar(@Valid @RequestBody ReservaModificarDTO dto, Authentication auth) {
        return reservaService.actualizarDTO(dto, auth.getName());
    }

    /// El anfitrion dueño (o admin) acepta la reserva.
    @PatchMapping("/confirmar/{id}")
    public ReservaResponseDTO confirmar(@PathVariable Long id, Authentication auth) {
        return reservaService.confirmarDTO(id, auth.getName());
    }

    /// Cliente dueño, anfitrion dueño o admin pueden cancelar.
    @PatchMapping("/cancelar/{id}")
    public ReservaResponseDTO cancelar(@PathVariable Long id, Authentication auth) {
        return reservaService.cancelarDTO(id, auth.getName());
    }

    /// El anfitrion dueño (o admin) finaliza una reserva confirmada ya vencida.
    @PatchMapping("/finalizar/{id}")
    public ReservaResponseDTO finalizar(@PathVariable Long id, Authentication auth) {
        return reservaService.finalizarDTO(id, auth.getName());
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        reservaService.borrarPorId(id);
    }
}
