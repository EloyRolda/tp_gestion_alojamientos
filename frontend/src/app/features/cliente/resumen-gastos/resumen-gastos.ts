import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { EstadisticasService } from '../../../core/services/estadisticas.service';
import { ResumenCliente } from '../../../core/models/estadisticas.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';

/// SCRUM-34 / RF38. Resumen de gastos del cliente logueado.
@Component({
  selector: 'app-resumen-gastos',
  standalone: true,
  imports: [DecimalPipe, Esqueleto],
  templateUrl: './resumen-gastos.html',
  styleUrl: './resumen-gastos.css',
})
export class ResumenGastos {
  private readonly estadisticasService = inject(EstadisticasService);

  readonly resumen = signal<ResumenCliente | null>(null);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  constructor() {
    this.estadisticasService.resumenCliente().subscribe({
      next: (r) => {
        this.resumen.set(r);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }
}
