package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Lombok

@Data
@NoArgsConstructor
@AllArgsConstructor
//JPA
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Email(message = "El formato de correo es invalido")
    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    @Column(name = "telefono", length = 30)
    private String telefono;
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fecha_registro;
    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoUsuario tipoUsuario;

}

