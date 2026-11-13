import { Component, ElementRef, OnInit, computed, inject, input, model, signal } from '@angular/core';
import { aIso, desdeIso, hoyIso, sumarDias } from '../../core/utils/fecha.util';

/// Rango de noches ocupadas: [inicio, fin) -- el dia "fin" es el check-out, asi
/// que ese dia ya se puede volver a usar como check-in (igual que el chequeo de
/// solapamiento del backend: inicio < finNueva && fin > inicioNueva).
export interface RangoOcupado {
  inicio: string;
  fin: string;
}

interface Dia {
  iso: string;
  numero: number;
  deshabilitado: boolean;
  ocupado: boolean;
  esHoy: boolean;
}

interface Mes {
  titulo: string;
  /// Huecos antes del dia 1 (la semana empieza el lunes).
  huecos: number[];
  dias: Dia[];
}

const NOMBRE_MES = new Intl.DateTimeFormat('es-AR', { month: 'long', year: 'numeric' });
const FECHA_LARGA = new Intl.DateTimeFormat('es-AR', { weekday: 'long', day: 'numeric', month: 'long', year: 'numeric' });
const FECHA_CORTA = new Intl.DateTimeFormat('es-AR', { weekday: 'short', day: 'numeric', month: 'short' });

/// Calendario para elegir check-in y check-out con dos toques: el primer dia es
/// el check-in, el segundo el check-out, y los dias del medio quedan marcados.
/// Los dias que no se pueden elegir (pasados u ocupados) se ven en gris y no se
/// pueden tocar. Muestra un mes en el celular y dos desde tablet.
///
/// Con el teclado: Tab llega al calendario y las flechas se mueven entre dias.
@Component({
  selector: 'app-calendario-rango',
  standalone: true,
  templateUrl: './calendario-rango.html',
  styleUrl: './calendario-rango.css',
})
export class CalendarioRango implements OnInit {
  private readonly host = inject(ElementRef<HTMLElement>);

  /// Fechas elegidas en ISO (two-way binding). Vacio = sin elegir.
  readonly inicio = model('');
  readonly fin = model('');
  readonly ocupados = input<RangoOcupado[]>([]);
  /// Primer dia elegible como check-in (por defecto, hoy).
  readonly minimo = input(hoyIso());
  /// "estadia": llegada y salida de una reserva. "periodo": un rango de fechas
  /// cualquiera (por ejemplo, para filtrar); cambian los textos y arranca
  /// mostrando el mes pasado y el actual.
  readonly modo = input<'estadia' | 'periodo'>('estadia');

  readonly diasSemana = ['lu', 'ma', 'mi', 'ju', 'vi', 'sá', 'do'];

  /// Primer mes visible (dia 1 de ese mes). Se ajusta en ngOnInit, cuando ya
  /// llegaron las fechas iniciales (por ejemplo, al reprogramar).
  readonly desde = signal(this.primerDiaDelMes(hoyIso()));
  /// Dia bajo el mouse mientras se elige el check-out (vista previa del rango).
  readonly hover = signal<string | null>(null);
  /// Dia que recibe el foco con Tab (los demas tienen tabindex -1).
  readonly enfocable = signal(hoyIso());

  /// Noches ocupadas, como Set para consultar rapido.
  private readonly nochesOcupadas = computed(() => {
    const set = new Set<string>();
    for (const r of this.ocupados()) {
      for (let d = r.inicio; d < r.fin; d = sumarDias(d, 1)) set.add(d);
    }
    return set;
  });

  /// Eligiendo check-out: el ultimo posible es el comienzo de la primera
  /// noche ocupada despues del check-in (no se puede "saltar" una reserva).
  private readonly topeCheckout = computed(() => {
    const inicio = this.inicio();
    if (!inicio || this.fin()) return null;
    const ocupadasDespues = [...this.nochesOcupadas()].filter((d) => d > inicio).sort();
    return ocupadasDespues[0] ?? null;
  });

  readonly eligiendoSalida = computed(() => !!this.inicio() && !this.fin());

  /// Check-out elegido o, mientras se elige, el dia bajo el mouse (si es valido).
  private readonly finVisible = computed(() => {
    if (this.fin()) return this.fin();
    const h = this.hover();
    const tope = this.topeCheckout();
    if (!this.eligiendoSalida() || !h || h <= this.inicio() || (tope != null && h > tope)) return null;
    return h;
  });

  readonly meses = computed<Mes[]>(() => {
    const base = desdeIso(this.desde());
    return [0, 1].map((k) => this.armarMes(new Date(base.getFullYear(), base.getMonth() + k, 1)));
  });

  readonly puedeRetroceder = computed(() => this.desde() > this.primerDiaDelMes(this.minimo()));

  readonly noches = computed(() => {
    if (!this.inicio() || !this.fin()) return 0;
    return Math.round((desdeIso(this.fin()).getTime() - desdeIso(this.inicio()).getTime()) / 86_400_000);
  });

