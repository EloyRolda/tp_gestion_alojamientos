package GestionAlojamiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "id_remitente", nullable = false)
    private Usuario remitente;

    @NotBlank
    @Size(max = 2000)
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "leido", nullable = false)
    private Boolean leido = false;
}
