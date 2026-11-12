import { Injectable, signal } from '@angular/core';

export interface Aviso {
  id: number;
  texto: string;
  saliendo: boolean;
}

const DURACION_MS = 3500;
const SALIDA_MS = 220;

/// Avisos breves abajo de la pantalla (SCRUM-68): confirman que una accion
/// salio bien (iniciar sesion, guardar, cancelar, confirmar...) y se van solos.
/// Los errores NO van por aca: se muestran al lado de donde ocurrieron.
@Injectable({ providedIn: 'root' })
export class AvisoService {
  private siguienteId = 1;
  private readonly lista = signal<Aviso[]>([]);
  readonly avisos = this.lista.asReadonly();

  mostrar(texto: string): void {
    const id = this.siguienteId++;
    this.lista.update((actuales) => [...actuales.slice(-2), { id, texto, saliendo: false }]);
    setTimeout(() => this.cerrar(id), DURACION_MS);
  }

  cerrar(id: number): void {
    this.lista.update((actuales) => actuales.map((a) => (a.id === id ? { ...a, saliendo: true } : a)));
    setTimeout(() => this.lista.update((actuales) => actuales.filter((a) => a.id !== id)), SALIDA_MS);
  }
}
