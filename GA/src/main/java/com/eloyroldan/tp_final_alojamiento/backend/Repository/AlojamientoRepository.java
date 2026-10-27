package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    List<Alojamiento> findByAnfitrion_Id(Long idAnfitrion);

    List<Alojamiento> findByTipoAlojamiento_IdTipoAlojamiento(Long idTipoAlojamiento);

    /// Util para saber si una direccion compartida sigue en uso antes de tocarla/borrarla.
    List<Alojamiento> findByDireccion_IdDireccion(Long idDireccion);
    boolean existsByDireccion_IdDireccion(Long idDireccion);

    /// Bloqueo pesimista: usado al crear/reprogramar una Reserva, para que dos
    /// solicitudes simultaneas sobre el MISMO alojamiento no puedan pasar juntas
    /// el chequeo de solapamiento de fechas (condicion de carrera clasica:
    /// las dos leen "no hay solapamiento" antes de que ninguna guarde nada).
    /// Requiere correr dentro de una transaccion activa (@Transactional) — el
    /// lock se mantiene hasta que esa transaccion termina.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Alojamiento a where a.idAlojamiento = :id")
    Optional<Alojamiento> findByIdConLock(@Param("id") Long id);

    //================================ OPTIMIZACION N+1 ================================
    // AlojamientoResponseDTO.desde() necesita anfitrion + direccion + tipoAlojamiento.
    // Sin JOIN FETCH, listar N alojamientos dispara 1 + 3N consultas (una por cada
    // relacion LAZY de cada fila). Con JOIN FETCH queda en 1 sola consulta siempre,
    // sin importar cuantos alojamientos haya.

    @Query("""
            select a from Alojamiento a
            join fetch a.anfitrion
            join fetch a.direccion
            join fetch a.tipoAlojamiento
            where a.activo = true
            order by a.titulo asc
            """)
    List<Alojamiento> findActivosConDetalle();

    @Query("""
            select a from Alojamiento a
            join fetch a.anfitrion
            join fetch a.direccion
            join fetch a.tipoAlojamiento
            where a.anfitrion.id = :idAnfitrion
            """)
    List<Alojamiento> findByAnfitrionIdConDetalle(@Param("idAnfitrion") Long idAnfitrion);
}
