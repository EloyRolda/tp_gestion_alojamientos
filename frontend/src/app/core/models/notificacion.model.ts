/// Representa un aviso interno que se muestra en la campana del encabezado.
export interface Notificacion {
  id: number;
  tipo: string;
  texto: string;
  enlace: string | null;
  leida: boolean;
  fecha: string;
}
