import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { PostService } from 'src/app/shared/post.service';
import { AddPostComponent } from '../add-post/add-post.component';
import { PostDetailsComponent } from '../post-details/post-details.component';
import { DataTransferService } from 'src/app/shared/data-transfer.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'post-card',
  templateUrl: './post-card.component.html',
  styleUrls: ['./post-card.component.css']
})
export class PostCardComponent implements OnInit {
  @Input('postSummary') postSummary: PostSummary;

  @Input('index') index: number;

  @Output('delete')
  emitter: EventEmitter<PostSummary> = new EventEmitter();

  currUser: User;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private auth: AuthService,
    private postService: PostService,
    private dataTransfer: DataTransferService
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

    let ref = this.dialog.open(AddPostComponent, config);
    ref.afterClosed()
      .subscribe();
  }

  delete(){
    this.postService.deletePost(this.postSummary.post)
      .subscribe(
        resp => this.emitter.emit(this.postSummary),
        err => console.log(err));
  }

  getDetails(){
    this.dataTransfer.getPostsObs()
      .pipe(first())
      .subscribe(posts => {
        const config: MatDialogConfig = {
          width: '100%',
          position: {top: '2rem'},
          closeOnNavigation: true,
          backdropClass: 'details-backdrop',
          data:{
            index: this.index,
            currentPost: this.postSummary,
            posts: posts}};
          
          this.dialog.open(PostDetailsComponent, config);
      });
  }

  onLike(){
    this.postService.addLike(this.postSummary.post, this.currUser)
      .subscribe(resp =>{
        this.postSummary.meLiked = !this.postSummary.meLiked;
        if (this.postSummary.meLiked)
          this.postSummary.likes++;
        else
          this.postSummary.likes--;
        })
  }

  toUserProfile(author: User){
    this.router.navigate(['home/profile'], {
      queryParams: {user: author.id}
    })
  }
}
