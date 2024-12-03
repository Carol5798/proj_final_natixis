import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ListaComponent } from '../lista/lista.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-principal',
  standalone: true,
  imports: [ListaComponent, HeaderComponent],
  templateUrl: './principal.component.html',
  styleUrl: './principal.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PrincipalComponent {

}
