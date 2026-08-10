package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("DEPARTAMENTO")
@Table(name = "departamento")
public class Departamento extends Alojamiento {

    //A fines practicos no contemplamos plantas negativas.
    @Column(name = "piso", nullable = false)
    private Integer piso;

    @Column(name = "tiene_ascensor", nullable = false)
    private boolean tieneAscensor;

    @Column(name = "expensas_incluidas", nullable = false)
    private boolean expensasIncluidas;
}
