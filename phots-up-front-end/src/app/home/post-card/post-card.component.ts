import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { PostService } from 'src/app/shared/post.service';
import { AddPostComponent } from '../add-post/add-post.component';
import { DataTransferService } from 'src/app/shared/data-transfer.service';

@Component({
  selector: 'post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.css']
})
export class PostCardComponent implements OnInit {
  @Input('postSummary') postSummary: PostSummary;

  @Output('delete')
  emitter: EventEmitter<PostSummary> = new EventEmitter();

  currUser: User;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private auth: AuthService,
    private postService: PostService,
    private transferServie: DataTransferService
    ) {}

  ngOnInit(): void {
    this.auth.getCurrUser().subscribe(u => this.currUser = u);
  }

  edit(){
    let config = new MatDialogConfig();
    config.disableClose = true;
    config.width = '40%';
    config.data = {
      postSumm: this.postSummary,
      isEdit: true
    };

    this.dialog.open(AddPostComponent, config);
  }

  delete(){
    this.postService.deletePost(this.postSummary.post)
      .subscribe(
        resp => this.emitter.emit(this.postSummary),
        err => console.log(err));
  }

  getDetails(){
    this.router.navigate(['home/post'], {
      queryParams: {id: this.postSummary.post.id}
    })
  }

  onLike(){
    let likeReq = {
      post: this.postSummary.post,
      user: this.currUser
    };

    this.postService.addLike(likeReq)
      .subscribe(resp =>{
        this.postSummary.meLiked = !this.postSummary.meLiked;
        if (this.postSummary.meLiked)
          this.postSummary.likes++;
        else
          this.postSummary.likes--;
        })
  }

  toUserProfile(author: User){
    this.transferServie.setObservableUser(author);
    this.router.navigate(['home/profile'], {
      queryParams: {user: author.id}
    })
  }
}
