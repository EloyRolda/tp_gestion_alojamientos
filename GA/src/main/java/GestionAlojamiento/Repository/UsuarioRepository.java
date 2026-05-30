package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Enums.TipoUsuario;
import GestionAlojamiento.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAllByTipoUsuario(TipoUsuario tipoUsuario);
    boolean existsByIdAndTipoUsuario(Long id, TipoUsuario tipoUsuario);
}