import { Direccion } from './direccion.model';
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
