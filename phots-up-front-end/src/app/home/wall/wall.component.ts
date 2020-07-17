import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from '../add-post/add-post.component';
import { PostSummary } from 'src/app/model/post-summary';
import { PostService } from 'src/app/shared/post.service';
import { DataTransferService } from 'src/app/shared/data-transfer.service';

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.css']
})
export class WallComponent implements OnInit {
  posts: PostSummary[] =[];
  liked: boolean = false;

  page: number = 0;

  constructor(
    private dialog: MatDialog,
    private postService: PostService,
    private transferService: DataTransferService
    ) { }

  ngOnInit(): void {
    this.postService.getPostObs()
      .subscribe(item => this.posts.push(item));

    this.postService.fetchPosts(this.page++);

    this.transferService.getPublishedPostObs()
      .subscribe(publishedPost => this.posts.unshift(publishedPost));
  }

  edit(post: PostSummary){
    let config = new MatDialogConfig();
    config.disableClose = true;
    config.width = '40%';
    config.data = {
      postSumm: post,
      isEdit: true
    };
    
    let dialogRef = this.dialog.open(AddPostComponent,config);
  }

  onLike(){
    this.liked = !this.liked;
  }

}
