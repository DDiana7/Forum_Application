import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Answer {
  id?: number;
  text: string;
  imageUrl?: string;
  createdAt?: string;
  author?: any;
  question?: any;
  voteScore?: number;

}

@Injectable({
  providedIn: 'root'
})
export class AnswerService {

  private apiUrl = 'http://localhost:8080/answer';

  constructor(private http: HttpClient) {}

  getAnswersByQuestionId(questionId: number): Observable<Answer[]> {
    return this.http.get<Answer[]>(`${this.apiUrl}/question/${questionId}`);
  }

  addAnswer(answer: Answer): Observable<Answer> {
    return this.http.post<Answer>(`${this.apiUrl}/add`, answer);
  }

  updateAnswer(id: number, answer: Answer): Observable<Answer> {
    return this.http.put<Answer>(`${this.apiUrl}/update/${id}`, answer);
  }

  deleteAnswer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }



  voteAnswer(answerId: number, userId: number, voteType: string): Observable<number> {
    return this.http.post<number>(
      `http://localhost:8080/vote/answer/${answerId}?userId=${userId}&voteType=${voteType}`,
      {}
    );
  }


  acceptAnswer(answerId: number, userId: number): Observable<any> {
    return this.http.post(
      `http://localhost:8080/answer/accept/${answerId}?userId=${userId}`,
      {}
    );
  }
}
