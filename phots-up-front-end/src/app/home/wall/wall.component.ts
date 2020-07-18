import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from '../add-post/add-post.component';
import { PostSummary } from 'src/app/model/post-summary';
import { PostService } from 'src/app/shared/post.service';
import { DataTransferService } from 'src/app/shared/data-transfer.service';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.css']
})
export class WallComponent implements OnInit {
  posts: PostSummary[] =[];
  currUser: User;
  isMore: boolean = true;

  page: number = 0;

  constructor(
    private dialog: MatDialog,
    private postService: PostService,
    private transferService: DataTransferService,
    private auth: AuthService
    ) { }

  ngOnInit(): void {
    this.auth.getCurrUser().subscribe(u => this.currUser = u);

    this.postService.getPostObs()
      .subscribe(item =>{
        this.posts.push(item);
        this.isMore = true});

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

  delete(postSummary: PostSummary){
    this.postService.deletePost(postSummary.post)
      .subscribe(resp =>{
        let ind = this.posts.indexOf(postSummary);
        this.posts.splice(ind, 1)},
        
        err => console.log(err));
  }

  onLike(postSumm: PostSummary){
    let likeReq = {
      post: postSumm.post,
      user: this.currUser
    };

    this.postService.addLike(likeReq)
      .subscribe(resp =>{
        postSumm.meLiked = !postSumm.meLiked
        if (postSumm.meLiked)
          postSumm.likes++;
        else
          postSumm.likes--;
      });
  }

  onMore(){
    this.isMore = false;
    this.postService.fetchPosts(this.page++);
  }

}
