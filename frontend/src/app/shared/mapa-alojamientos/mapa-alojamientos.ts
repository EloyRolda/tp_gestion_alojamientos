import {
  Component,
  DestroyRef,
  ElementRef,
  ViewEncapsulation,
  afterNextRender,
  computed,
  effect,
  inject,
  input,
  signal,
  viewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import * as L from 'leaflet';
import { Alojamiento } from '../../core/models/alojamiento.model';
import { miniatura } from '../../core/utils/imagen.util';

/// Vista de todo el pais: vista inicial cuando ningun alojamiento tiene ubicacion.
const CENTRO_PAIS: L.LatLngTuple = [-38.4, -63.6];
const ZOOM_PAIS = 4;

const pesos = new Intl.NumberFormat('es-AR', { maximumFractionDigits: 0 });

/// Desde este zoom se ven los precios; mas lejos, cada alojamiento es un punto
/// (con muchos resultados, los precios se pisarian entre si).
const ZOOM_PRECIOS = 15;

/// Mapa de resultados de la busqueda (SCRUM-66). Usa Leaflet con el mapa de
/// OpenStreetMap (gratis, sin clave). Cada alojamiento con ubicacion aparece
/// como una etiqueta con su precio (o un punto, si el mapa esta alejado); al
/// tocarla se abre una ficha con la foto,
/// el nombre, el precio y un link al detalle.
///
/// Este componente se carga con @defer desde la busqueda: Leaflet (y sus
/// estilos) solo se descargan cuando alguien elige ver el mapa.
@Component({
  selector: 'app-mapa-alojamientos',
  standalone: true,
  // Sin encapsulacion: las etiquetas y fichas las crea Leaflet fuera de la
  // plantilla de Angular, asi que sus estilos tienen que ser globales.
  encapsulation: ViewEncapsulation.None,
  templateUrl: './mapa-alojamientos.html',
  styleUrls: ['../../../../node_modules/leaflet/dist/leaflet.css', './mapa-alojamientos.css'],
})
export class MapaAlojamientos {
  private readonly router = inject(Router);
  private readonly contenedor = viewChild.required<ElementRef<HTMLDivElement>>('contenedor');

  readonly alojamientos = input.required<Alojamiento[]>();

  readonly conUbicacion = computed(() =>
    this.alojamientos().filter((a) => a.direccion.latitud != null && a.direccion.longitud != null),
  );
  readonly sinUbicacion = computed(() => this.alojamientos().length - this.conUbicacion().length);

  private mapa: L.Map | null = null;
  private capaMarcadores: L.LayerGroup | null = null;
  private readonly listo = signal(false);

  constructor() {
    afterNextRender(() => this.crearMapa());

    effect(() => {
      const lista = this.conUbicacion();
      if (this.listo()) {
        this.pintarMarcadores(lista);
      }
    });

    inject(DestroyRef).onDestroy(() => this.mapa?.remove());
  }

  private crearMapa(): void {
    this.mapa = L.map(this.contenedor().nativeElement, { scrollWheelZoom: true, wheelPxPerZoomLevel: 90 });
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; colaboradores de <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(this.mapa);
    this.capaMarcadores = L.layerGroup().addTo(this.mapa);
    const mapa = this.mapa;
    const actualizarDetalle = () =>
      mapa.getContainer().classList.toggle('mapa--lejos', mapa.getZoom() < ZOOM_PRECIOS);
    mapa.on('zoomend', actualizarDetalle);
    this.mapa.setView(CENTRO_PAIS, ZOOM_PAIS);
    actualizarDetalle();
    this.listo.set(true);
  }

  private pintarMarcadores(lista: Alojamiento[]): void {
    const mapa = this.mapa;
    const capa = this.capaMarcadores;
    if (!mapa || !capa) return;
    capa.clearLayers();

    const puntos: L.LatLngTuple[] = [];
    for (const a of lista) {
      const punto: L.LatLngTuple = [a.direccion.latitud!, a.direccion.longitud!];
      puntos.push(punto);
      const etiqueta = document.createElement('span');
      etiqueta.className = 'marcador-precio';
      etiqueta.textContent = '$' + pesos.format(a.precioNoche);
      const icono = L.divIcon({ className: 'marcador', html: etiqueta, iconSize: [0, 0] });
      L.marker(punto, { icon: icono, title: a.titulo, riseOnHover: true, keyboard: true })
        .bindPopup(() => this.armarFicha(a), { autoPanPadding: [20, 20] })
        .addTo(capa);
    }

    // Encuadra todos los resultados; con uno solo, un zoom de barrio.
    if (puntos.length === 1) {
      mapa.setView(puntos[0], 15);
    } else if (puntos.length > 1) {
      mapa.fitBounds(L.latLngBounds(puntos), { padding: [40, 40], maxZoom: 15 });
    }
  }

  /// La ficha se arma con nodos del DOM (y no con HTML en texto) para que un
  /// titulo con caracteres especiales nunca se interprete como codigo.
  private armarFicha(a: Alojamiento): HTMLElement {
    const ficha = document.createElement('a');
    ficha.className = 'ficha-mapa';
    ficha.href = '/alojamientos/' + a.idAlojamiento;
    ficha.addEventListener('click', (evento) => {
      evento.preventDefault();
      this.router.navigate(['/alojamientos', a.idAlojamiento]);
    });

    const foto = miniatura(a.imagenPrincipal, 480);
    if (foto) {
      const img = document.createElement('img');
      img.src = foto;
      img.alt = '';
      img.width = 240;
      img.height = 180;
      ficha.appendChild(img);
    }

    const cuerpo = document.createElement('span');
    cuerpo.className = 'ficha-mapa__cuerpo';
    const titulo = document.createElement('strong');
    titulo.textContent = a.titulo;
    const lugar = document.createElement('span');
    lugar.className = 'ficha-mapa__lugar';
    lugar.textContent = `${a.direccion.ciudad} · hasta ${a.capacidad} ${a.capacidad === 1 ? 'huésped' : 'huéspedes'}`;
    const precio = document.createElement('span');
    precio.className = 'ficha-mapa__precio';
    precio.textContent = '$' + pesos.format(a.precioNoche) + ' ';
    const noche = document.createElement('span');
    noche.className = 'ficha-mapa__lugar';
    noche.textContent = '/ noche';
    precio.appendChild(noche);
    cuerpo.append(titulo, lugar, precio);
    ficha.appendChild(cuerpo);
    return ficha;
  }
}
