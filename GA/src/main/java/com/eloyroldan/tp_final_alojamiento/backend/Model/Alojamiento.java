package com.eloyroldan.tp_final_alojamiento.backend.Model;

import com.eloyroldan.tp_final_alojamiento.backend.Converter.MotivoReporteConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOREPORTE;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// Alojamiento publicado por un anfitrion. No tiene tablas hijas por tipo
/// (Casa/Depto/Hotel): el tipo es una referencia al catalogo TipoAlojamiento
/// y las comodidades especificas se resuelven via Servicio + ServicioxAlojamiento.
/// Las relaciones inversas (reservas, galeria, servicios) se consultan desde
/// sus propios repositorios, no se mantienen como colecciones aca.
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "idAlojamiento")
@ToString(exclude = {"anfitrion", "direccion", "tipoAlojamiento"})
//JPA
@Entity
@Table(name = "alojamiento", indexes = {
        @Index(name = "idx_alojamiento_anfitrion", columnList = "id_anfitrion")
})
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alojamiento")
    private Long idAlojamiento;

    @NotNull
    @Builder.Default
    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    @NotNull
    @Positive
    @Builder.Default
    @Column(name = "cant_ambientes", nullable = false)
    private Integer cantAmbientes = 1;

    @NotNull
    @Positive
    @Builder.Default
    @Column(name = "cant_banios", nullable = false)
    private Integer cantBanios = 1;

    @NotNull
    @Positive
    @Builder.Default
    @Column(name = "cant_habitaciones", nullable = false)
    private Integer cantHabitaciones = 1;

    @NotNull
    @Positive
    @Builder.Default
    @Column(name = "capacidad", nullable = false)
    private Integer capacidad = 1;

    @Builder.Default
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion = "Excelente alojamiento";

    @NotNull
    @Positive
    @Column(name = "precio_noche", nullable = false, precision = 10, scale = 4)
    private BigDecimal precioNoche;

    @NotBlank
    @Size(max = 150)
    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anfitrion", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alojamiento_anfitrion"))
    private Usuario anfitrion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alojamiento_direccion"))
    private Direccion direccion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_alojamiento", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alojamiento_tipo"))
    private TipoAlojamiento tipoAlojamiento;

    /// Motivo por el que la moderación ocultó la publicación, junto con el detalle
    /// opcional y la fecha. Hibernate crea estas columnas nullable por defecto.
    @Convert(converter = MotivoReporteConverter.class)
    @Column(name = "motivo_ocultamiento")
    private MOTIVOREPORTE motivoOcultamiento;

    @Size(max = 500)
    @Column(name = "detalle_ocultamiento", length = 500)
    private String detalleOcultamiento;

    @Column(name = "fecha_ocultamiento")
    private LocalDateTime fechaOcultamiento;
}
