import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/// Pagina institucional (SCRUM-64): que es AlojaME, como funciona en 3 pasos y
/// quien lo desarrolla.
@Component({
  selector: 'app-sobre-nosotros',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './sobre-nosotros.html',
  styleUrl: './sobre-nosotros.css',
})
export class SobreNosotros {}