  readonly resumen = computed(() => {
    const i = this.inicio();
    const f = this.fin();
    if (this.modo() === 'periodo') {
      if (!i) return 'Elegí el primer día';
      if (!f) return `Desde: ${FECHA_CORTA.format(desdeIso(i))} · elegí el último día`;
      return `${FECHA_CORTA.format(desdeIso(i))} → ${FECHA_CORTA.format(desdeIso(f))}`;
    }
    if (!i) return 'Elegí el día de llegada';
    if (!f) return `Llegada: ${FECHA_CORTA.format(desdeIso(i))} · elegí la salida`;
    const n = this.noches();
    return `${FECHA_CORTA.format(desdeIso(i))} → ${FECHA_CORTA.format(desdeIso(f))} · ${n} ${n === 1 ? 'noche' : 'noches'}`;
  });

  ngOnInit(): void {
    const hoy = hoyIso();
    const inicial = this.inicio() || (this.minimo() > hoy ? this.minimo() : hoy);
    // En "periodo" casi siempre se busca hacia atras: se ven el mes pasado y el actual.
    const primerMes = this.modo() === 'periodo' && !this.inicio() ? sumarDias(this.primerDiaDelMes(hoy), -1) : inicial;
    this.desde.set(this.primerDiaDelMes(primerMes));
    this.enfocable.set(inicial);
  }

  mover(meses: number): void {
    const d = desdeIso(this.desde());
    const nuevo = aIso(new Date(d.getFullYear(), d.getMonth() + meses, 1));
    if (meses < 0 && nuevo < this.primerDiaDelMes(this.minimo())) return;
    this.desde.set(nuevo);
  }

  elegir(dia: Dia): void {
    if (this.deshabilitado(dia)) return;
    const inicio = this.inicio();
    if (this.eligiendoSalida() && dia.iso > inicio) {
      this.fin.set(dia.iso);
    } else {
      // Primer toque, o un dia anterior al check-in: arranca un rango nuevo.
      this.inicio.set(dia.iso);
      this.fin.set('');
    }
    this.enfocable.set(dia.iso);
    this.hover.set(null);
  }

  limpiar(): void {
    this.inicio.set('');
    this.fin.set('');
    this.hover.set(null);
  }

  /// Un dia no se puede tocar si es anterior al minimo, si esa noche esta
  /// ocupada (salvo que sirva como check-out) o si queda despues de una
  /// reserva ajena mientras se elige el check-out.
  deshabilitado(dia: Dia): boolean {
    if (dia.iso < this.minimo()) return true;
    if (this.eligiendoSalida() && dia.iso > this.inicio()) {
      const tope = this.topeCheckout();
      return tope != null && dia.iso > tope;
    }
    return dia.ocupado;
  }

  enRango(iso: string): boolean {
    const i = this.inicio();
    const f = this.finVisible();
    return !!i && !!f && iso > i && iso < f;
  }

  esExtremo(iso: string): 'inicio' | 'fin' | null {
    if (iso === this.inicio()) return 'inicio';
    return iso === this.finVisible() ? 'fin' : null;
  }

  etiqueta(dia: Dia): string {
    let texto = FECHA_LARGA.format(desdeIso(dia.iso));
    if (dia.iso === this.inicio()) texto += ', check-in';
    if (dia.iso === this.fin()) texto += ', check-out';
    if (this.deshabilitado(dia)) texto += dia.ocupado ? ', no disponible' : ', no se puede elegir';
    return texto;
  }

  /// Flechas: un dia (izquierda/derecha) o una semana (arriba/abajo).
  teclado(evento: KeyboardEvent, dia: Dia): void {
    const pasos: Record<string, number> = { ArrowLeft: -1, ArrowRight: 1, ArrowUp: -7, ArrowDown: 7 };
    const paso = pasos[evento.key];
    if (paso == null) return;
    evento.preventDefault();
    const destino = sumarDias(dia.iso, paso);
    if (destino < this.minimo()) return;
    // Si el destino queda fuera de los meses visibles, se pasa de mes.
    const visibles = this.meses();
    const ultimo = visibles[visibles.length - 1].dias.at(-1)!.iso;
    if (destino < this.desde()) this.mover(-1);
    else if (destino > ultimo) this.mover(1);
    this.enfocable.set(destino);
    queueMicrotask(() =>
      (this.host.nativeElement as HTMLElement).querySelector<HTMLButtonElement>(`[data-fecha="${destino}"]`)?.focus(),
    );
  }

  private armarMes(primero: Date): Mes {
    const hoy = hoyIso();
    const ocupadas = this.nochesOcupadas();
    const cantidad = new Date(primero.getFullYear(), primero.getMonth() + 1, 0).getDate();
    const dias: Dia[] = [];
    for (let n = 1; n <= cantidad; n++) {
      const iso = aIso(new Date(primero.getFullYear(), primero.getMonth(), n));
      dias.push({ iso, numero: n, deshabilitado: false, ocupado: ocupadas.has(iso), esHoy: iso === hoy });
    }
    const titulo = NOMBRE_MES.format(primero);
    return {
      titulo: titulo.charAt(0).toUpperCase() + titulo.slice(1),
      // getDay(): 0 = domingo. Con la semana empezando el lunes, domingo = 6 huecos.
      huecos: Array.from({ length: (primero.getDay() + 6) % 7 }, (_, i) => i),
      dias,
    };
  }

  private primerDiaDelMes(iso: string): string {
    return iso.slice(0, 8) + '01';
  }
}
