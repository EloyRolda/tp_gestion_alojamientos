package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Se abre UNA vez que la reserva queda PAGADA (nunca antes) y UNO por reserva:
/// si el mismo cliente reserva el mismo alojamiento varias veces, cada reserva
/// tiene su propio chat, para que el admin pueda auditar conversaciones puntuales.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_anfitrion", nullable = false)
    private Usuario anfitrion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    /// Se cierra automaticamente 48hs despues de finalizada la estadia (ver Scheduler).
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
