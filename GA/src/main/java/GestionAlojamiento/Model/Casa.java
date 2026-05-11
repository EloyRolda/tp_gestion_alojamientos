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
    private boolean tiene_patio;
    @Column(name = "tiene_pileta", nullable = false)
    private boolean tiene_pileta;
    @Column(name = "tiene_parrilla", nullable = false)
    private boolean tiene_parrilla;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_alojamiento")
    private Alojamiento alojamiento;

}
