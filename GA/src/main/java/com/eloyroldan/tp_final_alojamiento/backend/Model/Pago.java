package com.eloyroldan.tp_final_alojamiento.backend.Model;

import com.eloyroldan.tp_final_alojamiento.backend.Converter.EstadoPagoConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Converter.MotivoCancelacionConverter;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// El desglose financiero de una Reserva: cuanto paga el cliente, cuanto es fee,
/// cuanto termina recibiendo el anfitrion, y que paso en caso de cancelacion.
/// 1 a 1 con Reserva. Se crea en PENDIENTE apenas el anfitrion confirma la reserva.
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "reserva")
//JPA
@Entity
@Table(name = "pago", indexes = {
        @Index(name = "idx_pago_estado", columnList = "estado"),
        @Index(name = "idx_pago_fecha_creacion", columnList = "fecha_creacion"),
        @Index(name = "idx_pago_fecha_pago", columnList = "fecha_pago"),
        @Index(name = "idx_pago_motivo_cancelacion", columnList = "motivo_cancelacion")
})
/// Hibernate SI genera este CHECK al crear la tabla (a diferencia de un @Table
/// "indexes", que ademas lo puede agregar a una tabla ya existente con
/// ddl-auto=update, este constraint solo se aplica al CREAR la tabla desde cero
/// — si ya la tenes creada, hace falta correrlo a mano una vez, ver
/// db/completar_indices_y_checks.sql).
@Check(constraints = "monto_base > 0")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    /// Lo que el anfitrion definio que quiere recibir neto.
    @NotNull
    @Column(name = "monto_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoBase;

    @NotNull
    @Column(name = "comision_plataforma", nullable = false, precision = 10, scale = 2)
    private BigDecimal comisionPlataforma;

    /// Estimada al crear el pago (la real la fija MercadoPago segun el medio de pago
    /// que use el cliente); ver PagoService para el criterio de calculo.
    @NotNull
    @Column(name = "comision_mercadopago", nullable = false, precision = 10, scale = 2)
    private BigDecimal comisionMercadopago;

    /// montoBase + comisionPlataforma + comisionMercadopago: lo que efectivamente paga el cliente.
    @NotNull
    @Column(name = "monto_total_cliente", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotalCliente;

    @NotNull
    @Builder.Default
    @Convert(converter = EstadoPagoConverter.class)
    @Column(name = "estado", nullable = false)
    private ESTADOPAGO estado = ESTADOPAGO.PENDIENTE;

    /// Id del pago del lado de MercadoPago, una vez creado el checkout.
    @Column(name = "id_pago_externo", length = 100)
    private String idPagoExterno;

    /// URL de Checkout Pro para que el cliente pague (se re-genera si se vuelve a pedir).
    @Column(name = "url_checkout", length = 500)
    private String urlCheckout;

    @NotNull
    @Column(name = "fecha_limite_pago", nullable = false)
    private LocalDateTime fechaLimitePago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    /// Id del Payout (transferencia al anfitrion) del lado de MercadoPago.
    @Column(name = "id_transferencia_externa", length = 100)
    private String idTransferenciaExterna;

    @Column(name = "fecha_transferencia")
    private LocalDateTime fechaTransferencia;

    @Convert(converter = MotivoCancelacionConverter.class)
    @Column(name = "motivo_cancelacion")
    private MOTIVOCANCELACION motivoCancelacion;

    @Column(name = "monto_reembolsado_cliente", precision = 10, scale = 2)
    private BigDecimal montoReembolsadoCliente;

    @Column(name = "monto_retenido_anfitrion", precision = 10, scale = 2)
    private BigDecimal montoRetenidoAnfitrion;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_pago_reserva"))
    private Reserva reserva;
}
