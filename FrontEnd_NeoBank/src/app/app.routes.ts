import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegistoComponent } from './components/registo/registo.component';
import { PasswordResetComponent } from './components/password-reset/password-reset.component';
import { PrincipalComponent } from './components/principal/principal.component';
import { TransacaoComponent } from './components/transacao/transacao.component';

export const routes: Routes = [
    { path: '', redirectTo: 'principal', pathMatch: 'full' },
    { path: 'principal', component:PrincipalComponent},
    { path: 'login', component: LoginComponent },         
    { path: 'registo', component:RegistoComponent },
    { path: 'password-reset', component:PasswordResetComponent},
    {path: 'transacao', component:TransacaoComponent},
    { path: '**', redirectTo: '' }                 
     

  ];