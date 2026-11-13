import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, forkJoin, map, of, switchMap } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Alojamiento, AlojamientoModificar, AlojamientoRegistro } from '../models/alojamiento.model';
import { ServicioxAlojamiento } from '../models/servicio.model';

/// Lectura publica (backend: GET /Alojamiento/listar y /mostrar/{id} son permitAll).
/// No hay query params de filtro en el backend -- la busqueda (RF39) se filtra
/// del lado del cliente sobre esta misma lista, ver BusquedaComponent. Las
/// operaciones de escritura (registrar/actualizar/propios/servicios) exigen
/// sesion de ANFITRION (o ADMIN) -- ver SecurityConfig.
@Injectable({ providedIn: 'root' })
export class AlojamientoService {
  private readonly http = inject(HttpClient);

  listar(): Observable<Alojamiento[]> {
    return this.http.get<Alojamiento[]>(`${API_BASE_URL}/Alojamiento/listar`);
  }

  obtener(id: number): Observable<Alojamiento> {
    return this.http.get<Alojamiento>(`${API_BASE_URL}/Alojamiento/mostrar/${id}`);
  }

  /// Alojamiento completo (con su anfitrion y si esta publicado) de cada id
  /// pedido, incluidos los pausados. El listado publico solo trae los
  /// publicados, asi que los que falten se consultan de a uno (suelen ser
  /// pocos). Si alguno no se puede consultar, simplemente no esta en el mapa.
  porIds(ids: number[]): Observable<Map<number, Alojamiento>> {
    const pedidos = new Set(ids);
    if (pedidos.size === 0) return of(new Map());
    return this.listar().pipe(
      switchMap((publicados) => {
        const mapa = new Map(publicados.filter((a) => pedidos.has(a.idAlojamiento)).map((a) => [a.idAlojamiento, a] as const));
        const faltan = [...pedidos].filter((id) => !mapa.has(id));
        if (faltan.length === 0) return of(mapa);
        return forkJoin(faltan.map((id) => this.obtener(id).pipe(catchError(() => of(null))))).pipe(
          map((extra) => {
            for (const a of extra) if (a) mapa.set(a.idAlojamiento, a);
            return mapa;
          }),
        );
      }),
    );
  }

  listarPropios(): Observable<Alojamiento[]> {
    return this.http.get<Alojamiento[]>(`${API_BASE_URL}/Alojamiento/listar/propios`);
  }

  registrar(dto: AlojamientoRegistro): Observable<Alojamiento> {
    return this.http.post<Alojamiento>(`${API_BASE_URL}/Alojamiento/registrar`, dto);
  }

  actualizar(dto: AlojamientoModificar): Observable<Alojamiento> {
    return this.http.put<Alojamiento>(`${API_BASE_URL}/Alojamiento/actualizar`, dto);
  }

  listarServicios(idAlojamiento: number): Observable<ServicioxAlojamiento[]> {
    return this.http.get<ServicioxAlojamiento[]>(`${API_BASE_URL}/Alojamiento/${idAlojamiento}/servicios`);
  }

  agregarServicios(idAlojamiento: number, nombresServicios: string[]): Observable<void> {
    return this.http.post<void>(`${API_BASE_URL}/Alojamiento/${idAlojamiento}/servicios`, nombresServicios);
  }

  /// idServicio es el id del catalogo (Servicio.idServicio), no el de la fila
  /// ServicioxAlojamiento -- ver ServicioxAlojamientoService.quitarServicio en el backend.
  quitarServicio(idAlojamiento: number, idServicio: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/Alojamiento/${idAlojamiento}/servicios/${idServicio}`);
  }
}
