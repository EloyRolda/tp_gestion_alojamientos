import { Component, ElementRef, computed, effect, inject, input, signal, untracked, viewChild } from '@angular/core';
import { NgTemplateOutlet } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { catchError, forkJoin, map, of } from 'rxjs';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { Servicio } from '../../../core/models/servicio.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { AlojamientoCard } from '../../../shared/alojamiento-card/alojamiento-card';
import { MapaAlojamientos } from '../../../shared/mapa-alojamientos/mapa-alojamientos';
import { coincide } from '../../../core/utils/busqueda.util';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';
import { Kaza } from '../../../shared/kaza/kaza';
import { BuscadorLista } from '../../../shared/buscador-lista/buscador-lista';

/// Tarjetas por pagina: 4 filas de 3 en desktop.
const POR_PAGINA = 12;

/// SCRUM-23 / RF14, RF39. El backend no expone query params de filtro
/// (/Alojamiento/listar siempre devuelve todo), asi que el filtro es
/// client-side sobre la lista completa. Para poder filtrar por servicio, se trae una vez
/// (en paralelo, al cargar la pagina) que servicios tiene cada alojamiento;
/// de ahi en mas filtrar es instantaneo, sin pegarle de nuevo a la API.
///
/// Responsive (SCRUM-52): en mobile y tablet la busqueda por texto queda
/// siempre visible y el resto de los filtros se pliega detras de un boton que
/// muestra cuantos hay activos; en desktop los filtros son una barra lateral
/// fija a la izquierda de la grilla de resultados.
///
/// Paginado: la lista muestra de a POR_PAGINA tarjetas, con "anterior /
/// siguiente" arriba a la derecha y al pie; cualquier cambio de filtro vuelve
/// a la pagina 1. El mapa muestra siempre todos los resultados.
///
/// Servicios: como el catalogo puede ser largo, se eligen en una carta aparte
/// (con buscador) en vez de una lista de casillas en la barra lateral.
///
/// Modo mapa (SCRUM-66): el componente del mapa va en un bloque @defer, asi que
/// Leaflet no pesa en la pagina de quien solo usa la lista.
@Component({
  selector: 'app-busqueda',
  standalone: true,
  imports: [FormsModule, NgTemplateOutlet, AlojamientoCard, MapaAlojamientos, Esqueleto, Vacio, Kaza, BuscadorLista],
  templateUrl: './busqueda.html',
  styleUrl: './busqueda.css',
})
export class Busqueda {
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly catalogoService = inject(CatalogoService);

  private readonly todos = signal<Alojamiento[]>([]);
  private readonly serviciosPorAlojamiento = signal<Map<number, number[]>>(new Map());
  /// Hasta que no se sabe que servicios tiene cada alojamiento, filtrar por
  /// servicio dejaria la lista vacia: el boton espera a que termine de cargar.
  readonly serviciosListos = signal(false);

