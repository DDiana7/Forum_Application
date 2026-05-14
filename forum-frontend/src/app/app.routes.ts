
import { Routes } from '@angular/router';

import { Register } from './pages/register/register';
import { Login } from './pages/login/login';
import { Main } from './pages/main/main';
import { QuestionDetails } from './pages/question-details/question-details';
import { Profile } from './pages/profile/profile';

export const routes: Routes = [
  { path: '', component: Main },
  { path: 'main', component: Main },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'question/:id', component: QuestionDetails },
  { path: 'profile', component: Profile }
];
