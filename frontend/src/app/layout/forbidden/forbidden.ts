import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Kaza } from '../../shared/kaza/kaza';

/// 403 -- adonde redirige roleGuard cuando hay sesion pero el rol no alcanza
/// (por ejemplo, un administrador que entra a "Reservar"). Kaza niega con la
/// cabeza y ofrece volver al inicio.
@Component({
  selector: 'app-forbidden',
  standalone: true,
  imports: [RouterLink, Kaza],
  templateUrl: './forbidden.html',
  styleUrl: './forbidden.css',
})
export class Forbidden {}
