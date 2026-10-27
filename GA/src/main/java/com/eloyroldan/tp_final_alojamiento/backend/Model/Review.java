package com.eloyroldan.tp_final_alojamiento.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/// Reseña de un cliente sobre una reserva puntual ya finalizada (no sobre el
/// alojamiento en general). Un mismo cliente puede dejar una review por cada
/// reserva finalizada que haya tenido en un mismo alojamiento.
/// Es inmodificable una vez creada: solo se puede eliminar.
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idReview")
@ToString(exclude = "reserva")
//JPA
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long idReview;

    @NotNull
    @Min(value = 1, message = "El puntaje minimo es 1")
    @Max(value = 5, message = "El puntaje maximo es 5")
    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @NotBlank
    @Column(name = "comentario", columnDefinition = "TEXT", nullable = false)
    private String comentario;

    @CreationTimestamp
    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_review_reserva"))
    private Reserva reserva;
}
