import { Injectable, inject } from '@angular/core';
import { ConfirmacionService } from './confirmacion.service';

/// Avisos de que una accion salio bien (iniciar sesion, guardar, confirmar,
/// publicar...). Se muestran en la misma carta con Kaza que las
/// confirmaciones (ver ConfirmacionService), asi toda la app usa un solo tipo
/// de cartel. Los errores NO van por aca: se muestran al lado de donde ocurrieron.
@Injectable({ providedIn: 'root' })
export class AvisoService {
  private readonly carta = inject(ConfirmacionService);

  /// titulo: lo que paso; detalle: que sigue (opcional).
  mostrar(titulo: string, detalle: string[] = []): void {
    void this.carta.informar({ titulo, mensaje: detalle });
  }
}
