package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/// Persistencia de los alojamientos favoritos. Los listados de ids salen de aca
/// (una sola consulta) y el Service dueño de Alojamiento arma los DTOs.
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    boolean existsByUsuario_IdAndAlojamiento_IdAlojamiento(Long idUsuario, Long idAlojamiento);

    /// Ids de los alojamientos favoritos del usuario, solo los activos, del mas
    /// reciente al mas viejo (mismo orden que usa el listado de la pantalla).
    @Query("""
            select f.alojamiento.idAlojamiento from Favorito f
            where f.usuario.id = :idUsuario
              and f.alojamiento.activo = true
            order by f.fechaAlta desc, f.idFavorito desc
            """)
    List<Long> idsAlojamientosFavoritos(@Param("idUsuario") Long idUsuario);

    void deleteByUsuario_IdAndAlojamiento_IdAlojamiento(Long idUsuario, Long idAlojamiento);

    /// Usado por AlojamientoService antes de borrar un alojamiento (la FK no tiene cascada).
    void deleteByAlojamiento_IdAlojamiento(Long idAlojamiento);
}
