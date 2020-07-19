import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  loggedIn: boolean = false;

  constructor(
    private dialog: MatDialog,
    private router: Router
    ) { }

  ngOnInit(): void {
    
  }

  home(){
    this.router.navigate(['home']);
  }

}
