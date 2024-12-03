import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private apiUrl = 'http://localhost:8080/utilizador/login';

  constructor(private http: HttpClient) {}

  login(credentials: { emailOrNif: string; password: string }): Observable<any> {
    return this.http.post(this.apiUrl, credentials);
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token); // Store token in localStorage
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token'); // Clear the token
  }

  isLoggedIn(): boolean {
    return !!this.getToken(); // Check if a token exists
  }
}
