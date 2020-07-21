import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, ValidatorFn, ValidationErrors, Validators } from '@angular/forms';
import { AuthService } from 'src/app/shared/auth.service';
import { Router } from '@angular/router';
import { NotificationsService } from 'angular2-notifications';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  regForm: FormGroup;
  hide: boolean = true;

  constructor(
    private auth: AuthService,
    private router: Router,
    private toastsService: NotificationsService
    ) { }

  ngOnInit(): void {
    this.regForm = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(3)]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(5)]),
      repeatPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(5)])},
        {validators: this.passMatch});
  }

  signUp(){
    let regRequest = {
      username: this.regForm.get('username').value,
      password: this.regForm.get('password').value
    };

    this.auth.registrateUser(regRequest)
      .subscribe(
        resp => this.router.navigate(['/login']),
        err => this.toastsService.error(
          'error during registration','that name already taken or whatever'))
  }

  passMatch: ValidatorFn = (form: FormGroup): ValidationErrors | null =>{
    let once = form.get('password').value;
    let twice = form.get('repeatPassword').value;
    return (once === twice) ? null : {notMatching: true};
  }
}
