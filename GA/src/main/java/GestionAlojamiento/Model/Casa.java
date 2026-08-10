package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CASA")
@Table(name = "casa")
public class Casa extends Alojamiento {

    @Column(name = "tiene_patio", nullable = false)
    private boolean tienePatio;

    @Column(name = "tiene_pileta", nullable = false)
    private boolean tienePileta;

    @Column(name = "tiene_parrilla", nullable = false)
    private boolean tieneParrilla;
}
