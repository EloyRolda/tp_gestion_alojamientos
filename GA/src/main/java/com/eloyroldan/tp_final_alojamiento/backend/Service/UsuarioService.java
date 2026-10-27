package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AliasCobroInfoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.RecursoDuplicadoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.UnautorizedExeption;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AliasCobroService aliasCobroService;

    /// VerificacionService depende de UsuarioService (para marcar el correo
    /// verificado y para buscar por email al reenviar), asi que esta referencia
    /// de vuelta (usada solo una vez, al registrar) se inyecta con @Lazy.
    @Lazy
    private final VerificacionService verificacionService;

    //---------------------------------------- LISTAR ----------------------------------------

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> listarClientes() {
        return usuarioRepository.findAllByTipo(TIPOUSUARIO.CLIENTE);
    }

    public List<Usuario> listarAnfitriones() {
        return usuarioRepository.findAllByTipo(TIPOUSUARIO.ANFITRION);
    }

    public List<Usuario> listarAdministradores() {
        return usuarioRepository.findAllByTipo(TIPOUSUARIO.ADMIN);
    }

    //---------------------------------------- OBTENER ----------------------------------------

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IdNoEncontradoException("Usuario no encontrado: " + id));
    }

    /// Resuelve el usuario autenticado sin importar si inicio sesion con email, usuario_login o telefono.
    public Usuario obtenerPorIdentificador(String identificador) {
        return usuarioRepository.findByEmailOrUsuarioLoginOrTelefono(identificador, identificador, identificador)
                .orElseThrow(() -> new UnautorizedExeption("Usuario no encontrado: " + identificador));
    }

    public Usuario obtenerAnfitrionPorId(Long id) {
        Usuario u = obtenerPorId(id);
        if (u.getTipo() != TIPOUSUARIO.ANFITRION) {
            throw new ParametroInvalidoException("El usuario indicado no es un anfitrion.");
        }
        return u;
    }

    public Usuario obtenerClientePorId(Long id) {
        Usuario u = obtenerPorId(id);
        if (u.getTipo() != TIPOUSUARIO.CLIENTE) {
            throw new ParametroInvalidoException("El usuario indicado no es un cliente.");
        }
        return u;
    }

    //---------------------------------------- CREAR ----------------------------------------

    @Transactional
    /// Crea un usuario (CLIENTE o ANFITRION) validando duplicados de email, telefono y
    /// usuario_login. Queda con emailVerificado=false y se dispara el mail de verificacion.
    public Usuario crear(UsuarioRegistroDTO dto) {

        if (dto.getTipo() == TIPOUSUARIO.ADMIN) {
            throw new ParametroInvalidoException("No se puede registrar un administrador desde este endpoint.");
        }

        validarDuplicados(dto.getEmail(), dto.getTelefono(), dto.getUsuarioLogin());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setUsuarioLogin(dto.getUsuarioLogin().toLowerCase());
        usuario.setEmail(dto.getEmail().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setActivo(true);
        usuario.setEmailVerificado(false);
        usuario.setTipo(dto.getTipo());

        Usuario guardado = usuarioRepository.save(usuario);
        verificacionService.generarYEnviar(guardado);
        return guardado;
    }

    @Transactional
    /// Crea un administrador. Solo deberia poder invocarse con un admin ya autenticado;
    /// para el primer admin de una base nueva se recomienda insertarlo directo por SQL.
    /// Se crea ya verificado: no pasa por el alta publica ni por el mail de confirmacion.
    public Usuario crearAdministrador(UsuarioRegistroDTO dto) {
        validarDuplicados(dto.getEmail(), dto.getTelefono(), dto.getUsuarioLogin());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setUsuarioLogin(dto.getUsuarioLogin().toLowerCase());
        usuario.setEmail(dto.getEmail().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setActivo(true);
        usuario.setEmailVerificado(true);
        usuario.setTipo(TIPOUSUARIO.ADMIN);

        return usuarioRepository.save(usuario);
    }

    private void validarDuplicados(String email, String telefono, String usuarioLogin) {
        if (usuarioRepository.existsByEmail(email.toLowerCase())) {
            throw new RecursoDuplicadoException("El correo ya esta registrado.");
        }
        if (usuarioRepository.existsByTelefono(telefono)) {
            throw new RecursoDuplicadoException("El telefono ya esta registrado.");
        }
        if (usuarioRepository.existsByUsuarioLogin(usuarioLogin.toLowerCase())) {
            throw new RecursoDuplicadoException("El usuario ya esta en uso.");
        }
    }

    //---------------------------------------- MODIFICAR ----------------------------------------

    /// El propio usuario actualiza su perfil. No puede tocar su propio "activo"
    /// (si un admin lo dio de baja, no deberia poder reactivarse a si mismo).
    @Transactional
    public Usuario actualizarPropio(UsuarioModificarDTO dto) {
        dto.setActivo(null);
        return actualizar(dto);
    }

    /// El admin puede editar cualquier campo de cualquier usuario, incluido "activo".
    @Transactional
    public Usuario actualizarComoAdmin(UsuarioModificarDTO dto) {
        return actualizar(dto);
    }

    private Usuario actualizar(UsuarioModificarDTO dto) {

        Usuario usuario = obtenerPorId(dto.getId());

        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            usuario.setApellido(dto.getApellido());
        }
        if (dto.getUsuarioLogin() != null && !dto.getUsuarioLogin().equalsIgnoreCase(usuario.getUsuarioLogin())) {
            if (usuarioRepository.existsByUsuarioLogin(dto.getUsuarioLogin().toLowerCase())) {
                throw new RecursoDuplicadoException("El usuario ya esta en uso.");
            }
            usuario.setUsuarioLogin(dto.getUsuarioLogin().toLowerCase());
        }
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail().toLowerCase())) {
                throw new RecursoDuplicadoException("El correo ya esta en uso por otro usuario.");
            }
            usuario.setEmail(dto.getEmail().toLowerCase());
        }
        if (dto.getTelefono() != null && !dto.getTelefono().equals(usuario.getTelefono())) {
            if (usuarioRepository.existsByTelefono(dto.getTelefono())) {
                throw new RecursoDuplicadoException("El telefono ya esta en uso por otro usuario.");
            }
            usuario.setTelefono(dto.getTelefono());
        }
        if (dto.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }

        return usuarioRepository.save(usuario);
    }

    //---------------------------------------- BORRAR (logico) ----------------------------------------

    @Transactional
    public void borrarPorId(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    //---------------------------------------- VERIFICACION DE CORREO ----------------------------------------

    /// Usado por VerificacionService al consumir un token valido.
    @Transactional
    public void marcarEmailVerificado(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);
    }

    //---------------------------------------- ALIAS DE COBRO (payouts) ----------------------------------------

    /// Paso 1 del flujo "pongo el alias -> veo el banco -> confirmo": solo valida,
    /// NO guarda nada todavia.
    public AliasCobroInfoDTO validarAliasCobro(String valor) {
        return aliasCobroService.validar(valor);
    }

    /// Paso 2: se vuelve a validar del lado del servidor (nunca confiar en que el
    /// front ya lo valido) y reci entonces se guarda.
    @Transactional
    public AliasCobroInfoDTO confirmarAliasCobro(Long idUsuario, String valor) {
        AliasCobroInfoDTO info = aliasCobroService.validar(valor);
        if (!info.isValido()) {
            throw new ParametroInvalidoException(info.getMensaje());
        }
        Usuario usuario = obtenerPorId(idUsuario);
        usuario.setAliasCobro(valor.trim());
        usuarioRepository.save(usuario);
        return info;
    }
}
