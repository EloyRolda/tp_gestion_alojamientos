package GestionAlojamiento.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
//JPA
@Entity
@Table(name = "review", uniqueConstraints = {@UniqueConstraint(name = "uq_review_cliente_aloj", columnNames = {"id_cliente", "id_alojamiento"})})// Los usuarios/cliente pueden dejar solamente una review por alojamiento
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

    @Column(name = "fecha", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;//No deberia poder modificarse en el service.

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;

}
