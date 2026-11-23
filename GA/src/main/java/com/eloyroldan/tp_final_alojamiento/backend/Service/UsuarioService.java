package com.eloyroldan.tp_final_alojamiento.backend.Service;

import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioModificarDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.UsuarioRegistroDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.AliasCobroInfoDTO;
import com.eloyroldan.tp_final_alojamiento.backend.DTO.Response.MotivoReporteDTO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.IdNoEncontradoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.ParametroInvalidoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.RecursoDuplicadoException;
import com.eloyroldan.tp_final_alojamiento.backend.Exception.UnautorizedExeption;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import com.eloyroldan.tp_final_alojamiento.backend.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AliasCobroService aliasCobroService;
    private final EmailService emailService;

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

    /// id de usuario -> "Nombre Apellido", en una sola consulta. Lo usa la
    /// moderacion de reportes para no resolver el nombre del objetivo fila por fila.
    public Map<Long, String> nombresPorIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        List<Long> lista = new ArrayList<>(ids);
        Map<Long, String> resultado = new HashMap<>();
        for (Object[] fila : usuarioRepository.nombresPorIds(lista)) {
            resultado.put((Long) fila[0], (String) fila[1]);
        }
        return resultado;
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

    /// A diferencia de obtenerPorIdentificador, no tira excepcion si no existe.
    /// Pensado para flujos publicos pre-login (ej: reenviar verificacion) donde
    /// filtrar "existe/no existe" por el tipo de error seria un oraculo de
    /// enumeracion de usuarios.
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email.toLowerCase());
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
    /// Si va a desactivar (true->false) pide motivo y delega en suspender();
    /// si va a reactivar (false->true) delega en reactivar().
    @Transactional
    public Usuario actualizarComoAdmin(UsuarioModificarDTO dto) {
        Usuario usuario = obtenerPorId(dto.getId());
        Boolean activoNuevo = dto.getActivo();

        if (activoNuevo != null && !activoNuevo.equals(usuario.getActivo())) {
            if (activoNuevo) {
                reactivar(dto.getId());
            } else {
                if (dto.getMotivoSuspension() == null) {
                    throw new ParametroInvalidoException("Elegí el motivo de la suspensión.");
                }
                suspender(dto.getId(), dto.getMotivoSuspension(), dto.getDetalleSuspension());
            }
        }

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

        return usuarioRepository.save(usuario);
    }

    //---------------------------------------- SUSPENSION / REACTIVACION ----------------------------------------

    /// Da de baja una cuenta por moderación guardando motivo, detalle opcional y fecha.
    /// No permite suspender a un administrador. El correo de aviso se intenta enviar,
    /// pero si Brevo falla no detiene la suspensión.
    @Transactional
    public void suspender(Long id, MOTIVOREPORTE motivo, String detalle) {
        Usuario usuario = obtenerPorId(id);
        if (usuario.getTipo() == TIPOUSUARIO.ADMIN) {
            throw new ParametroInvalidoException("No se puede suspender a un administrador.");
        }

        usuario.setActivo(false);
        usuario.setMotivoSuspension(motivo);
        // Un detalle en blanco se guarda como null (el textarea puede venir con espacios).
        detalle = (detalle == null || detalle.isBlank()) ? null : detalle.trim();
        usuario.setDetalleSuspension(detalle);
        usuario.setFechaSuspension(LocalDateTime.now());
        usuarioRepository.save(usuario);

        try {
            String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
            String etiqueta = MotivoReporteDTO.desde(motivo).getEtiqueta();

            String html = "Hola " + HtmlUtils.htmlEscape(nombreCompleto)
                    + ", tu cuenta fue suspendida. Motivo: " + HtmlUtils.htmlEscape(etiqueta) + ".";
            if (detalle != null && !detalle.isBlank()) {
                html += " Detalle: " + HtmlUtils.htmlEscape(detalle) + ".";
            }
            html += " Si creés que es un error, respondé este correo.";

            emailService.enviar(usuario.getEmail(), nombreCompleto, "Tu cuenta de AlojaME fue suspendida", html);
        } catch (RuntimeException e) {
            log.warn("No se pudo enviar correo de suspensión para usuario {}: {}", usuario.getId(), e.getMessage());
        }
    }

    /// Reactiva una cuenta suspendida limpiando los campos de suspensión y
    /// enviando el correo de reactivación. Si el correo falla, la reactivación queda hecha.
    @Transactional
    public void reactivar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(true);
        usuario.setMotivoSuspension(null);
        usuario.setDetalleSuspension(null);
        usuario.setFechaSuspension(null);
        usuarioRepository.save(usuario);

        try {
            String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
            String html = "Hola " + HtmlUtils.htmlEscape(nombreCompleto)
                    + ", tu cuenta de AlojaME volvió a estar activa.";
            emailService.enviar(usuario.getEmail(), nombreCompleto, "Tu cuenta de AlojaME volvió a estar activa", html);
        } catch (RuntimeException e) {
            log.warn("No se pudo enviar correo de reactivación para usuario {}: {}", usuario.getId(), e.getMessage());
        }
    }

    /// Devuelve el mensaje de suspensión sólo si la contraseña coincide para el
    /// identificador dado. Lo usa AuthController cuando Spring lanza DisabledException.
    public Optional<String> mensajeDeSuspension(String identificador, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailOrUsuarioLoginOrTelefono(
                identificador, identificador, identificador);
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();
        if (!passwordEncoder.matches(password, usuario.getPassword()) || Boolean.TRUE.equals(usuario.getActivo())) {
            return Optional.empty();
        }

        String mensaje = "Tu cuenta está suspendida.";
        if (usuario.getMotivoSuspension() != null) {
            String etiqueta = MotivoReporteDTO.desde(usuario.getMotivoSuspension()).getEtiqueta();
            mensaje = "Tu cuenta está suspendida: " + etiqueta + ".";
            if (usuario.getDetalleSuspension() != null && !usuario.getDetalleSuspension().isBlank()) {
                mensaje += " Detalle: " + usuario.getDetalleSuspension();
            }
        }

        return Optional.of(mensaje);
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
    /// front ya lo valido) y recien se guarda.
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
