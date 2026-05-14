package GestionAlojamiento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
//JPA
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metodo_pago", length = 100)
    private String metodoPago;

    @OneToOne //Indica que es una relacion 1 a 1
    @MapsId // Dice que es mapeable
    @JoinColumn(name = "id_usuario") // Indica a que columna se ingresa
    private Usuario usuario;
}
