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

  isTagPopupOpen = false;
  showAllTags = false;
  tagSearchPopupText = '';

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

  isLoggedIn(): boolean{
    return this.user !== null
  }

  openPostBox() {
    if(!this.user){
      alert('You must be logged in to ask a question');
      this.router.navigate(['/login']);
      return;
    }
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
    this.user = null;
    this.showOnlyMyQuestions = false;
    this.applyFilters();
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
    if(!this.user){
      this.router.navigate(['/login']);
      return;
    }
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

  openTagPopup() {
    this.isTagPopupOpen = true;
    this.showAllTags = false;
    this.tagSearchPopupText = '';
  }

  closeTagPopup() {
    this.isTagPopupOpen = false;
  }

  getPopupTags() {
    let availableTags = this.tags.filter(tag =>
      !this.selectedTags.some(selectedTag => selectedTag.id === tag.id)
    );

    if (this.tagSearchPopupText.trim() !== '') {
      const search = this.tagSearchPopupText.toLowerCase();

      availableTags = availableTags.filter(tag =>
        tag.name.toLowerCase().includes(search)
      );
    }

    if (!this.showAllTags && this.tagSearchPopupText.trim() === '') {
      return this.getPopularTags()
        .map(item => item.tag)
        .filter(tag =>
          !this.selectedTags.some(selectedTag => selectedTag.id === tag.id)
        );
    }

    return availableTags;
  }

  tagExistsInPopupSearch() {
    const search = this.tagSearchPopupText.trim().toLowerCase();

    return this.tags.some(tag =>
      tag.name.toLowerCase() === search
    );
  }

  createTagFromPopup() {
    const newTagName = this.tagSearchPopupText.trim();

    if (newTagName === '') {
      return;
    }

    const existingTag = this.tags.find(
      tag => tag.name.toLowerCase() === newTagName.toLowerCase()
    );

    if (existingTag) {
      this.addExistingTag(existingTag);
      this.closeTagPopup();
      return;
    }

    const newTag: Tag = {
      name: newTagName
    };

    this.tagService.addTag(newTag).subscribe({
      next: (createdTag) => {
        this.tags.push(createdTag);
        this.selectedTags.push(createdTag);
        this.tagSearchPopupText = '';
        this.closeTagPopup();
      },
      error: (error) => {
        alert('Could not create tag');
        console.log(error);
      }
    });
  }

  getUserVotesKey(): string {
    return `questionVotes_${this.user?.id}`;
  }

  getUserQuestionVotes(): Record<number, 'UPVOTE' | 'DOWNVOTE'> {
    const saved = localStorage.getItem(this.getUserVotesKey());
    return saved ? JSON.parse(saved) : {};
  }

  saveQuestionVote(questionId: number, voteType: 'UPVOTE' | 'DOWNVOTE') {
    const votes = this.getUserQuestionVotes();
    votes[questionId] = voteType;
    localStorage.setItem(this.getUserVotesKey(), JSON.stringify(votes));
  }

  hasVoted(questionId: number | undefined, voteType: 'UPVOTE' | 'DOWNVOTE'): boolean {
    if (!questionId || !this.user) return false;
    const votes = this.getUserQuestionVotes();
    return votes[questionId] === voteType;
  }


  voteQuestion(question: Question, voteType: 'UPVOTE' | 'DOWNVOTE', event: Event) {
    event.stopPropagation();

    if (!this.user) {
      alert('You must be logged in to vote');
      this.router.navigate(['/login']);
      return;
    }

    if (!question.id) return;


    this.questionService.voteQuestion(question.id, this.user.id, voteType).subscribe({
      next: (newScore) => {
        this.saveQuestionVote(question.id!, voteType); // în loc de userQuestionVotes.set
        const index = this.filteredQuestions.findIndex(q => q.id === question.id);
        if (index !== -1) {
          this.filteredQuestions[index] = { ...question, voteScore: newScore };
        }
        const index2 = this.questions.findIndex(q => q.id === question.id);
        if (index2 !== -1) {
          this.questions[index2] = { ...question, voteScore: newScore };
        }
        this.cdr.detectChanges();
      },
      error: () => {
        alert('You cannot vote this question');
      }
    });
  }


}
