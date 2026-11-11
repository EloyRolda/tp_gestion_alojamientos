import { Pago } from './pago.model';

/// Espejo de ESTADORESERVA (backend).
export type EstadoReserva = 'SOLICITADA' | 'CONFIRMADA' | 'FINALIZADA' | 'CANCELADA';

/// Espejo de AlojamientoResumenDTO (version liviana embebida en Reserva/Review).
export interface AlojamientoResumen {
  idAlojamiento: number;
  titulo: string;
  precioNoche: number;
}

/// Espejo de UsuarioResumenDTO.
export interface UsuarioResumen {
  id: number;
  nombre: string;
  apellido: string;
}

/// Espejo de ReservaResponseDTO. "precioTotal" puede venir null (columna
/// GENERATED de MariaDB que Hibernate no refresca en memoria tras el insert):
/// nunca confiar en este campo para mostrar el
/// total, se recalcula en el cliente con calcularNoches/calcularTotal
/// (reserva.util.ts), igual que hace el propio backend (PagoService.calcularMontoBase).
export interface Reserva {
  idReserva: number;
  fechaInicio: string;
  fechaFin: string;
  precioNochePactado: number;
  precioTotal: number | null;
  estadoReserva: EstadoReserva;
  alojamiento: AlojamientoResumen;
  cliente: UsuarioResumen;
  tieneReview: boolean;
  pago: Pago | null;
}

/// Espejo de ReservaRegistroDTO.
export interface ReservaRegistro {
  fechaInicio: string;
  fechaFin: string;
  idAlojamiento: number;
  idCliente?: number | null;
}

/// Espejo de ReservaModificarDTO.
export interface ReservaModificar {
  idReserva: number;
  fechaInicio: string;
  fechaFin: string;
}
