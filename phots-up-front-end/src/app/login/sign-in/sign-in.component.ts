import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
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
  loading: boolean = false;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('',[Validators.required, Validators.minLength(3)]),
      password: new FormControl('',[Validators.required, Validators.minLength(5)])
    });
  }

  onSubmit(){
    const loginRequest = {
      username: this.loginForm.get('username').value,
      password: this.loginForm.get('password').value
    };
    this.loading = true;
    this.auth.login(loginRequest)
      .subscribe(
        ans =>{
          if (ans)
            this.onSuccesLogin()},
        err => {
          this.loading = false;
          console.log(err)
        });
  }

  private onSuccesLogin(){
    this.loading = false;
    this.router.navigate(['/home']);
  }
}
