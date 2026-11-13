import { Signal, effect, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/// Pantallas de edicion del anfitrion (editar alojamiento, galeria): si el
/// alojamiento cargado es de otro anfitrion, se manda a la 403 en vez de
/// mostrar un formulario que el backend despues rechaza al guardar.
/// Se llama desde el constructor del componente (usa inject y effect).
export function exigirDuenio(idDuenio: Signal<number | null>): void {
  const auth = inject(AuthService);
  const router = inject(Router);
  effect(() => {
    const duenio = idDuenio();
    const yo = auth.usuario()?.id;
    if (duenio != null && yo != null && duenio !== yo) {
      router.navigate(['/403'], { replaceUrl: true });
    }
  });
}
