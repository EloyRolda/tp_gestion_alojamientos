import { Signal, computed, effect, signal, untracked } from '@angular/core';

/// Paginado en memoria de un listado ya filtrado, para usar con <app-paginador>.
/// Devuelve la pagina y el tamaño elegidos (signals que el paginador modifica)
/// y la porcion visible de la lista. Cuando cambia lo que devuelve "reiniciarCon"
/// (la busqueda, el filtro de estado, etc.) se vuelve a la primera pagina.
///
///   readonly paginado = crearPaginado(this.reservasFiltradas, () => [this.busqueda(), this.filtro()]);
///   @for (r of paginado.visibles(); track r.id) { ... }
///
/// Hay que llamarlo al declarar el campo (contexto de inyeccion, por el effect).
export function crearPaginado<T>(lista: Signal<T[]>, reiniciarCon?: () => unknown, tamanioInicial = 10) {
  const pagina = signal(1);
  const porPagina = signal(tamanioInicial);
  const visibles = computed(() => {
    const items = lista();
    const tam = porPagina();
    const ultima = Math.max(1, Math.ceil(items.length / tam));
    const actual = Math.min(Math.max(1, pagina()), ultima);
    return items.slice((actual - 1) * tam, actual * tam);
  });
  if (reiniciarCon) {
    effect(() => {
      reiniciarCon();
      untracked(() => pagina.set(1));
    });
  }
  return { pagina, porPagina, visibles };
}
