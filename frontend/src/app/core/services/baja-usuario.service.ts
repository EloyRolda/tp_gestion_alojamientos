import { Injectable, inject } from '@angular/core';
import { Observable, catchError, concatMap, firstValueFrom, forkJoin, from, map, of, switchMap, toArray } from 'rxjs';
import { Router } from '@angular/router';
import { ConfirmacionService } from './confirmacion.service';
import { AlojamientoService } from './alojamiento.service';
import { ReservaService } from './reserva.service';
import { Alojamiento } from '../models/alojamiento.model';
import { Reserva } from '../models/reserva.model';
import { Usuario } from '../models/usuario.model';

/// Lo que se ve afectado si el admin desactiva una cuenta.
export interface PlanBaja {
  /// Alojamientos publicados (activos) del anfitrion: se pausan.
  alojamientos: Alojamiento[];
  /// Reservas solicitadas o confirmadas sin pagar: se cancelan (no mueven plata).
  sinPago: Reserva[];
  /// Reservas confirmadas y ya pagadas: NO se tocan solas (cancelarlas devuelve
  /// plata); quedan para que el admin decida desde "Todas las reservas".
  pagadas: Reserva[];
}

export interface ResultadoBaja {
  pausados: number;
  canceladas: number;
  fallidas: number;
}

/// Baja en cascada de una cuenta, del lado del frontend.
///
/// El backend, al desactivar un usuario, solo le cambia el estado: los
/// alojamientos de un anfitrion desactivado seguian apareciendo en la busqueda
/// (y reservarlos fallaba con "El anfitrion esta inactivo"), y las solicitudes
/// de un cliente desactivado seguian vivas aunque nunca iba a poder pagarlas.
/// Este servicio arma el "plan" (que se ve afectado, para mostrarlo antes de
/// confirmar) y despues lo ejecuta con los endpoints que el admin ya puede usar:
/// pausar alojamientos (PUT /Alojamiento/actualizar) y cancelar reservas sin
/// pago (PATCH /Reserva/cancelar).
@Injectable({ providedIn: 'root' })
export class BajaUsuarioService {
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly reservaService = inject(ReservaService);
  private readonly carta = inject(ConfirmacionService);
  private readonly router = inject(Router);

  /// Paso 1: arma el plan y pide confirmacion mostrando que se va a ver
  /// afectado. Devuelve el plan si el admin acepta; null si vuelve atras.
  /// Si el analisis falla, igual deja desactivar (sin cascada) avisandolo.
  async confirmarBaja(u: Usuario): Promise<PlanBaja | null> {
    let plan: PlanBaja;
    try {
      plan = await firstValueFrom(this.analizar(u));
    } catch {
      plan = { alojamientos: [], sinPago: [], pagadas: [] };
    }
    const ok = await this.carta.pedir({
      titulo: `Desactivar a ${u.nombre} ${u.apellido}`,
      mensaje: this.describir(u, plan),
      aceptar: 'Desactivar',
      peligro: true,
    });
    return ok ? plan : null;
  }

  /// Paso 2 (con la cuenta ya desactivada): aplica la cascada y muestra el resultado.
  async aplicarCascada(u: Usuario, plan: PlanBaja): Promise<void> {
    const r = await firstValueFrom(this.ejecutar(plan));
    const lineas: string[] = [];
    if (r.pausados) lineas.push(`Se pausaron ${cantidad(r.pausados, 'alojamiento', 'alojamientos')}.`);
    if (r.canceladas) lineas.push(`Se cancelaron ${cantidad(r.canceladas, 'reserva sin pago', 'reservas sin pago')}.`);
    if (plan.pagadas.length) {
      lineas.push(`Quedan ${cantidad(plan.pagadas.length, 'reserva pagada', 'reservas pagadas')} para revisar: están en su ficha, sección Reservas.`);
    }
    if (r.fallidas) lineas.push(`${cantidad(r.fallidas, 'cambio no se pudo hacer', 'cambios no se pudieron hacer')}: revisalo a mano.`);
    if (!lineas.length) lineas.push('No tenía reservas activas ni alojamientos publicados.');
    const titulo = `${u.nombre} ${u.apellido} quedó desactivado`;
    if (plan.pagadas.length) {
      // Con reservas pagadas pendientes de decidir: un boton directo a ellas.
      const ver = await this.carta.pedir({ titulo, mensaje: lineas, aceptar: 'Ver sus reservas', cancelar: 'Cerrar' });
      if (ver) void this.router.navigate(['/admin/usuarios', u.id], { fragment: 'reservas' });
      return;
    }
    await this.carta.informar({ titulo, mensaje: lineas });
  }

  /// Al reactivar no se republica nada solo: se avisa.
  async avisarReactivacion(u: Usuario): Promise<void> {
    await this.carta.informar({
      titulo: `${u.nombre} ${u.apellido} puede volver a entrar`,
      mensaje:
        u.tipo === 'ANFITRION'
          ? ['Sus alojamientos siguen pausados: puede volver a publicarlos desde Mis alojamientos.']
          : ['La cuenta quedó activa de nuevo.'],
    });
  }

