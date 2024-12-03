import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, RouterLinkActive } from '@angular/router';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  credentials = { identifier: '', password: '' };
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        if (response.success) {
          this.authService.saveToken(response.data.token);
          this.router.navigate(['/dashboard']); // Redirect to a secured route
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
