package GestionAlojamiento.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Lombok
@Data
@NoArgsConstructor
//JPA
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long id;

    @Max(value = 5, message = "El valor maximo es 5")
    @Min(value = 0, message = "El valor minimo es 0")
    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @Size(max = 2000, message = "El comentario es muy largo")
    @Column(name = "comentario", nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha; // No deberia modificarse pasadas 48hs, ver ReviewService.

    /// La reseña es de UNA estadia puntual, no del alojamiento en general:
    /// por eso se liga 1 a 1 con la Reserva (FINALIZADA) que la origino, y no
    /// hay unicidad por cliente+alojamiento (un cliente puede reseñar cada
    /// estadia distinta que haya tenido en el mismo lugar).
    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = false, unique = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_alojamiento", nullable = false)
    private Alojamiento alojamiento;

}
