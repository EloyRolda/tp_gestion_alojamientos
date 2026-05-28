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
    @Column(name = "id_servicio")
    private Long id;

    @Column(name = "tiene_cocina", nullable = false)
    private Boolean tieneCocina;

    @Column(name = "tiene_lavarropa", nullable = false)
    private Boolean tieneLavarropa;

    @Column(name = "tiene_wifi", nullable = false)
    private Boolean tieneWifi;

    @Column(name = "tiene_estacionamiento", nullable = false)
    private Boolean tieneEstacionamiento;

}
