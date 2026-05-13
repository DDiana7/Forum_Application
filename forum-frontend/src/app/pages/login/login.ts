import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
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

  constructor(
      private userService: UserService,
      private router:Router
  ) {}

  login() {
    this.userService.login(this.user).subscribe({
      next: (response) => {

        if(response == null){
          alert('Invalid credentials');
          return;
        }

        localStorage.setItem(
          'user',
          JSON.stringify(response)
        );

        console.log(response);

        this.router.navigate(['/main']);
      },

      error: (error) => {
        alert('Login failed');
        console.log(error);
      }
    });
  }
}
