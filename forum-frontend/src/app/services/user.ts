import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
//import {response} from 'express';

export interface User{
  id?:number;
  email: string;
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})

export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/users';

  register(user: User): Observable<User> {
    return this.http.post<User>(
      `${this.apiUrl}/register`,
      user
    );
  }

  login(user: any): Observable<User> {
    return this.http.post<User>(
      `${this.apiUrl}/login`,
      user
    );
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }
}
