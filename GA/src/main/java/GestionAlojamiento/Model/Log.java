package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// Auditoria de acciones relevantes del sistema, visible solo para el admin.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long id;

    /// Puede ser null (ej: intento de login fallido con un email que no existe).
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    /// 'CREAR', 'MODIFICAR', 'BORRAR', 'LOGIN', 'LOGIN_FALLIDO', 'PAGO', etc.
    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    /// 'ALOJAMIENTO', 'RESERVA', 'USUARIO', 'REVIEW', 'REPORTE', etc.
    @Column(name = "entidad", nullable = false, length = 50)
    private String entidad;

    @Column(name = "id_entidad")
    private Long idEntidad;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
}
