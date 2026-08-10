package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// Traza el pago de Mercado Pago asociado a una reserva. Hoy trabaja en modo
/// SIMULADO (ver PagoService), pero el modelo ya queda listo para el dia que
/// se conecten las credenciales reales de la API de pagos: solo cambia la
/// implementacion interna de PagoService, no esta entidad ni el resto del flujo.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    /// Id de preferencia/checkout que devolveria la API de Mercado Pago.
    @Column(name = "preference_id", length = 100)
    private String preferenceId;

    /// Id del pago ya acreditado que devolveria la API de Mercado Pago (webhook).
    @Column(name = "payment_id", length = 100)
    private String paymentId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_acreditacion")
    private LocalDateTime fechaAcreditacion;
}
