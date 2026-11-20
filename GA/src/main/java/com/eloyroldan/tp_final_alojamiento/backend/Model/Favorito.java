/// Favorito: un alojamiento que un cliente guardo para volver a el antes de reservar.
/// La restriccion de unicidad usuario + alojamiento evita duplicados: guardar dos
/// veces el mismo alojamiento no crea dos filas.
package com.eloyroldan.tp_final_alojamiento.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idFavorito")
@ToString(exclude = {"usuario", "alojamiento"})
//JPA
@Entity
@Table(name = "favorito",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_favorito_usuario_alojamiento",
                columnNames = {"id_usuario", "id_alojamiento"}),
        indexes = @Index(name = "idx_favorito_usuario", columnList = "id_usuario"))
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long idFavorito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alojamiento", nullable = false)
    private Alojamiento alojamiento;

    /// Fecha en la que el cliente guardo el alojamiento (ordena el listado).
    @CreationTimestamp
    @Column(name = "fecha_alta", updatable = false)
    private LocalDateTime fechaAlta;
}
