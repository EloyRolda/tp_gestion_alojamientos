package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.AnfitrionModificarDTO;
import GestionAlojamiento.DTO.AnfitrionRegistroDTO;
import GestionAlojamiento.DTO.ClienteRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Model.Administrador;
import GestionAlojamiento.Model.Anfitrion;
import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoEstado;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.AnfitrionRepository;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnfitrionService {
    private final AnfitrionRepository anfitrionRepository;
    private final UsuarioService usuarioService;

    //---------------------------------------- LISTAR ----------------------------------------
    public List<Anfitrion> listarTodos() {
        return anfitrionRepository.findAll();
    }

    public Anfitrion obtenerPorId(Long id) {
        return anfitrionRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Error, el anfitrion no existe en la base de datos: " + id));
    }

    //---------------------------------------- CREAR ----------------------------------------
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

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long id) {
        if (!anfitrionRepository.existsById(id)) {
            throw new IdNoEncontradoException("Anfitrion no encontrado");
        }
        anfitrionRepository.deleteById(id);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Anfitrion actualizar(AnfitrionModificarDTO anfitrionModificarDTO) {

        Anfitrion anfitrion = obtenerPorId(anfitrionModificarDTO.getId());
        Usuario usuario = usuarioService.obtenerPorId(anfitrionModificarDTO.getId());

        usuario = usuarioService.modificarObjeto(usuario, mapearUsuario(anfitrionModificarDTO));
        anfitrion.setUsuario(usuario);

        return anfitrionRepository.save(anfitrion);
    }

    //---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    /// Mapea el DTO de MODIFICAR a un USUARIO y lo RETORNA
    public Usuario mapearUsuario(AnfitrionRegistroDTO anfitrionRegistroDTO) {
        Usuario usuario = new Usuario(
                null,
                anfitrionRegistroDTO.getNombre(),
                anfitrionRegistroDTO.getEmail(),
                anfitrionRegistroDTO.getPassword(),
                anfitrionRegistroDTO.getTelefono(),
                LocalDateTime.now(),
                true,
                TipoUsuario.ANFITRION
        );
        return usuario;
    }

    /// Mapea el DTO de MODIFICAR a un USUARIO y lo RETORNA
    public Usuario mapearUsuario(AnfitrionModificarDTO anfitrionModificarDTO) {
        Usuario usuario = new Usuario(
                anfitrionModificarDTO.getId(),
                anfitrionModificarDTO.getNombre(),
                anfitrionModificarDTO.getEmail(),
                anfitrionModificarDTO.getPassword(),
                anfitrionModificarDTO.getTelefono(),
                null,   //Fecha registro
                anfitrionModificarDTO.getActivo(),
                TipoUsuario.ANFITRION
        );
        return usuario;
    }

}
