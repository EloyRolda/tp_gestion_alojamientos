package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.UsuarioModificarDTO;
import GestionAlojamiento.DTO.UsuarioRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.ParametroInvalidoException;
import GestionAlojamiento.Exception.RecursoDuplicadoException;
import GestionAlojamiento.Exception.UnautorizedExeption;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    //---------------------------------------- LISTAR ----------------------------------------

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarClientes() {
        return usuarioRepository.findAllByTipoUsuario(TipoUsuario.CLIENTE);
    }

    public List<Usuario> listarAnfitriones() {
        return usuarioRepository.findAllByTipoUsuario(TipoUsuario.ANFITRION);
    }

    public List<Usuario> listarAdministradores() {
        return usuarioRepository.findAllByTipoUsuario(TipoUsuario.ADMINISTRADOR);
    }

    //---------------------------------------- Obtener por ID o Correo ----------------------------------------

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Usuario no encontrado: " + id));
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UnautorizedExeption("Usuario no encontrado: " + email));
    }

    //---------------------------------------- CREAR ----------------------------------------

    @Transactional
    public Usuario crear(UsuarioRegistroDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail().toLowerCase())) {
            throw new RecursoDuplicadoException("El correo ya está registrado.");
        }

        if (dto.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            throw new ParametroInvalidoException("No se puede registrar un administrador desde este endpoint.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().toLowerCase());
        usuario.setEmail(dto.getEmail().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setTipoUsuario(dto.getTipoUsuario());

        // Campos específicos por rol
        if (dto.getTipoUsuario() == TipoUsuario.CLIENTE && dto.getMetodoPago() != null) {
            usuario.setMetodo_pago(dto.getMetodoPago());
        }

        return usuarioRepository.save(usuario);
    }

    // Crear admin
    @Transactional
    public Usuario crearAdministrador(UsuarioRegistroDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail().toLowerCase())) {
            throw new RecursoDuplicadoException("El correo ya está registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().toLowerCase());
        usuario.setEmail(dto.getEmail().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        usuario.setMatricula(dto.getMatricula());

        return usuarioRepository.save(usuario);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Usuario actualizar(UsuarioModificarDTO dto) {
        
        Usuario usuario = obtenerPorId(dto.getId());

        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre().toLowerCase());
        }

        if (dto.getEmail() != null) {
            // Verificar que el nuevo email no lo tenga otro usuario
            usuarioRepository.findByEmail(dto.getEmail().toLowerCase()).ifPresent(existente -> {
                if (!existente.getId().equals(usuario.getId())) {
                    throw new RecursoDuplicadoException("El correo ya está en uso por otro usuario.");
                }
            });
            usuario.setEmail(dto.getEmail().toLowerCase());
        }

        if (dto.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getTelefono() != null) {
            usuario.setTelefono(dto.getTelefono());
        }

        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }

        if (dto.getMetodoPago() != null) {
            usuario.setMetodo_pago(dto.getMetodoPago());
        }

        if (dto.getMatricula() != null) {
            usuario.setMatricula(dto.getMatricula());
        }

        return usuarioRepository.save(usuario);
    }

    //---------------------------------------- BORRAR/DESACTIVAR ----------------------------------------

    @Transactional
    public void borrarPorId(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public Usuario obtenerAnfitrionPorId(Long id) {
        Usuario u = obtenerPorId(id);
        if (u.getTipoUsuario() != TipoUsuario.ANFITRION) {
            throw new ParametroInvalidoException("El usuario indicado no es un anfitrión.");
        }
        return u;
    }

    public Usuario obtenerClientePorId(Long id) {
        Usuario u = obtenerPorId(id);
        if (u.getTipoUsuario() != TipoUsuario.CLIENTE) {
            throw new ParametroInvalidoException("El usuario indicado no es un cliente.");
        }
        return u;
    }




    @Transactional
    public Usuario crear(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}