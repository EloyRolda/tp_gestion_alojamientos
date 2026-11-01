import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin, map } from 'rxjs';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { Alojamiento } from '../../../core/models/alojamiento.model';
import { Servicio } from '../../../core/models/servicio.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import { AlojamientoCard } from '../../../shared/alojamiento-card/alojamiento-card';

/// SCRUM-23 / RF14, RF39. El backend no expone query params de filtro
/// (/Alojamiento/listar siempre devuelve todo), asi que el filtro es
/// client-side sobre la lista completa -- decision tomada y documentada en
/// PLAN_LOTES_FRONTEND.md. Para poder filtrar por servicio, se trae una vez
/// (en paralelo, al cargar la pagina) que servicios tiene cada alojamiento;
/// de ahi en mas filtrar es instantaneo, sin pegarle de nuevo a la API.
@Component({
  selector: 'app-busqueda',
  standalone: true,
  imports: [FormsModule, AlojamientoCard],
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
  readonly precioMin = signal<number | null>(null);
  readonly precioMax = signal<number | null>(null);
  readonly capacidadMin = signal<number | null>(null);
  readonly serviciosSeleccionados = signal<Set<number>>(new Set());

  readonly resultados = computed<Alojamiento[]>(() => {
    const texto = this.texto().trim().toLowerCase();
    const precioMin = this.precioMin();
    const precioMax = this.precioMax();
    const capacidadMin = this.capacidadMin();
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

  limpiarFiltros(): void {
    this.texto.set('');
    this.precioMin.set(null);
    this.precioMax.set(null);
    this.capacidadMin.set(null);
    this.serviciosSeleccionados.set(new Set());
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
