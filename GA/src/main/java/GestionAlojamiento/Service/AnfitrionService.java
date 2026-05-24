package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.AnfitrionModificarDTO;
import GestionAlojamiento.DTO.AnfitrionRegistroDTO;
import GestionAlojamiento.DTO.ClienteRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Administrador;
import GestionAlojamiento.Model.Anfitrion;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.AnfitrionRepository;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnfitrionService {
    private final AnfitrionRepository anfitrionRepository;
    private final UsuarioService usuarioService;

    //------------------------ LISTAR ------------------------
    public List<Anfitrion> listarTodos() {
        return anfitrionRepository.findAll();
    }

    public Anfitrion obtenerPorId(Long id) {
        return anfitrionRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el anfitrion no existe en la base de datos: " + id));
    }

    //------------------------ Borrar/Crear ------------------------
    @Transactional
    public Anfitrion crear(AnfitrionRegistroDTO anfitrionRegistroDTO) {
        Usuario usuario = new Usuario();

        usuario.setActivo(true);//valor por defecto
        usuario.setEmail(anfitrionRegistroDTO.getEmail());
        usuario.setNombre(anfitrionRegistroDTO.getNombre());
        usuario.setPassword(anfitrionRegistroDTO.getPassword());
        usuario.setTelefono(anfitrionRegistroDTO.getTelefono());
        usuario.setTipoUsuario(TipoUsuario.ANFITRION);

        usuarioService.crear(usuario);

        Anfitrion anfitrion = new Anfitrion();
        anfitrion.setUsuario(usuario);

        return anfitrionRepository.save(anfitrion);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!anfitrionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Anfitrión no encontrado");
        }
        anfitrionRepository.deleteById(id);
    }

    @Transactional
    public Anfitrion actualizar(AnfitrionModificarDTO anfitrionModificarDTO) {

        Anfitrion anfitrion = obtenerPorId(anfitrionModificarDTO.getId());
        Usuario usuario = usuarioService.obtenerPorId(anfitrionModificarDTO.getId());

        if (anfitrionModificarDTO.getEmail() != null) {
            usuario.setEmail(anfitrionModificarDTO.getEmail());
        }
        if (anfitrionModificarDTO.getTelefono() != null) {
            usuario.setTelefono(anfitrionModificarDTO.getTelefono());
        }
        if (anfitrionModificarDTO.getPassword() != null) {
            usuario.setPassword(anfitrionModificarDTO.getPassword());
        }
        if (anfitrionModificarDTO.isActivo() != usuario.isActivo()) {
            usuario.setActivo(anfitrionModificarDTO.isActivo());
        }
        anfitrion.setUsuario(usuario);

        return anfitrionRepository.save(anfitrion);
    }


}
