import { Component, computed, input } from '@angular/core';
import { EstadoReserva } from '../../core/models/reserva.model';
import { EstadoPago } from '../../core/models/pago.model';

/// Etiqueta de color para el estado de una reserva o de un pago. Un solo lugar
/// para el texto que ve el usuario y el color de cada estado (clases .chip--*
/// de styles.css), asi todas las pantallas los muestran igual.
const TEXTOS: Record<EstadoReserva | EstadoPago, string> = {
  SOLICITADA: 'Solicitada',
  CONFIRMADA: 'Confirmada',
  FINALIZADA: 'Finalizada',
  CANCELADA: 'Cancelada',
  PENDIENTE: 'Pendiente de pago',
  PAGADO: 'Pagado',
  VENCIDO: 'Vencido',
  REEMBOLSADO_TOTAL: 'Reembolso total',
  REEMBOLSADO_PARCIAL: 'Reembolso parcial',
};

@Component({
  selector: 'app-estado',
  standalone: true,
  template: `<span [class]="'chip chip--' + clase()">{{ texto() }}</span>`,
})
export class Estado {
  readonly valor = input.required<EstadoReserva | EstadoPago>();

  protected readonly clase = computed(() => this.valor().toLowerCase());
  protected readonly texto = computed(() => TEXTOS[this.valor()] ?? this.valor());
}
