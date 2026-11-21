/// Espejos de los enums y DTOs de reportes del backend (RF44). El "codigo" de un
/// motivo es el nombre del enum, y "aplicaA" dice para que objetivo sirve cada uno.

/// Espejo de TIPOREPORTE.
export type TipoReporte = 'ALOJAMIENTO' | 'USUARIO';

/// Espejo de ESTADOREPORTE.
export type EstadoReporte = 'PENDIENTE' | 'RESUELTO';

/// Espejo de ACCIONMODERACION (la usa la pantalla de moderacion, SCRUM-79).
export type AccionModeracion = 'DESCARTAR' | 'OCULTAR_ALOJAMIENTO' | 'DESACTIVAR_USUARIO';

/// A que tipo de objetivo aplica un motivo.
export type CategoriaReporte = 'CLIENTE' | 'ANFITRION' | 'ALOJAMIENTO';

/// Espejo de MotivoReporteDTO.
export interface MotivoReporte {
  codigo: string;
  etiqueta: string;
  aplicaA: CategoriaReporte[];
}

/// Espejo de ReporteRegistroDTO.
export interface ReporteRegistro {
  tipo: TipoReporte;
  idObjetivo: number;
  /// Nombre del enum MOTIVOREPORTE (ej: 'TRATO_OFENSIVO').
  motivo: string;
  /// Obligatorio cuando el motivo es 'OTRO' (lo valida el backend).
  detalle?: string | null;
}

/// Espejo de ReporteResponseDTO.
export interface Reporte {
  idReporte: number;
  tipo: TipoReporte;
  idObjetivo: number;
  motivo: string;
  etiquetaMotivo: string | null;
  detalle: string | null;
  estado: EstadoReporte;
  fechaCreacion: string;
}
