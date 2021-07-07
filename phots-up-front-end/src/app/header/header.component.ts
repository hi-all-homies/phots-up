import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { User } from '../model/user';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  currUser: User;

  constructor(
    private auth: AuthService,
    private router: Router
    ) { }

  ngOnInit(): void {
    this.auth.getCurrentUser()
      .subscribe(u => this.currUser = u);
  }

  home(){
    this.router.navigate(['home']);
  }

  toProfile(){
    this.router.navigate(['/home/profile'], {
      queryParams: {user: this.currUser.id}
    })
  }

  signIn(){
    this.router.navigate(['/login']);
  }

  logout(){
    this.auth.logOut();
    this.router.navigate(['/login']);
  }
}
