import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AlojamientoService } from '../../../core/services/alojamiento.service';
import { CatalogoService } from '../../../core/services/catalogo.service';
import { AlojamientoModificar, AlojamientoRegistro } from '../../../core/models/alojamiento.model';
import { DireccionInput } from '../../../core/models/direccion.model';
import { TipoAlojamiento } from '../../../core/models/tipo-alojamiento.model';
import { Servicio, ServicioxAlojamiento } from '../../../core/models/servicio.model';
import { extraerMensajeError } from '../../../core/utils/http-error.util';
import type { Punto } from '../../../shared/selector-ubicacion/selector-ubicacion';
import { SelectorUbicacion } from '../../../shared/selector-ubicacion/selector-ubicacion';
import { Esqueleto } from '../../../shared/esqueleto/esqueleto';
import { AvisoService } from '../../../core/services/aviso.service';
import { exigirDuenio } from '../../../core/utils/duenio.util';

interface FormAlojamiento {
  titulo: string;
  descripcion: string;
  precioNoche: number | null;
  capacidad: number | null;
  cantAmbientes: number | null;
  cantHabitaciones: number | null;
  cantBanios: number | null;
  nombreTipoAlojamiento: string;
}

const FORM_VACIO: FormAlojamiento = {
  titulo: '',
  descripcion: '',
  precioNoche: null,
  capacidad: null,
  cantAmbientes: null,
  cantHabitaciones: null,
  cantBanios: null,
  nombreTipoAlojamiento: '',
};

const DIRECCION_VACIA: DireccionInput = {
  pais: 'Argentina',
  provincia: '',
  codigoPostal: '',
  ciudad: '',
  calle: '',
  altura: 0,
};

/// SCRUM-36 / RF11, RF12, RF17. Un solo componente para alta y edicion: sin
/// "id" (ruta /anfitrion/alojamientos/nuevo) es alta; con "id" (ruta
/// /anfitrion/alojamientos/:id/editar) es edicion. La direccion solo se carga
/// al crear -- el backend no tiene un endpoint para modificarla (AlojamientoModificarDTO
/// no trae campo "direccion", solo "idDireccion" para reemplazarla por otra YA
/// existente, caso de uso que no se ofrece aca) -- en edicion se muestra de solo lectura.
/// Los servicios en alta se mandan embebidos en el alta; en edicion se agregan/quitan
/// al toque contra los endpoints dedicados (la reserva ya existe, no hay "guardar" previo).
///
/// Ubicacion en el mapa (SCRUM-67): opcional. En alta viaja dentro de la
/// direccion; en edicion se puede mover el punto (no la calle) y viaja como
/// latitud/longitud del alojamiento. El mapa se carga con @defer.
@Component({
  selector: 'app-alojamiento-form',
  standalone: true,
  imports: [FormsModule, RouterLink, SelectorUbicacion, Esqueleto],
  templateUrl: './alojamiento-form.html',
  styleUrl: './alojamiento-form.css',
})
export class AlojamientoForm {
  readonly id = input<string>();

  private readonly alojamientoService = inject(AlojamientoService);
  private readonly aviso = inject(AvisoService);
  private readonly catalogoService = inject(CatalogoService);
  private readonly router = inject(Router);
  /// Anfitrion dueño del alojamiento en edicion (para exigirDuenio).
  private readonly idDuenio = signal<number | null>(null);

  readonly esEdicion = computed(() => !!this.id());

  readonly form = signal<FormAlojamiento>({ ...FORM_VACIO });
  readonly direccion = signal<DireccionInput>({ ...DIRECCION_VACIA });
  readonly ubicacion = signal<Punto | null>(null);
  /// En edicion: si el alojamiento ya tenia punto, el backend solo permite moverlo.
  readonly teniaUbicacion = signal(false);

