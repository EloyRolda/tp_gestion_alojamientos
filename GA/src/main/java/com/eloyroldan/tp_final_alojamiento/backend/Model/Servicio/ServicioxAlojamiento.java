package com.eloyroldan.tp_final_alojamiento.backend.Model.Servicio;

import com.eloyroldan.tp_final_alojamiento.backend.Model.Alojamiento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/// Tabla puente muchos-a-muchos entre Alojamiento y Servicio.
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"alojamiento", "servicio"})
//JPA
@Entity
@Table(name = "servicio_x_alojamiento",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_sxa_alojamiento_servicio",
                columnNames = {"id_alojamiento", "id_servicio"}
        ))
public class ServicioxAlojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio_x_alojamiento")
    private Long idServicioXAlojamiento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alojamiento", nullable = false,
            foreignKey = @ForeignKey(name = "fk_sxa_alojamiento"))
    private Alojamiento alojamiento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false,
            foreignKey = @ForeignKey(name = "fk_sxa_servicio"))
    private Servicio servicio;
}
