/// Espejo de ServicioResponseDTO. Catalogo administrable de servicios/comodidades.
export interface Servicio {
  idServicio: number;
  nombreServicio: string;
}

/// Espejo de ServicioxAlojamientoResponseDTO (un servicio ya vinculado a un alojamiento puntual).
export interface ServicioxAlojamiento {
  idServicioXAlojamiento: number;
  servicio: Servicio;
}
