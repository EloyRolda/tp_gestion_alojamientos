package com.eloyroldan.tp_final_alojamiento.backend.DTO.Response;

import com.eloyroldan.tp_final_alojamiento.backend.Enum.ESTADOPAGO;
import com.eloyroldan.tp_final_alojamiento.backend.Enum.MOTIVOCANCELACION;
import com.eloyroldan.tp_final_alojamiento.backend.Model.Pago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// Desglose financiero completo de una Reserva (ver Model.Pago).
//Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {

    private Long idPago;
    private BigDecimal montoBase;
    private BigDecimal comisionPlataforma;
    private BigDecimal comisionMercadopago;
    private BigDecimal montoTotalCliente;
    private ESTADOPAGO estado;
    private String urlCheckout;
    private LocalDateTime fechaLimitePago;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaTransferencia;
    private MOTIVOCANCELACION motivoCancelacion;
    private BigDecimal montoReembolsadoCliente;
    private BigDecimal montoRetenidoAnfitrion;

    public static PagoResponseDTO desde(Pago p) {
        return new PagoResponseDTO(
                p.getIdPago(), p.getMontoBase(), p.getComisionPlataforma(), p.getComisionMercadopago(),
                p.getMontoTotalCliente(), p.getEstado(), p.getUrlCheckout(), p.getFechaLimitePago(), p.getFechaPago(),
                p.getFechaTransferencia(), p.getMotivoCancelacion(),
                p.getMontoReembolsadoCliente(), p.getMontoRetenidoAnfitrion());
    }
}
