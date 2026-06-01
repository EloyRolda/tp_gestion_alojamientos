package GestionAlojamiento.RestController;

import GestionAlojamiento.DTO.CasaModificarDTO;
import GestionAlojamiento.DTO.CasaRegistroDTO;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Model.Casa;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Hotel;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Service.CasaService;
import GestionAlojamiento.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/Casa")
public class CasaController {

    private final CasaService casaService;
    private final UsuarioService usuarioService;

    @GetMapping("/listar")
    public List<Casa> listar() {
        return casaService.listar();
    }

    @GetMapping("/mostrar/{id}")
    public Casa mostrarPorId(@PathVariable Long id) {
        return casaService.obtenerPorId(id);
    }

    /// Permite ver todos los hoteles registrados
    @GetMapping("/listar/propios")
    public List<Casa> listarPropios() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return casaService.listarPorAnfitrion(auth.getName());
    }

    @PostMapping("/registrar")
    public Casa registrar(@RequestBody CasaRegistroDTO casaRegistroDTO) {
        return casaService.crear(casaRegistroDTO);
    }

    @PutMapping("/actualizar")
    public Casa actualizar(@RequestBody CasaModificarDTO casaModificarDTO) {
        return casaService.modificar(casaModificarDTO);
    }

    @DeleteMapping("/eliminar/{id}")
    public void borrarPorId(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario usuarioLogueado = usuarioService.obtenerPorEmail(email);

        Casa casa = casaService.obtenerPorId(id);

        if (!casa.getAlojamiento()
                .getAnfitrion()
                .getId()
                .equals(usuarioLogueado.getId()) && usuarioLogueado.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {

            throw new ParametroInvalidoException("No autorizado para eliminar esta casa");
        }

        casaService.borrarPorId(id);
    }
}
