package GestionAlojamiento.Service;

import GestionAlojamiento.DTO.AdministradorModificarDTO;
import GestionAlojamiento.DTO.AdministradorRegistroDTO;
import GestionAlojamiento.Exception.IdNoEncontradoException;
import GestionAlojamiento.Exception.RecursoDuplicadoException;
import GestionAlojamiento.Model.Administrador;
import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import GestionAlojamiento.Repository.AdministradorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public Administrador obtenerPorId(Long id) {
        return administradorRepository.findById(id).orElseThrow(() -> new IdNoEncontradoException("Error, id no se encuentra en la base de datos"));
    }


    //---------------------------------------- CREAR ----------------------------------------
    @Transactional
    public Administrador crear(AdministradorRegistroDTO administradorRegistroDTO) {

        if (administradorRepository.existsByMatricula(administradorRegistroDTO.getMatricula())) {
            throw new RecursoDuplicadoException("La matrícula ya existe.");
        }
        Usuario usuario = mapearUsuario(administradorRegistroDTO);

        usuarioService.crear(usuario);

        Administrador administrador = new Administrador();
        administrador.setUsuario(usuario);
        administrador.setMatricula(administradorRegistroDTO.getMatricula());

        return administradorRepository.save(administrador);
    }

    //---------------------------------------- BORRAR ----------------------------------------
    @Transactional
    public void borrarPorId(Long idAdmin) {
        if (!administradorRepository.existsById(idAdmin)) {
            throw new IdNoEncontradoException("id invalido, no se encuentra en la base de datos.");
        }
        administradorRepository.deleteById(idAdmin);
    }

    //---------------------------------------- MODIFICAR ----------------------------------------
    @Transactional
    public Administrador actualizar(AdministradorModificarDTO dto) {

        Administrador admin = administradorRepository.findById(dto.getId()).orElseThrow(() -> new IdNoEncontradoException("ID inválido"));

        if (dto.getMatricula() != null) {
            boolean existe = administradorRepository.existsByMatriculaAndIdNot(dto.getMatricula(), dto.getId());

            if (existe) {
                throw new RecursoDuplicadoException("La matrícula pertenece a otro administrador.");
            }

            admin.setMatricula(dto.getMatricula());
        }

        admin.setUsuario(usuarioService.modificarObjeto(admin.getUsuario(), mapearUsuario(dto)));

        return administradorRepository.save(admin);
    }
//---------------------------------------- MAPEOS DTO [PRIVADOS] ----------------------------------------

    /// Mapea el DTO de MODIFICAR a un USUARIO y lo RETORNA
    private Usuario mapearUsuario(AdministradorRegistroDTO administradorRegistroDTO) {
        Usuario usuario = new Usuario(
                null,
                administradorRegistroDTO.getNombre(),
                administradorRegistroDTO.getEmail(),
                administradorRegistroDTO.getPassword(),
                administradorRegistroDTO.getTelefono(),
                LocalDateTime.now(),
                true,
                TipoUsuario.ADMINISTRADOR
        );
        return usuario;
    }

    /// Mapea el DTO de MODIFICAR a un USUARIO y lo RETORNA
    private Usuario mapearUsuario(AdministradorModificarDTO administradorModificarDTO) {
        Usuario usuario = new Usuario(
                administradorModificarDTO.getId(),
                administradorModificarDTO.getNombre(),
                administradorModificarDTO.getEmail(),
                administradorModificarDTO.getPassword(),
                administradorModificarDTO.getTelefono(),
                null,   //Fecha registro
                administradorModificarDTO.getActivo(),
                TipoUsuario.ADMINISTRADOR
        );
        return usuario;
    }
}
