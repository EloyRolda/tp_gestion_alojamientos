package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
//JPA
@Entity
@Table(name = "disponibilidad", uniqueConstraints = @UniqueConstraint(name = "uq_disp_fecha", columnNames = {"id_alojamiento", "fecha"}))
/*
En La base de datos-> CONSTRAINT uq_disp_fecha UNIQUE (id_alojamiento, fecha)
En la llamada @Table -> uniqueConstraints, para asegurarse que no inserten 2 veces o mas la misma fecha y el mismo alojamiento.
 */
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FutureOrPresent(message = "la fecha no puede ser anterior al dia de hoy")
    // FutureOrPresent verifica que sera hoy o despues de hoy.
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "disponible", nullable = false)
    private boolean disponible;


}
