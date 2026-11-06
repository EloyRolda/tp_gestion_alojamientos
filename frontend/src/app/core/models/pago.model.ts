/// Espejo de ESTADOPAGO (backend).
export type EstadoPago = 'PENDIENTE' | 'PAGADO' | 'VENCIDO' | 'REEMBOLSADO_TOTAL' | 'REEMBOLSADO_PARCIAL';

/// Espejo de MOTIVOCANCELACION (backend).
export type MotivoCancelacion = 'ANFITRION' | 'CLIENTE_ANTICIPADA' | 'CLIENTE_TARDIA' | 'VENCIMIENTO_PAGO' | 'ADMIN';

/// Espejo de PagoResponseDTO. Desglose financiero completo de una Reserva.
export interface Pago {
  idPago: number;
  montoBase: number;
  comisionPlataforma: number;
  comisionMercadopago: number;
  montoTotalCliente: number;
  estado: EstadoPago;
  urlCheckout: string | null;
  fechaLimitePago: string | null;
  fechaPago: string | null;
  fechaTransferencia: string | null;
  motivoCancelacion: MotivoCancelacion | null;
  montoReembolsadoCliente: number | null;
  montoRetenidoAnfitrion: number | null;
}
