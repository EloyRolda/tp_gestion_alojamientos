/// Servicio para pedirle al administrador el motivo y detalle de una suspensión
/// u ocultamiento. Sigue el patrón del diálogo reutilizable: el componente
/// `app-motivo-suspension` se monta una vez en `app.html` y este servicio lo
/// controla con `pedir()`.
import { Injectable, signal } from '@angular/core';
import { CategoriaReporte } from '../models/reporte.model';

export interface PedidoMotivo {
  titulo: string;
  texto: string;
  categoria: CategoriaReporte;
  motivoInicial?: string;
}

export interface ResultadoMotivo {
  motivo: string;
  detalle: string;
}

@Injectable({ providedIn: 'root' })
export class MotivoSuspensionService {
  /// Pedido que está esperando una respuesta del diálogo.
  readonly pedidoActual = signal<PedidoMotivo | null>(null);

  private resolverActual: ((resultado: ResultadoMotivo | null) => void) | null = null;

  /// Abre el diálogo (lo hace visible el componente) y devuelve una promesa
  /// que se resuelve cuando el administrador confirma o cancela.
  pedir(pedido: PedidoMotivo): Promise<ResultadoMotivo | null> {
    this.pedidoActual.set(pedido);
    return new Promise((resolve) => {
      this.resolverActual = resolve;
    });
  }

  /// Lo llama el componente cuando se confirma o cancela.
  responder(resultado: ResultadoMotivo | null): void {
    this.pedidoActual.set(null);
    this.resolverActual?.(resultado);
    this.resolverActual = null;
  }
}
