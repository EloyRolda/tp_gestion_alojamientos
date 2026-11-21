/// Abre y cierra la carta de reporte (<app-reportar />). Funciona igual que
/// ConfirmacionService: una sola carta montada en app.html, que cualquier
/// pantalla abre con abrir({...}).
import { Injectable, signal } from '@angular/core';
import { CategoriaReporte, TipoReporte } from '../models/reporte.model';

export interface PedidoReporte {
  tipo: TipoReporte;
  idObjetivo: number;
  /// Que se esta reportando (CLIENTE / ANFITRION / ALOJAMIENTO): filtra los motivos.
  categoria: CategoriaReporte;
  /// Nombre que aparece en el titulo de la carta ("Reportar a <nombre>").
  nombreObjetivo: string;
}

@Injectable({ providedIn: 'root' })
export class ReportarService {
  private readonly pedidoActual = signal<PedidoReporte | null>(null);
  /// El pedido vigente, o null si la carta esta cerrada.
  readonly pedido = this.pedidoActual.asReadonly();

  abrir(pedido: PedidoReporte): void {
    this.pedidoActual.set(pedido);
  }

  cerrar(): void {
    this.pedidoActual.set(null);
  }
}
