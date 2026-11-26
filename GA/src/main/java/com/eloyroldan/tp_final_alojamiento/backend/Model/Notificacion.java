package com.eloyroldan.tp_final_alojamiento.backend.Model;

import com.eloyroldan.tp_final_alojamiento.backend.Converter.TipoNotificacionConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.TIPONOTIFICACION;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/// Aviso interno que se muestra en la campana del encabezado.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idNotificacion")
@ToString(exclude = "destinatario")
//JPA
@Entity
@Table(name = "notificacion", indexes = {
        @Index(name = "idx_notificacion_destinatario", columnList = "id_destinatario, leida")
})
public class Notificacion {

    //Lombok

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinatario", nullable = false)
    private Usuario destinatario;

    @Convert(converter = TipoNotificacionConverter.class)
    @Column(nullable = false, length = 50)
    private TIPONOTIFICACION tipo;

    @Column(nullable = false, length = 300)
    private String texto;

    @Column(nullable = false, length = 200)
    private String enlace;

    @Column(nullable = false)
    private boolean leida = false;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    //JPA
}
