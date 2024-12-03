import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { RegistoService } from '../../services/registo.service';


@Component({
  selector: 'app-registo',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './registo.component.html',
  styleUrl: './registo.component.css'
})

export class RegisterComponent {
  user = {
    nome: '',
    dataNascimento: '',
    nif: '',
    username: '',
    password: '',
    confirmPassword: '',  // Keep this for validation only
    email: '',
    numeroTelemovel: '',
    tipoUtilizador: 1, // Default to "Normal"
  };

  passwordStrengthMessage: string = '';
  passwordStrengthClass: string = '';
  passwordMatchMessage: string = '';
  passwordMatchClass: string = '';
  registrationError: string = '';  // To display registration error message

  constructor(private registoService: RegistoService, private router: Router) {}

  // Method to handle the form submission
  onSubmit(): void {
    if (this.isFormValid()) {
      // Create a new object without the confirmPassword field
      const { confirmPassword, ...userToSend } = this.user;  // Destructure to remove confirmPassword

      this.registoService.register(userToSend).subscribe({
        next: (response) => {
          if (response.success) {
            this.router.navigate(['/login']);  // Redirect to login page on successful registration
          } else {
            this.registrationError = response.message;  // Handle backend failure message
          }
        },
        error: (error) => {
          this.registrationError = 'Error during registration, please try again later.';  // Handle any errors from the API
          console.error('Registration error: ', error);
        }
      });
    } else {
      this.registrationError = 'Please fill in all required fields correctly.';
    }
  }

  // Method to check password strength (optional)
  checkPasswordStrength() {
    const password = this.user.password;
    // Add your password strength logic here
    if (password.length < 8) {
      this.passwordStrengthMessage = 'Password is too weak';
      this.passwordStrengthClass = 'text-danger';
    } else {
      this.passwordStrengthMessage = 'Password strength is good';
      this.passwordStrengthClass = 'text-success';
    }
  }

  // Method to check password match
  checkPasswordMatch() {
    if (this.user.password !== this.user.confirmPassword) {
      this.passwordMatchMessage = 'Passwords do not match';
      this.passwordMatchClass = 'text-danger';
    } else {
      this.passwordMatchMessage = 'Passwords match';
      this.passwordMatchClass = 'text-success';
    }
  }

  // Form validation check (to enable or disable submit button)
  isFormValid() {
    return (
      this.user.nome &&
      this.user.nif &&
      this.user.username &&
      this.user.password &&
      this.user.email &&
      this.user.numeroTelemovel &&
      (this.user.tipoUtilizador === 2 || this.user.dataNascimento) && // Check if "Normal" or "Empresa"
      this.user.password === this.user.confirmPassword
    );
  }

  // Method to change form validation based on tipoUtilizador
  onTipoUtilizadorChange() {
    if (this.user.tipoUtilizador === 2) {
      // If "Empresa", remove the validation for dataNascimento
      this.user.dataNascimento = '';  // Clear the dataNascimento field
    }
  }
}