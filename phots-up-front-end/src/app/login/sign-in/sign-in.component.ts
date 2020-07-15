import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { AuthService } from 'src/app/shared/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {
  hide: boolean = true;
  loginForm: FormGroup;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl(''),
      password: new FormControl('')
    });
  }

  onSubmit(){
    const loginRequest = {
      username: this.loginForm.get('username').value,
      password: this.loginForm.get('password').value
    };

    this.auth.login(loginRequest)
      .subscribe(
        ans =>{
          if (ans)
            this.onSuccesLogin()},
        err => console.log('wrong pass or whatever'));
  }

  private onSuccesLogin(){
    this.router.navigate(['/home']);
  }
}
