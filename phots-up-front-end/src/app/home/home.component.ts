import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from './add-post/add-post.component';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  newPost(){
    const config = new MatDialogConfig();
    config.disableClose = true;
    config.width = '40%';
    
    let dialogRef = this.dialog.open(AddPostComponent,config);
    dialogRef.beforeClosed().subscribe(obs => console.log("closing..."));
  }
}
