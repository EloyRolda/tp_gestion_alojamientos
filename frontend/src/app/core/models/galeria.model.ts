/// Espejo de GaleriaResponseDTO (una galeria por alojamiento).
export interface Galeria {
  id: number;
  nombreGaleria: string;
  idAlojamiento: number;
}

/// Espejo de ImagenResponseDTO.
export interface Imagen {
  id: number;
  alt: string;
  url: string;
  idGaleria: number;
}
