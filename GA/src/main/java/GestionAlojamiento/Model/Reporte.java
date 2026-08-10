package GestionAlojamiento.Model;

import GestionAlojamiento.Model.Enums.EstadoReporte;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/// Reporte que hace un usuario sobre otro usuario o sobre un alojamiento.
/// Siempre debe tener EXACTAMENTE uno de los dos targets cargado (se valida en el Service).
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_reportante", nullable = false)
    private Usuario reportante;

    @ManyToOne
    @JoinColumn(name = "id_usuario_reportado")
    private Usuario usuarioReportado;

    @ManyToOne
    @JoinColumn(name = "id_alojamiento_reportado")
    private Alojamiento alojamientoReportado;

    @NotBlank
    @Size(max = 2000)
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReporte estado = EstadoReporte.PENDIENTE;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
}
