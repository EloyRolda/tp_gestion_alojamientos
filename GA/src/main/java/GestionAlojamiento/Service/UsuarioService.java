package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.LoginDTO;
import GestionAlojamiento.DTO.UsuarioRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Exception.RecursoDuplicadoException;
import GestionAlojamiento.Exception.UnautorizedExeption;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    //Logueo
    public Usuario login(LoginDTO login) {
        Usuario usuario = obtenerPorCorreo(login.getEmail());
        if (!passwordEncoder.matches(login.getPassword(), usuario.getPassword())) {
            throw new UnautorizedExeption("Contraseña incorrecta.");
        }
        return usuario;
    }

    //------------------------ LISTAR POR ------------------------
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Usuario no encontrado en la base de datos"));
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByEmail(correo).orElseThrow(() -> new UnautorizedExeption("Usuario no encontrado en la base de datos"));
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
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            throw new RecursoDuplicadoException("Correo registrado en la base de datos por otro usuario.");
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario crear(UsuarioRegistroDTO usuarioRegistroDTO) {

        if (usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail().toLowerCase())) {
            throw new RecursoDuplicadoException("Correo registrado en la base de datos por otro usuario.");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRegistroDTO.getNombre().toLowerCase());
        usuario.setEmail(usuarioRegistroDTO.getEmail().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(usuarioRegistroDTO.getPassword()));
        usuario.setTelefono(usuarioRegistroDTO.getTelefono());
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setTipoUsuario(usuarioRegistroDTO.getTipoUsuario());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    ///desabilita un usuario de la base de datos.
    public void borrarPorId(Long id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("ID NO ENCONTRADO"));
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
            original.setPassword(passwordEncoder.encode(cambios.getPassword()));
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
