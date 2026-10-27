package com.eloyroldan.tp_final_alojamiento.backend.Model;

import com.eloyroldan.tp_final_alojamiento.backend.Converter.EstadoReservaConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADORESERVA;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/// Reserva de un cliente sobre un alojamiento para un rango de fechas.
/// precio_total es una columna GENERATED ALWAYS de MySQL (precio_noche_pactado * noches),
/// por eso se mapea como insertable=false, updatable=false: la calcula la base de datos.
/// El estado del pago YA NO vive aca (ver Model.Pago) — evita tener dos fuentes de
/// verdad para lo mismo. Para saber el estado de pago de una reserva, se consulta
/// PagoRepository.findByReserva_IdReserva.
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"alojamiento", "cliente", "review"})
//JPA
@Entity
@Table(name = "reserva", indexes = {
        @Index(name = "idx_reserva_estado", columnList = "estado_reserva"),
        @Index(name = "idx_reserva_alojamiento_estado", columnList = "id_alojamiento, estado_reserva")
})
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    /// Precio por noche vigente al momento de reservar (foto del precio del alojamiento).
    @NotNull
    @Positive
    @Column(name = "precio_noche_pactado", nullable = false, precision = 10, scale = 4)
    private BigDecimal precioNochePactado;

    /// Calculado por la base de datos (columna GENERATED). No se debe setear a mano.
    @Column(name = "precio_total", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal precioTotal;

    @NotNull
    @Builder.Default
    @Convert(converter = EstadoReservaConverter.class)
    @Column(name = "estado_reserva", nullable = false)
    private ESTADORESERVA estadoReserva = ESTADORESERVA.SOLICITADA;

    /// Cuando el anfitrion confirmo (arranca de aca el plazo de 48hs para pagar).
    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alojamiento", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reserva_alojamiento"))
    private Alojamiento alojamiento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reserva_cliente"))
    private Usuario cliente;

    /// Una reserva finalizada puede tener a lo sumo una review (1 a 1).
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;
}
