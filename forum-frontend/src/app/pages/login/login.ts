import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user';
@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  user = {
    email: '',
    password: ''
  };

  constructor(private userService: UserService) {}

  login() {
    this.userService.login(this.user).subscribe({
      next: (response) => {

        if(response == null){
          alert('Invalid credentials');
          return;
        }

        alert('Login successful');

        localStorage.setItem(
          'user',
          JSON.stringify(response)
        );

        console.log(response);
      },

      error: (error) => {
        alert('Login failed');
        console.log(error);
      }
    });
  }
}