  /// Texto que usa "Buscar en el mapa": calle, altura, ciudad y provincia.
  readonly direccionTexto = computed(() => {
    const d = this.direccion();
    if (!d.calle?.trim() || !d.ciudad?.trim()) return '';
    return [`${d.calle} ${d.altura || ''}`.trim(), d.ciudad, d.provincia, d.pais].filter((x) => x && String(x).trim()).join(', ');
  });
  readonly cargando = signal(true);
  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);

  readonly tipos = signal<TipoAlojamiento[]>([]);
  readonly catalogoServicios = signal<Servicio[]>([]);

  // Alta: servicios elegidos (checkboxes del catalogo) + uno libre a mano.
  readonly serviciosElegidos = signal<Set<string>>(new Set());
  readonly servicioNuevo = signal('');

  // Edicion: servicios ya asignados a este alojamiento + uno libre a agregar.
  readonly serviciosAsignados = signal<ServicioxAlojamiento[]>([]);
  readonly agregandoServicio = signal(false);
  readonly servicioAAgregar = signal('');

  constructor() {
    this.catalogoService.listarTipos().subscribe({ next: (t) => this.tipos.set(t) });
    this.catalogoService.listarServicios().subscribe({ next: (s) => this.catalogoServicios.set(s) });

    exigirDuenio(this.idDuenio);

    effect(() => {
      const idStr = this.id();
      if (!idStr) {
        this.cargando.set(false);
        return;
      }
      const idNumerico = Number(idStr);
      if (!Number.isFinite(idNumerico)) return;
      this.cargarParaEditar(idNumerico);
    });
  }

  actualizarCampo<K extends keyof FormAlojamiento>(campo: K, valor: FormAlojamiento[K]): void {
    this.form.update((actual) => ({ ...actual, [campo]: valor }));
  }

  actualizarDireccion<K extends keyof DireccionInput>(campo: K, valor: DireccionInput[K]): void {
    this.direccion.update((actual) => ({ ...actual, [campo]: valor }));
  }

  toggleServicioElegido(nombre: string): void {
    this.serviciosElegidos.update((actual) => {
      const copia = new Set(actual);
      copia.has(nombre) ? copia.delete(nombre) : copia.add(nombre);
      return copia;
    });
  }

  agregarServicioLibre(): void {
    const nombre = this.servicioNuevo().trim();
    if (!nombre) return;
    this.serviciosElegidos.update((actual) => new Set(actual).add(nombre));
    this.servicioNuevo.set('');
  }

  guardar(): void {
    this.error.set(null);
    this.enviando.set(true);

    if (this.esEdicion()) {
      const f = this.form();
      const dto: AlojamientoModificar = {
        idAlojamiento: Number(this.id()),
        titulo: f.titulo,
        descripcion: f.descripcion,
        precioNoche: f.precioNoche ?? undefined,
        capacidad: f.capacidad ?? undefined,
        cantAmbientes: f.cantAmbientes ?? undefined,
        cantHabitaciones: f.cantHabitaciones ?? undefined,
        cantBanios: f.cantBanios ?? undefined,
        nombreTipoAlojamiento: f.nombreTipoAlojamiento,
        latitud: this.ubicacion()?.latitud,
        longitud: this.ubicacion()?.longitud,
      };
      this.alojamientoService.actualizar(dto).subscribe({
        next: (a) => {
          this.enviando.set(false);
          this.aviso.mostrar('Cambios guardados');
          this.router.navigate(['/anfitrion/alojamientos', a.idAlojamiento, 'editar']);
        },
        error: (err) => {
          this.enviando.set(false);
          this.error.set(extraerMensajeError(err));
        },
      });
    } else {
      const f = this.form();
      const dto: AlojamientoRegistro = {
        titulo: f.titulo,
        descripcion: f.descripcion,
        precioNoche: f.precioNoche!,
        capacidad: f.capacidad!,
        cantAmbientes: f.cantAmbientes!,
        cantHabitaciones: f.cantHabitaciones!,
        cantBanios: f.cantBanios!,
        nombreTipoAlojamiento: f.nombreTipoAlojamiento,
        nombresServicios: [...this.serviciosElegidos()],
        direccion: {
          ...this.direccion(),
          latitud: this.ubicacion()?.latitud ?? null,
          longitud: this.ubicacion()?.longitud ?? null,
        },
      };
      this.alojamientoService.registrar(dto).subscribe({
        next: (a) => {
          this.enviando.set(false);
          this.aviso.mostrar('Alojamiento publicado', ['Ahora sumale fotos: las publicaciones con fotos se eligen mucho más.']);
          this.router.navigate(['/anfitrion/alojamientos', a.idAlojamiento, 'galeria']);
        },
        error: (err) => {
          this.enviando.set(false);
          this.error.set(extraerMensajeError(err));
        },
      });
    }
  }

  agregarServicioEnEdicion(): void {
    const nombre = this.servicioAAgregar().trim();
    const idAlojamiento = Number(this.id());
    if (!nombre || !idAlojamiento) return;

    this.agregandoServicio.set(true);
    this.alojamientoService.agregarServicios(idAlojamiento, [nombre]).subscribe({
      next: () => {
        this.agregandoServicio.set(false);
        this.servicioAAgregar.set('');
        this.recargarServiciosAsignados(idAlojamiento);
      },
      error: (err) => {
        this.agregandoServicio.set(false);
        this.error.set(extraerMensajeError(err));
      },
    });
  }

  quitarServicioEnEdicion(sxa: ServicioxAlojamiento): void {
    const idAlojamiento = Number(this.id());
    if (!idAlojamiento) return;
    this.alojamientoService.quitarServicio(idAlojamiento, sxa.servicio.idServicio).subscribe({
      next: () => this.recargarServiciosAsignados(idAlojamiento),
      error: (err) => this.error.set(extraerMensajeError(err)),
    });
  }

  private cargarParaEditar(idAlojamiento: number): void {
    this.cargando.set(true);
    this.alojamientoService.obtener(idAlojamiento).subscribe({
      next: (a) => {
        this.idDuenio.set(a.anfitrion.id);
        this.form.set({
          titulo: a.titulo,
          descripcion: a.descripcion,
          precioNoche: a.precioNoche,
          capacidad: a.capacidad,
          cantAmbientes: a.cantAmbientes,
          cantHabitaciones: a.cantHabitaciones,
          cantBanios: a.cantBanios,
          nombreTipoAlojamiento: a.tipoAlojamiento.nombreTipo,
        });
        this.direccion.set(a.direccion);
        const tiene = a.direccion.latitud != null && a.direccion.longitud != null;
        this.teniaUbicacion.set(tiene);
        this.ubicacion.set(tiene ? { latitud: a.direccion.latitud!, longitud: a.direccion.longitud! } : null);
        this.cargando.set(false);
        this.recargarServiciosAsignados(idAlojamiento);
      },
      error: (err) => {
        this.error.set(extraerMensajeError(err));
        this.cargando.set(false);
      },
    });
  }

  private recargarServiciosAsignados(idAlojamiento: number): void {
    this.alojamientoService.listarServicios(idAlojamiento).subscribe({ next: (s) => this.serviciosAsignados.set(s) });
  }
}
