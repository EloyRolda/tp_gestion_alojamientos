import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, map } from 'rxjs';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { Servicio } from '../../../core/models/servicio.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { AlojamientoCard } from '../../../shared/alojamiento-card/alojamiento-card';
import { MapaAlojamientos } from '../../../shared/mapa-alojamientos/mapa-alojamientos';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { Vacio } from '../../../shared/vacio/vacio';

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
/// Modo mapa (SCRUM-66): el componente del mapa va en un bloque @defer, asi que
/// Leaflet no pesa en la pagina de quien solo usa la lista.
@Component({
  selector: 'app-busqueda',
  standalone: true,
  imports: [FormsModule, AlojamientoCard, MapaAlojamientos, Esqueleto, Vacio],
  templateUrl: './busqueda.html',
  styleUrl: './busqueda.css',
})
export class Busqueda {
  private readonly alojamientoService = inject(AlojamientoService);
  private readonly catalogoService = inject(CatalogoService);

  private readonly todos = signal<Alojamiento[]>([]);
  private readonly serviciosPorAlojamiento = signal<Map<number, number[]>>(new Map());

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
    const texto = this.texto().trim().toLowerCase();
    const precioMin = this.tieneValor(this.precioMin()) ? this.precioMin() : null;
    const precioMax = this.tieneValor(this.precioMax()) ? this.precioMax() : null;
    const capacidadMin = this.tieneValor(this.capacidadMin()) ? this.capacidadMin() : null;
    const serviciosSel = this.serviciosSeleccionados();
    const mapaServicios = this.serviciosPorAlojamiento();

    return this.todos().filter((a) => {
      if (texto) {
        const haystack = `${a.titulo} ${a.direccion.ciudad} ${a.direccion.provincia}`.toLowerCase();
        if (!haystack.includes(texto)) return false;
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
    if (lista.length === 0) return;
    const llamadas = lista.map((a) =>
      this.alojamientoService
        .listarServicios(a.idAlojamiento)
        .pipe(map((servicios) => [a.idAlojamiento, servicios.map((s) => s.servicio.idServicio)] as const)),
    );
    forkJoin(llamadas).subscribe((pares) => this.serviciosPorAlojamiento.set(new Map(pares)));
  }
}
