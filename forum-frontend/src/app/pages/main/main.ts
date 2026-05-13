import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { QuestionService, Question } from '../../services/question';
import { TagService, Tag } from '../../services/tag';

@Component({
  selector: 'app-main',
  imports: [FormsModule, NgFor, NgIf, DatePipe, RouterLink],
  templateUrl: './main.html',
  styleUrl: './main.css'
})
export class Main implements OnInit {
  isUserMenuOpen = false;
  questions: Question[] = [];
  tags: Tag[] = [];

  filteredQuestions: Question[] = [];

  searchText = '';
  tagSearchText = '';
  userSearchText = '';
  showOnlyMyQuestions = false;

  title = '';
  text = '';
  imageUrl = '';

  tagInput = '';
  selectedTags: Tag[] = [];

  isPostBoxOpen = false;

  user: any = null;

  openedQuestionMenuId: number | null = null;
  editingQuestionId: number | null = null;

  editTitle = '';
  editText = '';
  editImageUrl = '';

  constructor(
    private questionService: QuestionService,
    private tagService: TagService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const savedUser = localStorage.getItem('user');

    if (savedUser) {
      this.user = JSON.parse(savedUser);
    }

    this.loadQuestions();
    this.loadTags();
  }

  openPostBox() {
    this.isPostBoxOpen = true;
  }

  closePostBox() {
    this.isPostBoxOpen = false;
    this.clearForm();
  }

  loadQuestions() {
    this.questionService.getAllQuestions().subscribe({
      next: (response) => {
        this.questions = response;
        this.applyFilters();
        this.cdr.detectChanges();
      },
      error: (error) => {
        alert('Could not load questions');
        console.log(error);
      }
    });
  }

  applyFilters() {
    this.filteredQuestions = this.questions.filter(question => {

      const matchesText =
        this.searchText.trim() === '' ||
        question.title.toLowerCase().includes(this.searchText.toLowerCase());

      const matchesTag =
        this.tagSearchText.trim() === '' ||
        (
          question.tags &&
          question.tags.some(tag =>
            tag.name.toLowerCase().includes(this.tagSearchText.toLowerCase())
          )
        );

      const matchesUser =
        this.userSearchText.trim() === '' ||
        (
          question.author &&
          question.author.username.toLowerCase().includes(this.userSearchText.toLowerCase())
        );

      const matchesOwnQuestions =
        !this.showOnlyMyQuestions ||
        (
          this.user &&
          question.author &&
          Number(question.author.id) === Number(this.user.id)
        );

      return matchesText && matchesTag && matchesUser && matchesOwnQuestions;
    });
  }

