/// Espejo de DireccionResponseDTO.
export interface Direccion {
  idDireccion: number;
  pais: string;
  provincia: string;
  codigoPostal: string;
  ciudad: string;
  calle: string;
  altura: number;
  /// Ubicacion en el mapa; null si el anfitrion no la marco.
  latitud: number | null;
  longitud: number | null;
}

/// Espejo de DireccionDTO (lo que pide AlojamientoRegistroDTO.direccion).
export interface DireccionInput {
  pais: string;
  provincia: string;
  codigoPostal: string;
  ciudad: string;
  calle: string;
  altura: number;
  /// Opcionales: si se manda una, hay que mandar la otra.
  latitud?: number | null;
  longitud?: number | null;
}
