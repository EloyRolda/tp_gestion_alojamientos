package GestionAlojamiento.Service;


import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.RecursoDuplicadoException;
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
    public Usuario obtenerPorId(Long id) {

        return usuarioRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Usuario no encontrado en la base de datos"));
    }

    //------------------------ GUARDAR/BORRAR ------------------------
    @Transactional
    public Usuario crear(Usuario usuario) {
        //Comprobacion si el correo es repetido
        if (!usuarioRepository.existsByEmail(usuario.getEmail().toLowerCase())) {
            //Normalizamos
            usuario.setEmail(usuario.getEmail().toLowerCase());
            usuario.setNombre(usuario.getNombre().toLowerCase());
            usuario.setActivo(true);
            usuario.setFechaRegistro(LocalDateTime.now());
        } else {
            throw new RuntimeException("Correo registrado en la base de datos por otro usuario.");
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    ///desabilita un usuario de la base de datos.
    public void borrarPorId(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, el id no concuerda con la base de datos");
        }
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("ID no encontrado en la base de datos" + id));
        u.setActivo(false);
        usuarioRepository.save(u);

        //usuarioRepository.deleteById(id);
    }
//---------------------------------------- MODIFICAR ----------------------------------------

    /// Modifica el objeto USUARIO SIN TOCAR LA BASE DE DATOS LLAMANDO y devuelve el objeto modificado
    /// Original hace referencia al objeto que queremos cambiar, CAMBIOS hace referencia a los campos modificados
    public Usuario modificarObjeto(Usuario original, Usuario cambios) {

        if (cambios.getNombre() != null) {
            original.setNombre(cambios.getNombre());
        }

        if (cambios.getEmail() != null) {

            Usuario usuarioExistente = usuarioRepository
                    .findByEmail(cambios.getEmail())
                    .orElse(null);

            if (usuarioExistente != null && !usuarioExistente.getId().equals(original.getId())) {
                throw new RecursoDuplicadoException("Correo existente en la base de datos.");
            }

            original.setEmail(cambios.getEmail());
        }

        if (cambios.getPassword() != null) {
            original.setPassword(cambios.getPassword());
        }

        if (cambios.getTelefono() != null) {
            original.setTelefono(cambios.getTelefono());
        }

        if (cambios.getActivo() != null) {
            original.setActivo(cambios.getActivo());
        }

        return original;
    }
}
