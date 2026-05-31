package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.ReservaModificarDTO;
import GestionAlojamiento.DTO.ReservaRegistroDTO;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Reserva;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.ReservaService;
import GestionAlojamiento.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Reserva")
public class ReservaController {
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<Reserva> listar() {
        return reservaService.listarTodos();
    }

    @GetMapping("/mostrar/{id}")
    public Reserva mostrarPorId(@PathVariable Long id) {
        return reservaService.obtenerPorId(id);
    }

    /// Permite ver todos las reservas propias
    @GetMapping("/listar/propios")
    public List<Reserva> listarPropios() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return reservaService.listarPorUsuario(auth.getName());
    }


    @PostMapping("/registrar")
    public Reserva registrar(@RequestBody ReservaRegistroDTO reservaRegistroDTO) {
        return reservaService.crear(reservaRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Reserva modificar(@RequestBody ReservaModificarDTO reservaModificarDTO) {
        return reservaService.actualizar(reservaModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuarioLogueado = usuarioService.obtenerPorEmail(email);

        Reserva reserva = reservaService.obtenerPorId(id);

        if (!reserva.getCliente()
                .getId()
                .equals(usuarioLogueado.getId())) {

            throw new ParametroInvalidoException("No autorizado para eliminar esta reserva");
        }

        reservaService.borrarPorId(id);
    }
}
