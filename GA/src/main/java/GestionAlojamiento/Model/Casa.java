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
@Table(name = "casa")

public class Casa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tiene_patio", nullable = false)
    private boolean tienePatio;
    @Column(name = "tiene_pileta", nullable = false)
    private boolean tienePileta;
    @Column(name = "tiene_parrilla", nullable = false)
    private boolean tieneParrilla;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;

}
