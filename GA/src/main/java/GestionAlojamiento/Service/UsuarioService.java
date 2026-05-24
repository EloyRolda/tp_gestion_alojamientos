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
}
