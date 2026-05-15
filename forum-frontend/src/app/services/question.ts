import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Question {
  id?: number;
  title: string;
  text: string;
  imageUrl?: string;
  status?: string;
  createdAt?: string;
  author?: any;
  tags?: any[];
  answers?: any[];
  voteScore?: number;
}

//poate fi folosit (injectat) oriunde in aplicatie
@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private apiUrl = 'http://localhost:8080/question';

  //http client face post get
  constructor(private http: HttpClient) {}

  getAllQuestions(): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.apiUrl}/getAll`);
  }

  getQuestionById(id: number): Observable<Question> {
    return this.http.get<Question>(`${this.apiUrl}/getById/${id}`);
  }

  addQuestion(question: Question): Observable<Question> {
    return this.http.post<Question>(`${this.apiUrl}/add`, question);
  }

  updateQuestion(id: number, question: Question): Observable<Question> {
    return this.http.put<Question>(`${this.apiUrl}/update/${id}`, question);
  }

  deleteQuestion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }


  voteQuestion(questionId: number, userId: number, voteType: string): Observable<number> {
    return this.http.post<number>(
      `http://localhost:8080/vote/question/${questionId}?userId=${userId}&voteType=${voteType}`,
      {}
    );
  }
}
