import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user';
import {RouterLink} from '@angular/router';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink, NgIf],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  showSuccessPopup = false;
  errorMessage = '';

  user = {
    username: '',
    email: '',
    password: ''
  };

  constructor(private userService: UserService, private router: Router) {}

  register() {
    this.userService.register(this.user).subscribe({
      next: () => {
        this.showSuccessPopup = true;
      },
      error: (error) => {
        this.errorMessage = 'Register failed. Try another email or username.';
        console.log(error);
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
