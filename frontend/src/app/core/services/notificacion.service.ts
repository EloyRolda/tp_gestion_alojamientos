import { DestroyRef, Injectable, effect, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpClient } from '@angular/common/http';
import { interval, startWith, Subscription } from 'rxjs';
import { AuthService } from './auth.service';
import { Notificacion } from '../models/notificacion.model';
import { API_BASE_URL } from '../config/api.config';

/// Servicio para el buzón de avisos internos (campana del encabezado).
/// Mantiene un signal con la cantidad de avisos sin leer y ofrece los métodos
/// para listar y marcar como leídos. La consulta periódica se activa y se corta
/// según el estado de autenticación, sin hacer pedidos HTTP en el constructor.
@Injectable({ providedIn: 'root' })
export class NotificacionService {
  private readonly http = inject(HttpClient);
  private readonly auth = inject(AuthService);
  private readonly destroyRef = inject(DestroyRef);

  private suscripcionPeriodica?: Subscription;

  readonly sinLeer = signal<number>(0);

  private baseUrl = `${API_BASE_URL}/Notificacion`;

  constructor() {
    effect(() => {
      if (this.auth.estaLogueado()) {
        this.iniciarConsulta();
      } else {
        this.detenerConsulta();
      }
    });
  }

  listar() {
    return this.http.get<Notificacion[]>(`${this.baseUrl}/mias`);
  }

  marcarLeida(id: number) {
    return this.http.post<void>(`${this.baseUrl}/${id}/leida`, {});
  }

  marcarTodas() {
    return this.http.post<void>(`${this.baseUrl}/leidas`, {});
  }

  consultarSinLeer() {
    return this.http.get<number>(`${this.baseUrl}/sin-leer`);
  }

  private iniciarConsulta() {
    if (this.suscripcionPeriodica) return;

    this.suscripcionPeriodica = interval(30_000)
      .pipe(startWith(0), takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.consultarSinLeer().subscribe({
          next: (cantidad) => this.sinLeer.set(cantidad),
          error: () => this.sinLeer.set(0),
        });
      });
  }

  private detenerConsulta() {
    this.suscripcionPeriodica?.unsubscribe();
    this.suscripcionPeriodica = undefined;
    this.sinLeer.set(0);
  }
}
