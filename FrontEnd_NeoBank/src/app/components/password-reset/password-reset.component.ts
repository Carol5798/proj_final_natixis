import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './password-reset.component.html',
  styleUrl: './password-reset.component.css'
})
export class PasswordResetComponent {

  email: string = '';
  newPassword: string = '';
  confirmNewPassword: string = '';
  passwordStrengthMessage: string = '';
  passwordStrengthClass: string = '';
  passwordMatchMessage: string = '';
  passwordMatchClass: string = '';

  constructor(private router: Router) {} 

  // Check password strength
  checkPasswordStrength() {
    const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    if (strongRegex.test(this.newPassword)) {
      this.passwordStrengthMessage = 'Senha forte';
      this.passwordStrengthClass = 'text-success';
    } else {
      this.passwordStrengthMessage =
        'Senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e símbolos.';
      this.passwordStrengthClass = 'text-danger';
    }
  }

  // Check if passwords match
  checkPasswordMatch() {
    if (this.newPassword && this.confirmNewPassword && this.newPassword === this.confirmNewPassword) {
      this.passwordMatchMessage = 'Senhas coincidem';
      this.passwordMatchClass = 'text-success';
    } else {
      this.passwordMatchMessage = 'Senhas não coincidem';
      this.passwordMatchClass = 'text-danger';
    }
  }

  // Check if form is valid
  isFormValid() {
    return (
      this.passwordStrengthClass === 'text-success' &&
      this.passwordMatchClass === 'text-success' &&
      this.email.length > 0
    );
  }

// Submit function with redirection
onSubmit() {
  if (this.isFormValid()) {
    console.log('Password reset submitted', { email: this.email, newPassword: this.newPassword });
    // Add actual logic for resetting the password, e.g., API call.

    // Redirect to the principal page after successful submission
    this.router.navigate(['/principal']);
  }
}

}
