package com.eloyroldan.tp_final_alojamiento.backend.Repository;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// Acceso a la tabla de avisos internos.
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findTop30ByDestinatario_IdOrderByFechaCreacionDescIdNotificacionDesc(Long idDestinatario);

    long countByDestinatario_IdAndLeidaFalse(Long idDestinatario);

    Optional<Notificacion> findByIdNotificacionAndDestinatario_Id(Long idNotificacion, Long idDestinatario);

    @Modifying
    @Query("update Notificacion n set n.leida = true where n.destinatario.id = :idDestinatario")
    int marcarTodasLeidas(@Param("idDestinatario") Long idDestinatario);
}
