package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
//JPA
@Entity
@Table(name = "direccion")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long id;

    @Column(name = "pais", length = 100, nullable = false)
    private String pais;
    @Column(name = "provincia", length = 100)
    private String provincia;
    @Column(name = "codigo_postal", length = 100, nullable = false)
    private String codigoPostal;
    @Column(name = "ciudad", length = 100, nullable = false)
    private String ciudad;
    @Column(name = "calle", length = 30, nullable = false)
    private String calle;
    @Column(name = "altura", nullable = false)
    private Integer altura;

}
