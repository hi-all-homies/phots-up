import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from '../add-post/add-post.component';

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.css']
})
export class WallComponent implements OnInit {
  posts: number[] = [1,2];
  liked: boolean = false;

  constructor(private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  edit(post: any){
    let config = new MatDialogConfig();
    config.disableClose = true;
    config.width = '40%';
    config.data = {postId: post}
    
    let dialogRef = this.dialog.open(AddPostComponent,config);
  }

  onLike(){
    this.liked = !this.liked;
  }

}
