/// Espejo de DireccionResponseDTO.
export interface Direccion {
  idDireccion: number;
  pais: string;
  provincia: string;
  codigoPostal: string;
  ciudad: string;
  calle: string;
  altura: number;
}

/// Espejo de DireccionDTO (lo que pide AlojamientoRegistroDTO.direccion).
export interface DireccionInput {
  pais: string;
  provincia: string;
  codigoPostal: string;
  ciudad: string;
  calle: string;
  altura: number;
}
