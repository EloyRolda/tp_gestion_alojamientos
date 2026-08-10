package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/// Gracias a la herencia JOINED, este repositorio devuelve alojamientos
/// polimorficos (mezcla de Casa/Departamento/Hotel) sin necesidad de tocar
/// las tablas de cada subtipo: es la base del listado/busqueda estilo Airbnb.
public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    List<Alojamiento> findByAnfitrionId(Long anfitrionId);

    List<Alojamiento> findByAnfitrionEmail(String emailAnfitrion);

    /// Busqueda con filtros opcionales para el listado publico (todos con activo=true y publicado=true).
    /// Los parametros en null se ignoran (patron JPQL "AND (:x IS NULL OR campo = :x)").
    /// El filtro por tipo (CASA/DEPARTAMENTO/HOTEL) se resuelve en AlojamientoService
    /// sobre esta lista, evitando pasarle un Class<?> como parametro JPQL (TYPE()
    /// con parametro nulo es fragil segun el proveedor de JPA).
    @Query("""
            SELECT a FROM Alojamiento a
            WHERE a.activo = true AND a.publicado = true
              AND (:ciudad IS NULL OR LOWER(a.direccion.ciudad) = LOWER(:ciudad))
              AND (:capacidadMinima IS NULL OR a.capacidad >= :capacidadMinima)
              AND (:precioMin IS NULL OR a.precioNoche >= :precioMin)
              AND (:precioMax IS NULL OR a.precioNoche <= :precioMax)
            """)
    List<Alojamiento> buscar(
            @Param("ciudad") String ciudad,
            @Param("capacidadMinima") Integer capacidadMinima,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}
