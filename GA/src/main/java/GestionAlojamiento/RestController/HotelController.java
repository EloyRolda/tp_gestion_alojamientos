package GestionAlojamiento.RestController;


import GestionAlojamiento.DTO.HotelModificarDTO;
import GestionAlojamiento.DTO.HotelRegistroDTO;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.HotelService;
import GestionAlojamiento.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Hotel")
public class HotelController {

    private final HotelService hotelService;
    private final UsuarioService usuarioService;

    /// Permite ver todos los hoteles registrados
    @GetMapping("/listar")
    public List<Hotel> listar() {
        return hotelService.listarTodos();
    }


    /// Permite ver todos los hoteles registrados
    @GetMapping("/listar/propios")
    public List<Hotel> listarPropios() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return hotelService.listarPorAnfitrion(auth.getName());
    }


    @GetMapping("/mostrar/{id}")
    public Hotel mostrarPorId(@PathVariable Long id) {
        return hotelService.obtenerPorId(id);
    }

    @PostMapping("/registrar")
    public Hotel registrar(@RequestBody HotelRegistroDTO hotelRegistroDTO) {
        return hotelService.crear(hotelRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Hotel actualizar(@RequestBody HotelModificarDTO hotelModificarDTO) {
        return hotelService.actualizar(hotelModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email del usuario logueado
        Usuario usuarioLogueado = usuarioService.obtenerPorEmail(email);

        if (!hotelService.obtenerPorId(id).getAlojamiento().getAnfitrion().getId().equals(usuarioLogueado.getId())  && usuarioLogueado.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            throw new ParametroInvalidoException("Error al borrar, el hotel no es de su autiroia");
        }

        hotelService.borrarPorId(id);
    }

}