  loadTags() {
    this.tagService.getAllTags().subscribe({
      next: (response) => {
        this.tags = response;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getUniqueUsers() {
    const users: string[] = [];

    this.questions.forEach(question => {
      if (question.author && !users.includes(question.author.username)) {
        users.push(question.author.username);
      }
    });

    return users;
  }

  clearFilters() {
    this.searchText = '';
    this.tagSearchText = '';
    this.userSearchText = '';
    this.showOnlyMyQuestions = false;

    this.applyFilters();
  }

  getFilteredTags() {
    if (this.tagInput.trim() === '') {
      return [];
    }

    const searchText = this.tagInput.toLowerCase();

    return this.tags.filter(tag =>
      tag.name.toLowerCase().includes(searchText) &&
      !this.selectedTags.some(selectedTag => selectedTag.id === tag.id)
    );
  }

  addExistingTag(tag: Tag) {
    this.selectedTags.push(tag);
    this.tagInput = '';
  }

  createAndAddTag() {
    const newTagName = this.tagInput.trim();

    if (newTagName === '') {
      return;
    }

    const alreadyExists = this.tags.find(
      tag => tag.name.toLowerCase() === newTagName.toLowerCase()
    );

    if (alreadyExists) {
      this.addExistingTag(alreadyExists);
      return;
    }

    const newTag: Tag = {
      name: newTagName
    };

    this.tagService.addTag(newTag).subscribe({
      next: (createdTag) => {
        this.tags.push(createdTag);
        this.selectedTags.push(createdTag);
        this.tagInput = '';
      },
      error: (error) => {
        alert('Could not create tag');
        console.log(error);
      }
    });
  }

  removeTag(tag: Tag) {
    this.selectedTags = this.selectedTags.filter(
      selectedTag => selectedTag.id !== tag.id
    );
  }

  addQuestion() {
    if (!this.user) {
      alert('You must be logged in');
      return;
    }

    if (this.title.trim() === '' || this.text.trim() === '') {
      alert('Title and text are required');
      return;
    }

    const question: Question = {
      title: this.title,
      text: this.text,
      imageUrl: this.imageUrl,
      author: {
        id: this.user.id
      },
      tags: this.selectedTags.map(tag => {
        return {
          id: tag.id
        };
      })
    };

    this.questionService.addQuestion(question).subscribe({
      next: () => {
        this.clearForm();
        this.isPostBoxOpen = false;
        this.loadQuestions();
      },
      error: (error) => {
        alert('Could not add question');
        console.log(error);
      }
    });
  }

  clearForm() {
    this.title = '';
    this.text = '';
    this.imageUrl = '';
    this.tagInput = '';
    this.selectedTags = [];
  }

  getFirstAnswer(question: Question) {
    if (question.answers && question.answers.length > 0) {
      return question.answers[0];
    }

    return null;
  }
  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  logout() {
    localStorage.removeItem('user');
    this.router.navigate(['']);
  }
  toggleQuestionMenu(questionId: number | undefined, event: Event) {
    event.stopPropagation();

    if (!questionId) {
      return;
    }

    if (this.openedQuestionMenuId === questionId) {
      this.openedQuestionMenuId = null;
    } else {
      this.openedQuestionMenuId = questionId;
    }
  }

  isQuestionAuthor(question: Question) {
    return this.user && question.author && this.user.id === question.author.id;
  }

  startEditQuestion(question: Question, event: Event) {
    event.stopPropagation();

    this.editingQuestionId = question.id!;
    this.editTitle = question.title;
    this.editText = question.text;
    this.editImageUrl = question.imageUrl || '';
    this.openedQuestionMenuId = null;
  }

  cancelEditQuestion(event: Event) {
    event.stopPropagation();

    this.editingQuestionId = null;
    this.editTitle = '';
    this.editText = '';
    this.editImageUrl = '';
  }

  saveEditQuestion(question: Question, event: Event) {
    event.stopPropagation();

    const updatedQuestion: Question = {
      ...question,
      title: this.editTitle,
      text: this.editText,
      imageUrl: this.editImageUrl
    };

    this.questionService.updateQuestion(question.id!, updatedQuestion).subscribe({
      next: () => {
        this.editingQuestionId = null;
        this.loadQuestions();
      },
      error: (error) => {
        alert('Could not update question');
        console.log(error);
      }
    });
  }

  deleteQuestion(questionId: number | undefined, event: Event) {
    event.stopPropagation();

    if (!questionId) {
      return;
    }

    const confirmed = confirm('Are you sure you want to delete this question?');

    if (!confirmed) {
      return;
    }

    this.questionService.deleteQuestion(questionId).subscribe({
      next: () => {
        this.loadQuestions();
      },
      error: (error) => {
        alert('Could not delete question');
        console.log(error);
      }
    });
  }

  goToProfile() {
    this.router.navigate(['/profile']);
  }


  getPopularTags() {
    const tagCounts: { tag: Tag, count: number }[] = [];

    this.tags.forEach(tag => {
      let count = 0;

      this.questions.forEach(question => {
        if (question.tags && question.tags.some(questionTag => questionTag.id === tag.id)) {
          count++;
        }
      });

      tagCounts.push({
        tag: tag,
        count: count
      });
    });

    return tagCounts
      .sort((a, b) => b.count - a.count)
      .slice(0, 4);
  }
}
