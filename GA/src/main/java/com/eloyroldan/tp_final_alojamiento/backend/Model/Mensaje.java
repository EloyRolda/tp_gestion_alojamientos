/// Mensaje interno de una reserva. Un mensaje pertenece a una reserva y tiene un
/// autor (cliente o anfitrión). No se agrega colección inversa en Reserva ni en Usuario.
package com.eloyroldan.tp_final_alojamiento.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//JPA
@Entity
@Table(name = "mensaje", indexes = @Index(name = "idx_mensaje_reserva", columnList = "id_reserva, fecha_envio"))
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autor")
    private Usuario autor;

    @Column(name = "texto", length = 1000, nullable = false)
    private String texto;

    @CreationTimestamp
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "leido", nullable = false)
    private boolean leido = false;
}
