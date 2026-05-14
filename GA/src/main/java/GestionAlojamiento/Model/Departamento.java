package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
//
@Entity
@Table(name = "departamento")
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    @Min(value = 0, "No se puede registrar un piso negativo")
    **Nota: Esta estoy en duda dado que puede haber pb y pisos negativos dependiendo el complejo;
    lo dejo comentado para tener constancia** -Eloy
    */

    @Column(name = "piso", nullable = false)
    private Integer piso;

    @Column(name = "tiene_ascensor", nullable = false)
    private boolean tieneAscensor;

    @Column(name = "expensas_incluidas", nullable = false)
    private boolean expensasIncluidas;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;
}
