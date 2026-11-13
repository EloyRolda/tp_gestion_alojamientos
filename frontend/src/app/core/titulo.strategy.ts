import { Injectable, inject } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { RouterStateSnapshot, TitleStrategy } from '@angular/router';

/// Titulo de la pestaña del navegador (y de los resultados de los buscadores)
/// para cada pantalla (SCRUM-69): "Mis reservas · AlojaME". El texto sale del
/// campo "title" de cada ruta; el inicio usa el titulo general del sitio.
@Injectable({ providedIn: 'root' })
export class TituloAlojaMe extends TitleStrategy {
  private readonly titulo = inject(Title);

  override updateTitle(estado: RouterStateSnapshot): void {
    const propio = this.buildTitle(estado);
    this.titulo.setTitle(propio ? `${propio} · AlojaME` : 'AlojaME · Alojamientos temporarios');
  }
}
