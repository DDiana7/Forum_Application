import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Tag {
  id?: number;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private apiUrl = 'http://localhost:8080/tag';

  constructor(private http: HttpClient) {}

  getAllTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${this.apiUrl}/getAll`);
  }

  addTag(tag: Tag): Observable<Tag> {
    return this.http.post<Tag>(`${this.apiUrl}/add`, tag);
  }
}
