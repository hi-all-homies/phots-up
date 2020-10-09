import { Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { FormGroup, FormControl, ValidatorFn, ValidationErrors, Validators } from '@angular/forms';
import { AuthService } from 'src/app/shared/auth.service';
import { NotificationsService } from 'angular2-notifications';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  regForm: FormGroup;
  hide: boolean = true;

  @ViewChild('form') private form: ElementRef;

  constructor(
    private auth: AuthService,
    private toastsService: NotificationsService,
    private renderer: Renderer2
    ) { }

  ngOnInit(): void {
    this.regForm = new FormGroup({
      email: new FormControl('', [
        Validators.email,
        Validators.required]),
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
      password: this.regForm.get('password').value,
      email: this.regForm.get('email').value
    };

    this.auth.registrateUser(regRequest)
      .subscribe(
        resp => this.successReg(),
        err => this.toastsService.error(
          'error during registration','that name already taken or whatever'))
  }


  private successReg(){
    const email = this.regForm.get('email').value;
    this.regForm.reset({email: '', username: '', password: '', repeatPassword: ''});

    const h2 = this.renderer.createElement('h2');
    const text = this.renderer.createText(`to the email ${email} the confirm code was sent`);
    this.renderer.appendChild(h2, text);

    this.renderer.appendChild(this.form.nativeElement, h2);
    this.renderer.setStyle(h2, 'color', 'royalblue');
    this.renderer.setStyle(h2, 'width', '100%');
  }


  private passMatch: ValidatorFn = (form: FormGroup): ValidationErrors | null =>{
    let once = form.get('password').value;
    let twice = form.get('repeatPassword').value;
    return (once === twice) ? null : {notMatching: true};
  }
}