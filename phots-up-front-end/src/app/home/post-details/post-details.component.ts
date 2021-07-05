import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { Comment } from 'src/app/model/comment';
import { CommentService } from 'src/app/shared/comment.service';
import { PostService } from 'src/app/shared/post.service';
import { DataTransferService } from 'src/app/shared/data-transfer.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit, OnDestroy{
  posts: PostSummary[];
  currentPost: PostSummary;
  index: number;

  currUser: User;
  comment: string;
  loading: boolean = true;

  unsubscribe = new Subject();


  constructor(
    private auth: AuthService,
    private commentServie: CommentService,
    private transferService: DataTransferService,
    private postServive: PostService,
    @Inject(MAT_DIALOG_DATA) private data: any
    ) {}

  ngOnInit(): void {
    this.index = this.data.index;
    this.currentPost = this.data.currentPost;
    this.posts = this.data.posts;
    this.comment = '';
    
    this.auth.getCurrUser()
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(u => this.currUser = u);

    this.transferService.getPostsObs()
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(list => this.posts = list);
    
    this.getPostById(this.currentPost.post.id);
  }

  private getPostById(id: number){
    this.postServive.getPostById(id)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(result => {
        this.currentPost = result;
        this.loading = false;
      });
  }

  back(){
    --this.index;
    this.currentPost = this.posts[this.index];
    this.loading = true;
    this.getPostById(this.currentPost.post.id);
  }

  forward(){
    ++this.index;
    this.currentPost = this.posts[this.index];
    this.loading = true;
    this.getPostById(this.currentPost.post.id);
  }

  addComment(){
    let comm = this.comment.trim();
    if (comm === '') return;
    
    let comment = this.gatherCommentObj();
    this.commentServie.addComment(comment)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(id =>{
        comment.id = id;
        this.currentPost.post.comments.push(comment);
        this.comment = ''});
  }


  addLike(){
    this.postServive.addLike(this.currentPost.post, this.currUser)
      .pipe(takeUntil(this.unsubscribe))
      .subscribe(resp =>{
        this.currentPost.meLiked = !this.currentPost.meLiked;
        if (this.currentPost.meLiked){
          this.currentPost.likes++;
          this.currentPost.post.likes.push(this.currUser);
        }
        else{
          this.currentPost.likes--;
          let ind = this.currentPost.post.likes.indexOf(this.currUser);
          this.currentPost.post.likes.splice(ind, 1);
        }})
  }

  private gatherCommentObj(): Comment{
    return {
      id: null,
      author: this.currUser,
      post: {
        author: this.currentPost.post.author,
        id: this.currentPost.post.id,
        comments: null,
        content: null,
        imageUrl: null,
        likes: null,
        created: null
      },
      content: this.comment
    }
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }
}