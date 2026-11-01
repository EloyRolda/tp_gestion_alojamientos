import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/// 404 -- ruta sin match (wildcard ** en app.routes.ts).
@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound {}