  readonly serviciosDisponibles = signal<Servicio[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  readonly texto = signal('');

  /// Texto que llega desde el buscador del encabezado (/buscar?q=...).
  readonly q = input<string>();
  readonly precioMin = signal<number | null>(null);
  readonly precioMax = signal<number | null>(null);
  readonly capacidadMin = signal<number | null>(null);
  readonly serviciosSeleccionados = signal<Set<number>>(new Set());

  /// Valores sugeridos para no tener que escribir numeros: rangos de precio por
  /// noche y cantidad minima de huespedes. Un toque los aplica y otro los saca.
  readonly rangosPrecio = [
    { etiqueta: 'Hasta $50.000', min: null, max: 50000 },
    { etiqueta: '$50.000 a $100.000', min: 50000, max: 100000 },
    { etiqueta: '$100.000 a $150.000', min: 100000, max: 150000 },
    { etiqueta: 'Más de $150.000', min: 150000, max: null },
  ];
  readonly opcionesHuespedes = [1, 2, 3, 4, 6, 8];

  rangoActivo(r: { min: number | null; max: number | null }): boolean {
    const min = this.tieneValor(this.precioMin()) ? Number(this.precioMin()) : null;
    const max = this.tieneValor(this.precioMax()) ? Number(this.precioMax()) : null;
    return min === r.min && max === r.max;
  }

  elegirRango(r: { min: number | null; max: number | null }): void {
    if (this.rangoActivo(r)) {
      this.precioMin.set(null);
      this.precioMax.set(null);
    } else {
      this.precioMin.set(r.min);
      this.precioMax.set(r.max);
    }
  }

  elegirHuespedes(n: number): void {
    this.capacidadMin.set(this.capacidadMin() === n ? null : n);
  }

  /// Resultados en grilla de tarjetas o en el mapa (SCRUM-66). Los dos usan la
  /// misma lista filtrada, asi que los filtros aplican igual en ambos.
  readonly modo = signal<'lista' | 'mapa'>('lista');

  /// Solo tiene efecto debajo de 1024px: en desktop el panel siempre se ve.
  readonly filtrosAbiertos = signal(false);

  private readonly copiarTextoDelEncabezado = effect(() => {
    this.texto.set(this.q() ?? '');
  });

  /// Filtros aplicados ademas del texto, para mostrarlos en el boton plegado.
  readonly filtrosActivos = computed(() => {
    let cantidad = this.serviciosSeleccionados().size;
    if (this.tieneValor(this.precioMin())) cantidad++;
    if (this.tieneValor(this.precioMax())) cantidad++;
    if (this.tieneValor(this.capacidadMin())) cantidad++;
    return cantidad;
  });

  readonly resultados = computed<Alojamiento[]>(() => {
    const texto = this.texto().trim();
    const precioMin = this.tieneValor(this.precioMin()) ? this.precioMin() : null;
    const precioMax = this.tieneValor(this.precioMax()) ? this.precioMax() : null;
    const capacidadMin = this.tieneValor(this.capacidadMin()) ? this.capacidadMin() : null;
    const serviciosSel = this.serviciosSeleccionados();
    const mapaServicios = this.serviciosPorAlojamiento();

    return this.todos().filter((a) => {
      if (texto && !coincide(texto, a.titulo, a.direccion.ciudad, a.direccion.provincia, a.direccion.calle, a.tipoAlojamiento.nombreTipo)) {
        return false;
      }
      if (precioMin !== null && a.precioNoche < precioMin) return false;
      if (precioMax !== null && a.precioNoche > precioMax) return false;
      if (capacidadMin !== null && a.capacidad < capacidadMin) return false;
      if (serviciosSel.size > 0) {
        const idsDelAlojamiento = mapaServicios.get(a.idAlojamiento) ?? [];
        const tieneTodosLosSeleccionados = [...serviciosSel].every((id) => idsDelAlojamiento.includes(id));
        if (!tieneTodosLosSeleccionados) return false;
      }
      return true;
    });
  });

  readonly pagina = signal(1);
  readonly totalPaginas = computed(() => Math.max(1, Math.ceil(this.resultados().length / POR_PAGINA)));
  readonly paginaActual = computed(() => Math.min(this.pagina(), this.totalPaginas()));
  readonly desde = computed(() => (this.paginaActual() - 1) * POR_PAGINA + 1);
  readonly hasta = computed(() => Math.min(this.paginaActual() * POR_PAGINA, this.resultados().length));
  readonly visibles = computed(() => this.resultados().slice(this.desde() - 1, this.hasta()));

  /// Filtros nuevos = resultados nuevos: se arranca de la primera pagina.
  private readonly volverAPrimeraPagina = effect(() => {
    this.resultados();
    untracked(() => this.pagina.set(1));
  });

  irAPagina(n: number): void {
    this.pagina.set(Math.min(Math.max(1, n), this.totalPaginas()));
    const suave = !window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    window.scrollTo({ top: 0, behavior: suave ? 'smooth' : 'auto' });
  }

  private readonly dialogoServicios = viewChild.required<ElementRef<HTMLDialogElement>>('dialogoServicios');
  readonly buscarServicio = signal('');
  readonly serviciosFiltrados = computed(() => {
    const t = this.buscarServicio().trim();
    return t ? this.serviciosDisponibles().filter((s) => coincide(t, s.nombreServicio)) : this.serviciosDisponibles();
  });
  /// "Wifi, Pileta y 2 más": para ver lo elegido sin abrir la carta.
  readonly nombresSeleccionados = computed(() => {
    const sel = this.serviciosSeleccionados();
    const nombres = this.serviciosDisponibles().filter((s) => sel.has(s.idServicio)).map((s) => s.nombreServicio);
    if (nombres.length === 0) return '';
    return nombres.length <= 3 ? nombres.join(', ') : `${nombres.slice(0, 2).join(', ')} y ${nombres.length - 2} más`;
  });

  abrirServicios(): void {
    this.buscarServicio.set('');
    this.dialogoServicios().nativeElement.showModal();
  }

  cerrarServicios(): void {
    this.dialogoServicios().nativeElement.close();
  }

  clicFondoServicios(evento: MouseEvent): void {
    if (evento.target === evento.currentTarget) this.cerrarServicios();
  }

  limpiarServicios(): void {
    this.serviciosSeleccionados.set(new Set());
  }

  constructor() {
    this.catalogoService.listarServicios().subscribe({ next: (lista) => this.serviciosDisponibles.set(lista) });

    this.alojamientoService.listar().subscribe({
      next: (lista) => {
        this.todos.set(lista);
        this.cargando.set(false);
        this.cargarServiciosDeTodos(lista);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  toggleServicio(idServicio: number): void {
    const actuales = new Set(this.serviciosSeleccionados());
    if (actuales.has(idServicio)) {
      actuales.delete(idServicio);
    } else {
      actuales.add(idServicio);
    }
    this.serviciosSeleccionados.set(actuales);
  }

  alternarFiltros(): void {
    this.filtrosAbiertos.update((abierto) => !abierto);
  }

  limpiarFiltros(): void {
    this.texto.set('');
    this.precioMin.set(null);
    this.precioMax.set(null);
    this.capacidadMin.set(null);
    this.serviciosSeleccionados.set(new Set());
  }

  /// Un input numerico vaciado por el usuario llega como null o como ''.
  private tieneValor(valor: number | null | string): valor is number {
    return valor !== null && valor !== '';
  }

  private cargarServiciosDeTodos(lista: Alojamiento[]): void {
    if (lista.length === 0) {
      this.serviciosListos.set(true);
      return;
    }
    // Si falla uno, ese alojamiento queda "sin servicios" en vez de trabar todo.
    const llamadas = lista.map((a) =>
      this.alojamientoService.listarServicios(a.idAlojamiento).pipe(
        map((servicios) => [a.idAlojamiento, servicios.map((s) => s.servicio.idServicio)] as const),
        catchError(() => of([a.idAlojamiento, [] as number[]] as const)),
      ),
    );
    forkJoin(llamadas).subscribe((pares) => {
      this.serviciosPorAlojamiento.set(new Map(pares));
      this.serviciosListos.set(true);
    });
  }
}
