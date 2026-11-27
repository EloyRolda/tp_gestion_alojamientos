/// Acceso a datos de los mensajes por reserva. La conversación usa join fetch
/// para traer al autor en una sola consulta y evitar N+1 al construir los DTOs.
package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("""
            select m
            from Mensaje m
            join fetch m.autor
            where m.reserva.idReserva = :idReserva
            order by m.fechaEnvio asc, m.idMensaje asc
            """)
    List<Mensaje> findConversacion(@Param("idReserva") Long idReserva);

    @Modifying
    @Query("""
            update Mensaje m
            set m.leido = true
            where m.reserva.idReserva = :idReserva
              and m.autor.id <> :idUsuario
            """)
    int marcarLeidosDeOtroAutor(@Param("idReserva") Long idReserva,
                                @Param("idUsuario") Long idUsuario);

    void deleteByReserva_IdReserva(Long idReserva);

    @Modifying
    @Query("""
            delete from Mensaje m
            where m.reserva.idReserva in (
                select r.idReserva
                from Reserva r
                where r.alojamiento.idAlojamiento = :idAlojamiento
            )
            """)
    int borrarPorAlojamiento(@Param("idAlojamiento") Long idAlojamiento);
}
