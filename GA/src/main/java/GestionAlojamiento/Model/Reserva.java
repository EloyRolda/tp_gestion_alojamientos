package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.EstadoReserva;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Lombok
@Data
@NoArgsConstructor
//JPA
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @DecimalMin(value = "0.0", inclusive = true, message = "Valor invalido, el precio no puede ser menor a cero")
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas;

    /// Mensaje breve del cliente al anfitrion explicando por que quiere reservar (parte de la SOLICITUD).
    @Size(max = 1000, message = "El mensaje es muy largo")
    @Column(name = "mensaje_solicitud", columnDefinition = "TEXT")
    private String mensajeSolicitud;

    /// Motivo opcional que carga el anfitrion al rechazar la solicitud.
    @Size(max = 500)
    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReserva estado = EstadoReserva.SOLICITADA;

    /// Momento en que el anfitrion acepto la solicitud (arranca la ventana de pago).
    @Column(name = "fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

    /// Limite (fechaAceptacion + 48hs) para pagar. Lo vencen los @Scheduled de ReservaService.
    @Column(name = "fecha_limite_pago")
    private LocalDateTime fechaLimitePago;

    /// Momento en que se cerro la estadia (FINALIZADA), usado para la ventana de resena/cierre de chat.
    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_alojamiento", nullable = false)
    private Alojamiento alojamiento;

}
