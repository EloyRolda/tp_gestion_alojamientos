package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /// Verifica si existe el correco en la db
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

}
