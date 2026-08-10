package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.CategoriaNotificacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaNotificacion categoria;

    @Column(name = "leida", nullable = false)
    private Boolean leida = false;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    /// Ruta/id opcional para que el frontend pueda redirigir al hacer click (ej: "/mis-reservas.html#5").
    @Column(name = "referencia", length = 150)
    private String referencia;
}