  analizar(u: Usuario): Observable<PlanBaja> {
    if (u.tipo === 'ADMIN') {
      return of({ alojamientos: [], sinPago: [], pagadas: [] });
    }
    return forkJoin({
      reservas: this.reservaService.listarTodas(),
      publicados: this.alojamientoService.listar(),
    }).pipe(
      switchMap(({ reservas, publicados }) => {
        const activas = reservas.filter((r) => r.estadoReserva === 'SOLICITADA' || r.estadoReserva === 'CONFIRMADA');
        if (u.tipo === 'CLIENTE') {
          return of(this.armar([], activas.filter((r) => r.cliente.id === u.id)));
        }
        // Anfitrion: sus alojamientos publicados, y ademas los pausados que
        // tengan reservas activas (el listado publico no los trae: se consulta
        // cada uno para saber de quien es).
        const propios = publicados.filter((a) => a.anfitrion.id === u.id);
        const idsPublicados = new Set(publicados.map((a) => a.idAlojamiento));
        const idsPropios = new Set(propios.map((a) => a.idAlojamiento));
        const otros = [...new Set(activas.map((r) => r.alojamiento.idAlojamiento))].filter((id) => !idsPublicados.has(id));
        const consultas = otros.length
          ? forkJoin(otros.map((id) => this.alojamientoService.obtener(id).pipe(catchError(() => of(null)))))
          : of([] as (Alojamiento | null)[]);
        return consultas.pipe(
          map((pausados) => {
            for (const a of pausados) {
              if (a && a.anfitrion.id === u.id) idsPropios.add(a.idAlojamiento);
            }
            return this.armar(propios, activas.filter((r) => idsPropios.has(r.alojamiento.idAlojamiento)));
          }),
        );
      }),
    );
  }

  /// Pausa los alojamientos y cancela las reservas sin pago, de a uno (si una
  /// falla, sigue con el resto y la cuenta en "fallidas").
  ejecutar(plan: PlanBaja): Observable<ResultadoBaja> {
    const pasos: Observable<'pausado' | 'cancelada' | 'fallo'>[] = [
      ...plan.alojamientos.map((a) =>
        this.alojamientoService.actualizar({ idAlojamiento: a.idAlojamiento, activo: false }).pipe(map(() => 'pausado' as const)),
      ),
      ...plan.sinPago.map((r) => this.reservaService.cancelar(r.idReserva).pipe(map(() => 'cancelada' as const))),
    ];
    if (pasos.length === 0) return of({ pausados: 0, canceladas: 0, fallidas: 0 });
    return from(pasos).pipe(
      concatMap((paso) => paso.pipe(catchError(() => of('fallo' as const)))),
      toArray(),
      map((r) => ({
        pausados: r.filter((x) => x === 'pausado').length,
        canceladas: r.filter((x) => x === 'cancelada').length,
        fallidas: r.filter((x) => x === 'fallo').length,
      })),
    );
  }

  /// Texto para la carta de confirmacion.
  describir(u: Usuario, plan: PlanBaja): string[] {
    const lineas = ['No va a poder iniciar sesión hasta que la vuelvas a activar, y si tenía la sesión abierta se le corta.'];
    if (plan.alojamientos.length) {
      lineas.push(`Se pausan sus ${cantidad(plan.alojamientos.length, 'alojamiento publicado', 'alojamientos publicados')}: dejan de aparecer en las búsquedas.`);
    }
    if (plan.sinPago.length) {
      lineas.push(`Se cancelan ${cantidad(plan.sinPago.length, 'reserva que todavía no se pagó', 'reservas que todavía no se pagaron')} (no se mueve dinero).`);
    }
    if (plan.pagadas.length) {
      lineas.push(
        `${cantidad(plan.pagadas.length, 'reserva ya pagada queda como está', 'reservas ya pagadas quedan como están')}: las ves en su ficha (tocá su nombre en Gestión de usuarios), sección Reservas, para cancelarlas con reembolso o mantenerlas.`,
      );
    }
    if (u.tipo !== 'ADMIN' && !plan.alojamientos.length && !plan.sinPago.length && !plan.pagadas.length) {
      lineas.push('No tiene reservas activas ni alojamientos publicados.');
    }
    return lineas;
  }

  private armar(alojamientos: Alojamiento[], reservas: Reserva[]): PlanBaja {
    return {
      alojamientos,
      sinPago: reservas.filter((r) => r.estadoReserva === 'SOLICITADA' || r.pago?.estado !== 'PAGADO'),
      pagadas: reservas.filter((r) => r.estadoReserva === 'CONFIRMADA' && r.pago?.estado === 'PAGADO'),
    };
  }
}

function cantidad(n: number, singular: string, plural: string): string {
  return `${n} ${n === 1 ? singular : plural}`;
}
