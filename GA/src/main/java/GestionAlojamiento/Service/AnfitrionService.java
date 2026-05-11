package GestionAlojamiento.Service;

import GestionAlojamiento.Model.Anfitrion;
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
    private final UsuarioRepository usuarioRepository;

    //------------------------ TEXT ------------------------
    public List<Anfitrion> listarTodos() {
        return anfitrionRepository.findAll();
    }

    public Anfitrion obtenerPorId(Long id) {
        return anfitrionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anfitrión no encontrado con ID: " + id));
    }

    //------------------------ Borrar/Crear ------------------------
    @Transactional
    public Anfitrion crear(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        if (anfitrionRepository.existsById(idUsuario)) {
            throw new RuntimeException("El usuario ya está registrado como anfitrión");
        }

        Anfitrion anfitrion = new Anfitrion();
        anfitrion.setUsuario(usuario);

        return anfitrionRepository.save(anfitrion);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!anfitrionRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Anfitrión no encontrado");
        }
        anfitrionRepository.deleteById(id);
    }
}
