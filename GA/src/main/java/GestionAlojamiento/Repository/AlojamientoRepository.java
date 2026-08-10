package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import GestionAlojamiento.Model.Enums.TipoAlojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    List<Alojamiento> findByAnfitrionId(Long anfitrionId);

    List<Alojamiento> findByAnfitrionEmail(String emailAnfitrion);

    /// Busqueda con filtros opcionales para el listado publico (todos con activo=true y publicado=true).
    /// Los parametros en null se ignoran (patron JPQL "AND (:x IS NULL OR campo = :x)"). Al ser
    /// "tipo" una columna comun (ya no una jerarquia de herencia), el filtro es una simple
    /// comparacion de enum: no hace falta resolver el tipo en memoria como antes.
    @Query("""
            SELECT a FROM Alojamiento a
            WHERE a.activo = true AND a.publicado = true
              AND (:ciudad IS NULL OR LOWER(a.direccion.ciudad) = LOWER(:ciudad))
              AND (:capacidadMinima IS NULL OR a.capacidad >= :capacidadMinima)
              AND (:precioMin IS NULL OR a.precioNoche >= :precioMin)
              AND (:precioMax IS NULL OR a.precioNoche <= :precioMax)
              AND (:tipo IS NULL OR a.tipo = :tipo)
            """)
    List<Alojamiento> buscar(
            @Param("ciudad") String ciudad,
            @Param("capacidadMinima") Integer capacidadMinima,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("tipo") TipoAlojamiento tipo
    );
}
