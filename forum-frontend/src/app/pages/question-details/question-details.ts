import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuestionService, Question } from '../../services/question';
import { AnswerService, Answer } from '../../services/answer';

@Component({
  selector: 'app-question-details',
  imports: [NgIf, NgFor, DatePipe, FormsModule],
  templateUrl: './question-details.html',
  styleUrl: './question-details.css'
})
export class QuestionDetails implements OnInit {

  question: Question | null = null;
  answers: Answer[] = [];

  answerText = '';
  answerImageUrl = '';

  user: any = null;
  questionId = 0;

  openedAnswerMenuId: number | null = null;
  editingAnswerId: number | null = null;

  editAnswerText = '';
  editAnswerImageUrl = '';

  openedQuestionMenu = false;
  editingQuestion = false;

  editQuestionTitle = '';
  editQuestionText = '';
  editQuestionImageUrl = '';
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private questionService: QuestionService,
    private answerService: AnswerService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const savedUser = localStorage.getItem('user');

    if (savedUser) {
      this.user = JSON.parse(savedUser);
    }

    this.questionId = Number(this.route.snapshot.paramMap.get('id'));

    this.loadQuestion();
    this.loadAnswers();
  }

  loadQuestion() {
    this.questionService.getQuestionById(this.questionId).subscribe({
      next: (response) => {
        this.question = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        alert('Could not load question');
        console.log(error);
      }
    });
  }

  loadAnswers() {
    this.answerService.getAnswersByQuestionId(this.questionId).subscribe({
      next: (response) => {
        this.answers = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  addAnswer() {
    if (!this.user) {
      alert('You must be logged in');
      return;
    }

    if (this.answerText.trim() === '') {
      alert('Answer text is required');
      return;
    }

    const answer: Answer = {
      text: this.answerText,
      imageUrl: this.answerImageUrl,
      author: {
        id: this.user.id
      },
      question: {
        id: this.questionId
      }
    };

    this.answerService.addAnswer(answer).subscribe({
      next: () => {
        this.answerText = '';
        this.answerImageUrl = '';
        this.loadQuestion();
        this.loadAnswers();
      },
      error: (error) => {
        alert('Could not add answer');
        console.log(error);
      }
    });
  }

  goBack() {
    this.router.navigate(['/main']);
  }

  toggleAnswerMenu(answerId: number | undefined) {
    if (!answerId) {
      return;
    }

    if (this.openedAnswerMenuId === answerId) {
      this.openedAnswerMenuId = null;
    } else {
      this.openedAnswerMenuId = answerId;
    }
  }

  isAnswerAuthor(answer: Answer) {
    return this.user && answer.author && this.user.id === answer.author.id;
  }

  startEditAnswer(answer: Answer) {
    this.editingAnswerId = answer.id!;
    this.editAnswerText = answer.text;
    this.editAnswerImageUrl = answer.imageUrl || '';
    this.openedAnswerMenuId = null;
  }

  cancelEditAnswer() {
    this.editingAnswerId = null;
    this.editAnswerText = '';
    this.editAnswerImageUrl = '';
  }

  saveEditAnswer(answer: Answer) {
    const updatedAnswer: Answer = {
      text: this.editAnswerText,
      imageUrl: this.editAnswerImageUrl,
      author: {
        id: answer.author.id
      },
      question: {
        id: this.questionId
      }
    };

    this.answerService.updateAnswer(answer.id!, updatedAnswer).subscribe({
      next: () => {
        this.editingAnswerId = null;
        this.loadAnswers();
      },
      error: (error) => {
        alert('Could not update answer');
        console.log(error);
      }
    });
  }

  deleteAnswer(answerId: number | undefined) {
    if (!answerId) {
      return;
    }

    const confirmed = confirm('Are you sure you want to delete this answer?');

    if (!confirmed) {
      return;
    }

    this.answerService.deleteAnswer(answerId).subscribe({
      next: () => {
        this.loadAnswers();
      },
      error: (error) => {
        alert('Could not delete answer');
        console.log(error);
      }
    });
  }

  isQuestionAuthor() {
    return this.user &&
      this.question &&
      this.question.author &&
      this.user.id === this.question.author.id;
  }

  toggleQuestionMenu() {
    this.openedQuestionMenu = !this.openedQuestionMenu;
  }

  startEditQuestion() {
    if (!this.question) {
      return;
    }

    this.editingQuestion = true;
    this.openedQuestionMenu = false;

    this.editQuestionTitle = this.question.title;
    this.editQuestionText = this.question.text;
    this.editQuestionImageUrl = this.question.imageUrl || '';
  }

  cancelEditQuestion() {
    this.editingQuestion = false;
    this.editQuestionTitle = '';
    this.editQuestionText = '';
    this.editQuestionImageUrl = '';
  }

  saveEditQuestion() {
    if (!this.question) {
      return;
    }

    const updatedQuestion: Question = {
      ...this.question,
      title: this.editQuestionTitle,
      text: this.editQuestionText,
      imageUrl: this.editQuestionImageUrl
    };

    this.questionService.updateQuestion(this.question.id!, updatedQuestion).subscribe({
      next: () => {
        this.editingQuestion = false;
        this.loadQuestion();
      },
      error: (error) => {
        alert('Could not update question');
        console.log(error);
      }
    });
  }

  deleteQuestion() {
    if (!this.question) {
      return;
    }

    const confirmed = confirm('Are you sure you want to delete this question?');

    if (!confirmed) {
      return;
    }

    this.questionService.deleteQuestion(this.question.id!).subscribe({
      next: () => {
        this.router.navigate(['/main']);
      },
      error: (error) => {
        alert('Could not delete question');
        console.log(error);
      }
    });
  }



  // --- VOTE INTREBARE ---
  getQuestionVotesKey(): string {
    return `questionVotes_${this.user?.id}`;
  }

  getQuestionVotes(): Record<number, 'UPVOTE' | 'DOWNVOTE'> {
    const saved = localStorage.getItem(this.getQuestionVotesKey());
    return saved ? JSON.parse(saved) : {};
  }

  hasVotedQuestion(voteType: 'UPVOTE' | 'DOWNVOTE'): boolean {
    if (!this.question?.id || !this.user) return false;
    return this.getQuestionVotes()[this.question.id] === voteType;
  }

  voteQuestion(voteType: 'UPVOTE' | 'DOWNVOTE') {
    if (!this.user) {
      alert('You must be logged in to vote');
      this.router.navigate(['/login']);
      return;
    }
    if (!this.question?.id) return;
    if (this.hasVotedQuestion(voteType)) return;

    this.questionService.voteQuestion(this.question.id, this.user.id, voteType).subscribe({
      next: (newScore) => {
        this.question!.voteScore = newScore;
        const votes = this.getQuestionVotes();
        votes[this.question!.id!] = voteType;
        localStorage.setItem(this.getQuestionVotesKey(), JSON.stringify(votes));
        this.cdr.detectChanges();
      },
      error: () => alert('Nu poți vota această întrebare')
    });
  }

  // --- VOTE RASPUNS ---
  getAnswerVotesKey(): string {
    return `answerVotes_${this.user?.id}`;
  }

  getAnswerVotes(): Record<number, 'UPVOTE' | 'DOWNVOTE'> {
    const saved = localStorage.getItem(this.getAnswerVotesKey());
    return saved ? JSON.parse(saved) : {};
  }

  hasVotedAnswer(answerId: number | undefined, voteType: 'UPVOTE' | 'DOWNVOTE'): boolean {
    if (!answerId || !this.user) return false;
    return this.getAnswerVotes()[answerId] === voteType;
  }

  voteAnswer(answer: Answer, voteType: 'UPVOTE' | 'DOWNVOTE') {
    if (!this.user) {
      alert('You must be logged in to vote');
      this.router.navigate(['/login']);
      return;
    }
    if (!answer.id) return;

    // blochează doar dacă e același vot
    if (this.hasVotedAnswer(answer.id, voteType)) return;

    this.answerService.voteAnswer(answer.id, this.user.id, voteType).subscribe({
      next: (newScore) => {
        answer.voteScore = newScore;
        const votes = this.getAnswerVotes();
        votes[answer.id!] = voteType; // suprascrie votul vechi
        localStorage.setItem(this.getAnswerVotesKey(), JSON.stringify(votes));

        this.answers.sort((a, b) => {
          const scorA = a.voteScore ?? 0;
          const scorB = b.voteScore ?? 0;
          return scorB - scorA; // cel cu scor mai mare apare primul
        });
        this.answers = [...this.answers];

        this.cdr.detectChanges();
      },
      error: () => alert('Nu poți vota acest răspuns')
    });
  }

  acceptAnswer(answer: Answer) {
    if (!this.user || !this.question?.id) return;

    this.answerService.acceptAnswer(answer.id!, this.user.id).subscribe({
      next: (updatedQuestion) => {
        this.question!.status = updatedQuestion.status;
        this.cdr.detectChanges();
      },
      error: () => alert('Nu poți accepta acest răspuns')
    });
  }
}
