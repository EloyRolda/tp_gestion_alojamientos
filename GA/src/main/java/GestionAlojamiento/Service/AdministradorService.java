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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
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
        Usuario  usuario = new Usuario();
        usuario.setActivo(true);//valor por defecto
        usuario.setEmail(administradorRegistroDTO.getEmail());
        usuario.setNombre(administradorRegistroDTO.getNombre());
        usuario.setPassword(administradorRegistroDTO.getPassword());
        usuario.setTelefono(administradorRegistroDTO.getTelefono());
        usuario.setTipoUsuario(TipoUsuario.ADMINISTRADOR);
        usuario.setFechaRegistro(LocalDateTime.now());
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
    public Administrador actualizar(AdministradorModificarDTO dto) {

        Administrador admin = administradorRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("ID inválido"));

        Usuario usuario = usuarioService.obtenerPorId(dto.getId());

        if (dto.getMatricula() != null) {
            boolean existe = administradorRepository
                    .existsByMatriculaAndIdNot(dto.getMatricula(), dto.getId());

            if (existe) {
                throw new RuntimeException("La matrícula pertenece a otro administrador.");
            }

            admin.setMatricula(dto.getMatricula());
        }

        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getTelefono() != null) {
            usuario.setTelefono(dto.getTelefono());
        }
        if (dto.getPassword() != null) {
            usuario.setPassword(dto.getPassword());
        }
        usuario.setActivo(dto.isActivo());

        return administradorRepository.save(admin);
    }

}
