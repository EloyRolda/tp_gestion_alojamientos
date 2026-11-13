import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Kaza } from '../../shared/kaza/kaza';

/// 404 -- ruta sin match (wildcard ** en app.routes.ts). Mismo formato que la 403.
@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [RouterLink, Kaza],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound {}
