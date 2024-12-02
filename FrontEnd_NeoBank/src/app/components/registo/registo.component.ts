import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';


@Component({
  selector: 'app-registo',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './registo.component.html',
  styleUrl: './registo.component.css'
})
export class RegistoComponent {
  username = '';
  email = '';
  password = '';
  confirmPassword = '';
  phone = '';
  nif = '';
  address = '';
  passwordStrengthMessage = '';
  passwordStrengthClass = '';
  passwordMatchMessage = '';
  passwordMatchClass = '';

  constructor(private router: Router) {}

  checkPasswordStrength() {
    const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
    if (strongRegex.test(this.password)) {
      this.passwordStrengthMessage = 'Senha forte';
      this.passwordStrengthClass = 'text-success';
    } else {
      this.passwordStrengthMessage = 'Senha deve conter letras maiúsculas, números e símbolos.';
      this.passwordStrengthClass = 'text-danger';
    }
  }

  checkPasswordMatch() {
    if (this.password === this.confirmPassword) {
      this.passwordMatchMessage = 'Senhas coincidem';
      this.passwordMatchClass = 'text-success';
    } else {
      this.passwordMatchMessage = 'Senhas não coincidem';
      this.passwordMatchClass = 'text-danger';
    }
  }

  isFormValid() {
    return (
      this.passwordStrengthClass === 'text-success' &&
      this.passwordMatchClass === 'text-success' &&
      this.username &&
      this.email &&
      this.phone &&
      this.nif &&
      this.address
    );
  }

  onSubmit() {
    if (this.isFormValid()) {
      console.log('Form submitted:', { username: this.username, email: this.email, phone: this.phone });
      // Add API call or further logic here
    
      this.router.navigate(['/principal']);
    }
  }
}
