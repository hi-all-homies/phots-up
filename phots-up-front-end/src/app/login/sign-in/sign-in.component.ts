import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthService } from 'src/app/shared/auth.service';
import { Router } from '@angular/router';
import { NotificationsService } from 'angular2-notifications';
import { first } from 'rxjs/operators';

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
    private router: Router,
    private toastsService: NotificationsService
  ) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('',[Validators.required, Validators.minLength(3)]),
      password: new FormControl('',[Validators.required, Validators.minLength(5)])
    });
  }

  logIn(){
    const loginRequest = {
      username: this.loginForm.get('username').value,
      password: this.loginForm.get('password').value
    };
    this.loading = true;
    this.auth.login(loginRequest)
      .pipe(first())
      .subscribe(
        ans =>{
          if (ans)
            this.onSuccesLogin()},
        err => {
          this.loading = false;
          this.toastsService.error('wrong username or password')});
  }

  private onSuccesLogin(){
    this.loading = false;
    this.router.navigate(['/home']);
  }
}
