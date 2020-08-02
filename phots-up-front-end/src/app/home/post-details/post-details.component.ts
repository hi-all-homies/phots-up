import { Component, OnInit, ElementRef } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { Comment } from 'src/app/model/comment';
import { CommentService } from 'src/app/shared/comment.service';
import { PostService } from 'src/app/shared/post.service';
import { ActivatedRoute } from '@angular/router';
import { DataTransferService } from 'src/app/shared/data-transfer.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { LikeDialogComponent } from './like-dialog/like-dialog.component';

@Component({
  selector: 'post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit {
  currUser: User;
  postSummary: PostSummary;
  comment: string;
  loading: boolean = true;

  constructor(
    private auth: AuthService,
    private commentServie: CommentService,
    private postServive: PostService,
    private route: ActivatedRoute,
    private transferService: DataTransferService,
    private dialog: MatDialog
    ) {}

  ngOnInit(): void {
    this.comment = '';
    let id = this.route.snapshot.queryParamMap.get('id');
    this.auth.getCurrUser().subscribe(u => this.currUser = u);

    this.getPostById(id);

    this.transferService.getNotificationObs()
        .subscribe(id => this.getPostById(id))
  }

  private getPostById(id: string){
    this.postServive.getPostById(id)
      .subscribe(body =>{
        this.postSummary = body;
        this.loading = false})
  }

  doComment(){
    let comment = this.gatherCommentObj();

    this.commentServie.addComment(comment)
      .subscribe(id =>{
        comment.id = id;
        this.postSummary.post.comments.push(comment);
        this.comment = ''});
  }

  getLikedUsers(ev: MouseEvent){
    let trigger = new ElementRef(ev.currentTarget);
    let config = new MatDialogConfig();
    config.data = {
      trigger: trigger,
      users: this.postSummary.post.likes,
    }

    this.dialog.open(LikeDialogComponent, config);
  }

  private gatherCommentObj(): Comment{
    return {
      id: null,
      author: this.currUser,
      post: {
        author: this.postSummary.post.author,
        id: this.postSummary.post.id,
        comments: null,
        content: null,
        imageKey: null,
        likes: null,
        created: null
      },
      content: this.comment
    }
  }
}
