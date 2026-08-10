package GestionAlojamiento.Repository;

import GestionAlojamiento.Model.Chat;
import GestionAlojamiento.Model.Enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByReservaId(Long reservaId);

    List<Chat> findByAnfitrionEmail(String emailAnfitrion);

    List<Chat> findByClienteEmail(String emailCliente);

    /// Para el scheduler: chats activos cuya reserva ya finalizo hace mas de 48hs.
    @Query("""
            SELECT c FROM Chat c
            WHERE c.activo = true AND c.reserva.estado = :estado
              AND c.reserva.fechaFinalizacion < :limite
            """)
    List<Chat> findActivosConEstadiaFinalizadaAntesDe(@Param("estado") EstadoReserva estado, @Param("limite") LocalDateTime limite);
}
