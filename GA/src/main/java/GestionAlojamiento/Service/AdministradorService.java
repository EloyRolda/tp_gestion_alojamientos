package GestionAlojamiento.Service;


import GestionAlojamiento.Model.Administrador;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.AdministradorRepository;
import GestionAlojamiento.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;

    //------------------------ LISTAR POR ------------------------
    public List<Administrador> listarTodos() {
        return administradorRepository.findAll(Sort.by(Sort.Direction.ASC, "usuario.nombre"));
    }

    public List<Administrador> listarPorMatricula() {
        return administradorRepository.findAll(Sort.by(Sort.Direction.ASC, "matricula"));
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Administrador crear(Long idUsuario, String matricula) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos."));
        if (administradorRepository.existsById(idUsuario)) {
            throw new RuntimeException("El usuario ya está registrado como Administrador.");
        }
        if (administradorRepository.existsByMatricula(matricula)) {
            throw new RuntimeException("La matrícula ya existe.");
        }

        Administrador administrador = new Administrador();
        administrador.setUsuario(usuario);
        administrador.setMatricula(matricula);
        return administradorRepository.save(administrador);
    }

    @Transactional
    public void borrarPorId(Long idAdmin) {
        if (!administradorRepository.existsById(idAdmin)) {
            throw new RuntimeException("id invalido, no se encuentra en la base de datos.");
        }
        administradorRepository.deleteById(idAdmin);
    }

    //------------------------  Actualizar  ------------------------
    @Transactional
    public Administrador actualizarMatricula(Long idAdmin, String nuevaMatricula) {
        Administrador administrador = administradorRepository.findById(idAdmin).orElseThrow(() -> new RuntimeException("id invalido, no se encuentra en la base de datos"));

        if (administradorRepository.existsByMatricula(nuevaMatricula)) {
            throw new RuntimeException("Esa matricula pertenese a otro administrador.");
        }
        administrador.setMatricula(nuevaMatricula);
        return administradorRepository.save(administrador);

    }

}
