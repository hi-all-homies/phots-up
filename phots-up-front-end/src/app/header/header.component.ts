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
    this.auth.getCurrUser().subscribe(u => this.currUser = u);
  }

  home(){
    this.router.navigate(['home']);
  }

  signIn(){
    this.router.navigate(['/login']);
  }

  logout(){
    this.auth.logOut();
    this.router.navigate(['/login']);
  }
}
