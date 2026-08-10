package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// La contracara de Review: la reseña breve que el ANFITRION deja sobre el
/// HUESPED al finalizar la estadia (una por reserva, igual que la del cliente).
@Data
@NoArgsConstructor
@Entity
@Table(name = "review_huesped")
public class ReviewHuesped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review_huesped")
    private Long id;

    @NotBlank
    @Size(max = 800)
    @Column(name = "comentario", nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_anfitrion", nullable = false)
    private Usuario anfitrion;

    @ManyToOne
    @JoinColumn(name = "id_huesped", nullable = false)
    private Usuario huesped;
}
