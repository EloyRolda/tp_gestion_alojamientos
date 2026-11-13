import { Component, inject } from '@angular/core';
import { ViewportScroller } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './layout/navbar/navbar';
import { Footer } from './layout/footer/footer';
import { Confirmacion } from './shared/confirmacion/confirmacion';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, Footer, Confirmacion],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  constructor() {
    // El encabezado es fijo: al saltar a una seccion con # (links del pie), el
    // titulo queda justo debajo del encabezado en vez de tapado por el.
    inject(ViewportScroller).setOffset(() => {
      const alto = document.querySelector('.topbar')?.getBoundingClientRect().height ?? 0;
      return [0, Math.ceil(alto) + 16];
    });
  }
}
