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
@Table(name = "anfitrion")
public class Anfitrion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne //Relacion
    @MapsId //Que mape
    @JoinColumn(name = "id_usuario") // A quien hace referencia
    private Usuario usuario;
}
