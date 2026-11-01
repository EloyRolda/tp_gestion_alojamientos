/// Espejo de ReviewResponseDTO (aplanado: no anida Reserva completa).
export interface Review {
  idReview: number;
  puntuacion: number;
  comentario: string;
  fecha: string;
  idReserva: number;
  idAlojamiento: number;
  tituloAlojamiento: string;
  idCliente: number;
  nombreCliente: string;
}
