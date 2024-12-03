import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PrincipalComponent } from '../principal/principal.component';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthServiceService } from '../../services/auth-service.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink, RouterLinkActive, PrincipalComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  credentials = { emailOrNif: '', password: '' };
  errorMessage = '';

  constructor(private authService: AuthServiceService, private router: Router) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        if (response.success) {
          this.authService.saveToken(response.data.token);
          this.router.navigate(['/principal']); // Redirect to a secured route
        } else {
          this.handleError(response.message);
        }
      },
      error: (err) => {
        if (err.error?.message) {
          this.handleError(err.error.message); // Handle API error messages
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      },
    });
  }

  handleError(message: string): void {
    if (message === 'Credenciais inválidas') {
      this.errorMessage = 'Invalid credentials. Please check your NIF/Email and password.';
    } else {
      this.errorMessage = message || 'An unknown error occurred.';
    }
  }
}
