import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { User } from '../model/user';
import { EventSourceService } from '../shared/event-source.service';
import { DataTransferService } from '../shared/data-transfer.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  currUser: User;

  constructor(
    private auth: AuthService,
    private router: Router,
    private sourceService: EventSourceService,
    private transferService: DataTransferService
    ) { }

  ngOnInit(): void {
    this.auth.getCurrUser().subscribe(u => this.currUser = u);
  }

  home(){
    this.sourceService.closeEventSource();
    this.router.navigate(['home']);
  }

  toProfile(){
    this.transferService.setObservableUser(this.currUser);
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
