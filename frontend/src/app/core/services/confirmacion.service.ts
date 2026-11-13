import { Injectable, signal } from '@angular/core';

export interface PedidoConfirmacion {
  titulo: string;
  /// Uno o mas parrafos: que va a pasar (y con que plata, si corresponde).
  mensaje: string[];
  aceptar: string;
  cancelar?: string;
  /// Accion que no se deshace: el boton de aceptar va en rojo.
  peligro?: boolean;
  /// Linea aparte, chica, al pie del texto (por ejemplo, el aviso de pagos simulados).
  nota?: string;
}

/// Cartel informativo: un solo boton ("Aceptar", "Continuar"...).
export interface Aviso {
  titulo: string;
  mensaje: string[];
  aceptar?: string;
  nota?: string;
}

interface PedidoActivo extends PedidoConfirmacion {
  /// false = cartel informativo (sin boton de "Volver").
  conCancelar: boolean;
  responder: (ok: boolean) => void;
}

/// Pide confirmacion antes de una accion que no se deshace (cancelar una
/// reserva, eliminar una foto, desactivar un usuario...) y muestra carteles con
/// el resultado (informar). Usa una tarjeta propia con Kaza (app-confirmacion,
/// montada una sola vez en app.html) y devuelve una promesa. Uso:
///
///   if (!(await this.confirmacion.pedir({ titulo, mensaje, aceptar, peligro: true }))) return;
@Injectable({ providedIn: 'root' })
export class ConfirmacionService {
  private readonly actual = signal<PedidoActivo | null>(null);
  readonly pedido = this.actual.asReadonly();

  pedir(pedido: PedidoConfirmacion): Promise<boolean> {
    // Si habia otro abierto, se toma como "no".
    this.actual()?.responder(false);
    return new Promise((resolve) => {
      this.actual.set({
        ...pedido,
        conCancelar: true,
        responder: (ok) => {
          this.actual.set(null);
          resolve(ok);
        },
      });
    });
  }

  /// Muestra un cartel con un solo boton y espera a que se cierre.
  informar(aviso: Aviso): Promise<void> {
    this.actual()?.responder(false);
    return new Promise((resolve) => {
      this.actual.set({
        titulo: aviso.titulo,
        mensaje: aviso.mensaje,
        aceptar: aviso.aceptar ?? 'Aceptar',
        nota: aviso.nota,
        conCancelar: false,
        responder: () => {
          this.actual.set(null);
          resolve();
        },
      });
    });
  }
}
