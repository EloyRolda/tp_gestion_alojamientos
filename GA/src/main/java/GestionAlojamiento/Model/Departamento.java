package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
//JPA
@Entity
@Table(name = "departamento")
public class Departamento {
    @Id
    @Column(name = "id_alojamiento")
    private Long id;

    //A fines practicos no contemplamos plantas negativas.
    @Column(name = "piso", nullable = false)
    private Integer piso;

    @Column(name = "tiene_ascensor", nullable = false)
    private boolean tieneAscensor;

    @Column(name = "expensas_incluidas", nullable = false)
    private boolean expensasIncluidas;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;
}
