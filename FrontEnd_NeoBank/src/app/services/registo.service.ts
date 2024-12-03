import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistoService {

  private apiUrl = 'http://localhost:8080/utilizador/register';  

  constructor(private http: HttpClient) {}

  
  register(user: any): Observable<any> {
    return this.http.post(this.apiUrl, user);
  }
}
