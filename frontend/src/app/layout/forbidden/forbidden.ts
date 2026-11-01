import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/// 403 -- adonde redirige roleGuard cuando hay sesion pero el rol no alcanza.
@Component({
  selector: 'app-forbidden',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './forbidden.html',
  styleUrl: './forbidden.css',
})
export class Forbidden {}
