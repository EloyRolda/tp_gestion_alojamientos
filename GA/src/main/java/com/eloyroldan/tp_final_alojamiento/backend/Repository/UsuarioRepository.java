package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPOUSUARIO;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    boolean existsByUsuarioLogin(String usuarioLogin);

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefono(String telefono);
    Optional<Usuario> findByUsuarioLogin(String usuarioLogin);

    /// Permite resolver el login sin importar si el usuario escribio su email,
    /// su usuario_login o su telefono en el campo de inicio de sesion.
    Optional<Usuario> findByEmailOrUsuarioLoginOrTelefono(String email, String usuarioLogin, String telefono);

    List<Usuario> findAllByTipo(TIPOUSUARIO tipo);
    boolean existsByIdAndTipo(Long id, TIPOUSUARIO tipo);

    /// Proyeccion liviana usada por UsuarioService.nombresPorIds para la
    /// moderacion de reportes: id -> "Nombre Apellido". Una unica consulta
    /// para todo el listado de reportes.
    @Query("select u.id, concat(u.nombre, ' ', u.apellido) from Usuario u where u.id in :ids")
    List<Object[]> nombresPorIds(@Param("ids") List<Long> ids);
}
