import { MotivoCancelacion } from './pago.model';

/// Espejo de ResumenClienteDTO.
export interface ResumenCliente {
  cantidadReservas: number;
  montoTotalGastado: number;
  montoPromedioPorEstadia: number | null;
}

/// Espejo de ResumenAlojamientoAnfitrionDTO. Una fila por cada alojamiento propio.
export interface ResumenAlojamientoAnfitrion {
  idAlojamiento: number;
  titulo: string;
  ingresosTotales: number;
  cantidadReservas: number;
  ratingPromedio: number | null;
}

/// Espejo de CobroPendienteDTO. Reservas ya pagadas por el cliente que
/// todavia esperan liquidarse (se liquidan recien al FINALIZAR).
export interface CobroPendiente {
  idReserva: number;
  tituloAlojamiento: string;
  montoAcobrar: number;
  fechaFinEstadia: string;
}

/// Espejo de FlujoDineroDTO. Panel financiero del admin para un rango de
/// fechas (o todo el historico si desde/hasta vienen null).
export interface FlujoDinero {
  totalFacturado: number;
  totalComisionPlataforma: number;
  totalComisionMercadopago: number;
  totalLiquidadoAnfitriones: number;
  totalReembolsadoClientes: number;
  cantidadPagos: number;
}

/// Espejo de CancelacionPorMotivoDTO.
export interface CancelacionPorMotivo {
  motivo: MotivoCancelacion;
  cantidad: number;
}

/// Espejo de IngresoMensualDTO.
export interface IngresoMensual {
  anio: number;
  mes: number;
  totalFacturado: number;
}

/// Espejo de AlojamientoRankingDTO. "valor" cambia de significado segun "criterio".
export interface AlojamientoRanking {
  idAlojamiento: number;
  titulo: string;
  valor: number;
  criterio: string;
}
