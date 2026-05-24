package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
//JPA
@Entity
@Table(name = "hotel")

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "Valor minimo de 0 estrellas")
    @Max(value = 5, message = "Valor maximo de 5 estrellas")
    @Column(name = "estrellas", nullable = false)
    private Integer estrellas;

    @Column(name = "incluye_desayuno", nullable = false)
    private boolean incluyeDesayuno;
    @Column(name = "servicio_limpieza", nullable = false)
    private boolean servicioLimpieza;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;
}
