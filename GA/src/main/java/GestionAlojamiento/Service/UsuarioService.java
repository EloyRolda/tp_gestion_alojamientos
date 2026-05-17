package GestionAlojamiento.Service;


import GestionAlojamiento.Model.Cliente;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    //------------------------ LISTAR POR ------------------------

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll(Sort.by(Sort.Direction.ASC, "usuario.nombre"));
    }

    public List<Usuario> listarPorNombre(String nombreBuscado) {
        return usuarioRepository.findAll().stream()
                .filter(a -> a.getNombre().equalsIgnoreCase(nombreBuscado)).collect(Collectors.toList());
    }

    public List<Usuario> listarPorFechaRegistro(LocalDate fechaRegistro) {
        return usuarioRepository.findAll().stream()
                .filter(a -> a.getFechaRegistro().equals(fechaRegistro)).collect(Collectors.toList());
    }

    public List<Usuario> listarActivos() {
        return usuarioRepository.findAll().stream()
                .filter(a -> a.isActivo()).collect(Collectors.toList());
    }

    public List<Usuario> listarInactivos() {
        return usuarioRepository.findAll().stream().filter(a -> !a.isActivo()).collect(Collectors.toList());
    }

    public Usuario obtenerPorId(Long id) {

        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
    }

    //------------------------ GUARDAR/BORRAR ------------------------
    @Transactional
    public Usuario crear(Usuario usuario) {
        //Comprobacion si el correo es repetido
        if (usuarioRepository.existsByEmail(usuario.getEmail().toLowerCase())) {
            throw new RuntimeException("Correo registrado en la base de datos por otro usuario.");
        }

        //Normalizamos
        usuario.setEmail(usuario.getEmail().toLowerCase());
        usuario.setNombre(usuario.getNombre().toLowerCase());
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void borrarPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, el id no concuerda con la base de datos");
        }
        usuarioRepository.deleteById(id);
    }
//------------------------ MODIFICAR ------------------------
/* COMENTADO POR INNESESARO, SIEMPRE ACCEDEREMOS A USUARIO MEDIANTE SUS ROLES
    @Transactional
    public Usuario actualizarNombre(Long id, String nuevoNombre) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        usuario.setNombre(nuevoNombre.toLowerCase());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarEmail(Long id, String nuevoEmail) {
        //Comprobacion si el correo es repetido
        if (usuarioRepository.existsByEmail(nuevoEmail.toLowerCase())) {
            throw new RuntimeException("Correo registrado en la base de datos por otro usuario.");
        }

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        usuario.setEmail(nuevoEmail.toLowerCase());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarPassword(Long id, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        usuario.setPassword(nuevaPassword.toLowerCase());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarTelefono(Long id, String nuevoTelefono) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        usuario.setTelefono(nuevoTelefono.toLowerCase());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actializarActividad(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        usuario.setActivo(activo);
        return usuarioRepository.save(usuario);
    }
    @Transactional
    public Usuario actualizarTipoUsuario(Long id, TipoUsuario nuevoTipo) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id no registrado en la base de datos."));
        usuario.setTipoUsuario(nuevoTipo);
        return usuarioRepository.save(usuario);
    }

 */

}
