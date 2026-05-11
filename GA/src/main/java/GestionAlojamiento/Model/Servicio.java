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
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tiene_cocina", nullable = false)
    private boolean tiene_cocina;

    @Column(name = "tiene_lavarropa", nullable = false)
    private boolean tiene_lavarropa;

    @Column(name = "tiene_wifi", nullable = false)
    private boolean tiene_wifi;

    @Column(name = "tiene_estacionamiento", nullable = false)
    private boolean tiene_estacionamiento;

}
