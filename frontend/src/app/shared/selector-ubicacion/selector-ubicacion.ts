import {
  Component,
  DestroyRef,
  ElementRef,
  ViewEncapsulation,
  afterNextRender,
  effect,
  inject,
  input,
  model,
  signal,
  viewChild,
} from '@angular/core';
import * as L from 'leaflet';

export interface Punto {
  latitud: number;
  longitud: number;
}

/// Centro de Mar del Plata: vista inicial mientras no haya un punto marcado.
const CENTRO_MDP: L.LatLngTuple = [-38.004, -57.548];

/// Nominatim (el buscador de direcciones de OpenStreetMap) pide no hacer mas de
/// un pedido por segundo y nunca buscar mientras se escribe: por eso se busca
/// solo con el boton, de a un pedido por vez y con una espera minima entre dos.
const ESPERA_ENTRE_BUSQUEDAS_MS = 1500;

/// Marcar en un mapa donde queda un alojamiento (SCRUM-67), al publicarlo o al
/// editarlo. Se toca el mapa para poner el punto y se lo arrastra para moverlo.
/// "Buscar en el mapa" ubica la direccion cargada en el formulario.
///
/// Se carga con @defer desde el formulario: Leaflet solo se descarga si el
/// anfitrion llega a esta parte del formulario.
@Component({
  selector: 'app-selector-ubicacion',
  standalone: true,
  // Sin encapsulacion: el pin lo crea Leaflet fuera de la plantilla de Angular.
  encapsulation: ViewEncapsulation.None,
  templateUrl: './selector-ubicacion.html',
  styleUrls: ['../../../../node_modules/leaflet/dist/leaflet.css', './selector-ubicacion.css'],
})
export class SelectorUbicacion {
  private readonly contenedor = viewChild.required<ElementRef<HTMLDivElement>>('contenedor');

  /// Punto elegido (two-way binding con el formulario).
  readonly punto = model<Punto | null>(null);
  /// Direccion escrita en el formulario, para el boton "Buscar en el mapa".
  readonly direccionTexto = input<string>('');
  /// En edicion el backend no permite borrar el punto, solo moverlo.
  readonly permiteQuitar = input(true);

  readonly buscando = signal(false);
  readonly mensaje = signal<string | null>(null);

  private mapa: L.Map | null = null;
  private marcador: L.Marker | null = null;
  private ultimaBusqueda = 0;

  constructor() {
    afterNextRender(() => this.crearMapa());

    // Si el punto cambia desde afuera (o al cargar la edicion), se refleja en el mapa.
    effect(() => {
      const p = this.punto();
      if (this.mapa) {
        this.dibujarMarcador(p);
      }
    });

    inject(DestroyRef).onDestroy(() => this.mapa?.remove());
  }

  async buscarDireccion(): Promise<void> {
    const texto = this.direccionTexto().trim();
    if (!texto) {
      this.mensaje.set('Completá la calle, la altura y la ciudad para buscarla en el mapa.');
      return;
    }
    const espera = this.ultimaBusqueda + ESPERA_ENTRE_BUSQUEDAS_MS - Date.now();
    if (this.buscando() || espera > 0) return;

    this.buscando.set(true);
    this.mensaje.set(null);
    this.ultimaBusqueda = Date.now();
    try {
      // fetch directo (y no HttpClient): asi el pedido a OpenStreetMap nunca
      // lleva el token de sesion que el interceptor agrega a la API propia.
      const url =
        'https://nominatim.openstreetmap.org/search?format=jsonv2&limit=1&countrycodes=ar&accept-language=es&q=' +
        encodeURIComponent(texto);
      const respuesta = await fetch(url, { headers: { Accept: 'application/json' } });
      if (!respuesta.ok) throw new Error(String(respuesta.status));
      const resultados: { lat: string; lon: string }[] = await respuesta.json();
      if (resultados.length === 0) {
        this.mensaje.set('No encontramos esa dirección. Marcá el punto tocando el mapa.');
        return;
      }
      const encontrado = { latitud: Number(resultados[0].lat), longitud: Number(resultados[0].lon) };
      this.punto.set(this.redondear(encontrado));
      this.mapa?.setView([encontrado.latitud, encontrado.longitud], 16);
      this.mensaje.set('Revisá que el punto quede sobre el alojamiento; si no, arrastralo.');
    } catch {
      this.mensaje.set('No se pudo buscar la dirección ahora. Marcá el punto tocando el mapa.');
    } finally {
      this.buscando.set(false);
    }
  }

  quitarPunto(): void {
    this.punto.set(null);
    this.mensaje.set(null);
  }

  private crearMapa(): void {
    const inicial = this.punto();
    this.mapa = L.map(this.contenedor().nativeElement, { scrollWheelZoom: true, wheelPxPerZoomLevel: 90 });
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; colaboradores de <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(this.mapa);
    this.mapa.setView(inicial ? [inicial.latitud, inicial.longitud] : CENTRO_MDP, inicial ? 16 : 13);
    this.mapa.on('click', (e: L.LeafletMouseEvent) =>
      this.punto.set(this.redondear({ latitud: e.latlng.lat, longitud: e.latlng.lng })),
    );
    this.dibujarMarcador(inicial);
  }

  private dibujarMarcador(p: Punto | null): void {
    if (!this.mapa) return;
    if (!p) {
      this.marcador?.remove();
      this.marcador = null;
      return;
    }
    const posicion: L.LatLngTuple = [p.latitud, p.longitud];
    if (this.marcador) {
      this.marcador.setLatLng(posicion);
      return;
    }
    const pin = L.divIcon({ className: 'pin-ubicacion', html: '<span></span>', iconSize: [28, 28], iconAnchor: [14, 34] });
    this.marcador = L.marker(posicion, { icon: pin, draggable: true, title: 'Ubicación del alojamiento' }).addTo(this.mapa);
    this.marcador.on('dragend', () => {
      const ll = this.marcador!.getLatLng();
      this.punto.set(this.redondear({ latitud: ll.lat, longitud: ll.lng }));
    });
  }

  /// 6 decimales alcanzan para ubicar una puerta (unos 10 cm).
  private redondear(p: Punto): Punto {
    return { latitud: Number(p.latitud.toFixed(6)), longitud: Number(p.longitud.toFixed(6)) };
  }
}
