import { Direccion, DireccionInput } from './direccion.model';
import { TipoAlojamiento } from './tipo-alojamiento.model';

/// Espejo de UsuarioResumenDTO: version publica/liviana embebida en Alojamiento.anfitrion.
export interface UsuarioResumen {
  id: number;
  nombre: string;
  apellido: string;
}

/// Espejo de AlojamientoResponseDTO.
export interface Alojamiento {
  idAlojamiento: number;
  titulo: string;
  descripcion: string;
  precioNoche: number;
  capacidad: number;
  cantAmbientes: number;
  cantHabitaciones: number;
  cantBanios: number;
  activo: boolean;
  anfitrion: UsuarioResumen;
  direccion: Direccion;
  tipoAlojamiento: TipoAlojamiento;
}

/// Espejo de AlojamientoRegistroDTO. Siempre crea una direccion nueva (no se
/// ofrece reutilizar idDireccion -- caso de uso avanzado, no pedido por ningun RF).
export interface AlojamientoRegistro {
  titulo: string;
  descripcion: string;
  precioNoche: number;
  capacidad: number;
  cantAmbientes: number;
  cantHabitaciones: number;
  cantBanios: number;
  nombreTipoAlojamiento: string;
  nombresServicios: string[];
  direccion: DireccionInput;
}

/// Espejo de AlojamientoModificarDTO. Todos los campos opcionales salvo el id
/// (solo se pisa lo que venga no-nulo) -- no se ofrece reemplazar la direccion
/// por otra existente desde esta vista (idDireccion), mismo criterio que arriba.
export interface AlojamientoModificar {
  idAlojamiento: number;
  titulo?: string;
  descripcion?: string;
  precioNoche?: number;
  capacidad?: number;
  cantAmbientes?: number;
  cantHabitaciones?: number;
  cantBanios?: number;
  activo?: boolean;
  nombreTipoAlojamiento?: string;
}
