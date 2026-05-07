import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  user = {
    username: '',
    email: '',
    password: ''
  };

  constructor(private userService: UserService) {}

  register() {
    this.userService.register(this.user).subscribe({
      next: (response) => {
        alert('Register successful');
        console.log(response);
      },
      error: (error) => {
        alert('Register failed');
        console.log(error);
      }
    });
  }
}
