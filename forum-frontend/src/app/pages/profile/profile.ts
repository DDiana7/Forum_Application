import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { QuestionService, Question } from '../../services/question';

@Component({
  selector: 'app-profile',
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

  user: any = null;
  questions: Question[] = [];
  myQuestions: Question[] = [];

  constructor(
    private router: Router,
    private questionService: QuestionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const savedUser = localStorage.getItem('user');

    if (!savedUser) {
      this.router.navigate(['']);
      return;
    }

    this.user = JSON.parse(savedUser);

    this.loadQuestions();
  }

  loadQuestions() {
    this.questionService.getAllQuestions().subscribe({
      next: (response) => {
        this.questions = response;

        this.myQuestions = response.filter(question =>
          question.author &&
          (
            Number(question.author.id) === Number(this.user.id) ||
            question.author.username === this.user.username
          )
        );

        console.log('Logged user id:', this.user.id);
        console.log('All questions:', this.questions);
        console.log('My questions:', this.myQuestions);

        this.cdr.detectChanges();
      },
      error: (error) => {
        alert('Could not load questions');
        console.log(error);
      }
    });
  }

  goBack() {
    this.router.navigate(['/main']);
  }

  openQuestion(questionId: number | undefined) {
    if (questionId) {
      this.router.navigate(['/question', questionId]);
    }
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['']);
  }
}
