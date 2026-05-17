package GestionAlojamiento.Service;


import GestionAlojamiento.DTO.AdministradorModificarDTO;
import GestionAlojamiento.DTO.AdministradorRegistroDTO;
import GestionAlojamiento.Model.Administrador;
import GestionAlojamiento.Model.Enums.TipoUsuario;
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
    private final UsuarioService usuarioService;

    //------------------------ LISTAR POR ------------------------
    public List<Administrador> listarTodos() {
        return administradorRepository.findAll(Sort.by(Sort.Direction.ASC, "usuario.nombre"));
    }

    public Administrador obtenerPorId(Long id){
        return administradorRepository.findById(id).orElseThrow(()->new RuntimeException("Error, id no se encuentra en la base de datos"));
    }

    public List<Administrador> listarPorMatricula() {
        return administradorRepository.findAll(Sort.by(Sort.Direction.ASC, "matricula"));
    }

    //------------------------ CREAR/BORRAR ------------------------
    @Transactional
    public Administrador crear(AdministradorRegistroDTO administradorRegistroDTO) {

        if (administradorRepository.existsByMatricula(administradorRegistroDTO.getMatricula())) {
            throw new RuntimeException("La matrícula ya existe.");
        }
        Usuario usuario = new Usuario();
        usuario.setActivo(true);//valor por defecto
        usuario.setEmail(administradorRegistroDTO.getEmail());
        usuario.setNombre(administradorRegistroDTO.getNombre());
        usuario.setPassword(administradorRegistroDTO.getPassword());
        usuario.setTelefono(administradorRegistroDTO.getTelefono());
        usuario.setTipoUsuario(TipoUsuario.ADMINISTRADOR);

        Administrador administrador = new Administrador();
        administrador.setUsuario(usuario);
        administrador.setMatricula(administradorRegistroDTO.getMatricula());
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
    public Administrador actualizar(AdministradorModificarDTO administradorModificarDTO) {
        Administrador administrador = administradorRepository.findById(administradorModificarDTO.getId()).orElseThrow(() -> new RuntimeException("id invalido, no se encuentra en la base de datos"));

        Usuario usuario = usuarioService.obtenerPorId(administradorModificarDTO.getId());
        if (administradorModificarDTO.getMatricula() != null) {
            throw new RuntimeException("Esa matricula pertenese a otro administrador.");
        }
        if (administradorModificarDTO.getEmail() != null) {
            usuario.setEmail(administradorModificarDTO.getEmail());
        }
        if (administradorModificarDTO.getTelefono() != null) {
            usuario.setTelefono(administradorModificarDTO.getTelefono());
        }
        if (administradorModificarDTO.getPassword() != null) {
            usuario.setPassword(administradorModificarDTO.getPassword());
        }
        if (administradorModificarDTO.isActivo() != usuario.isActivo()) {
            usuario.setActivo(administradorModificarDTO.isActivo());
        }

        if (administradorModificarDTO.getEmail() != null) {
            administrador.setMatricula(administradorModificarDTO.getMatricula());
        }
        return administradorRepository.save(administrador);

    }

}
