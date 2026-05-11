package GestionAlojamiento.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Esto es para que lombok asigne los gets y sets automaticamente
@NoArgsConstructor //Genera constructor "vacio"
@AllArgsConstructor //Genera constructor con todos los atributos pasables
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "pais", length = 100, nullable = false)
    private String pais;
    @Column(name = "provincia", length = 100)
    private String provincia;
    @Column(name = "codigo_postal", length = 100, nullable = false)
    private String codigo_postal;
    @Column(name = "ciudad", length = 100, nullable = false)
    private String ciudad;
    @Column(name = "calle", length = 30, nullable = false)
    private String calle;
    @Column(name = "altura", nullable = false)
    int altura;

}
