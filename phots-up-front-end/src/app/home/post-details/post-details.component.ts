import { Component, OnInit } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { Comment } from 'src/app/model/comment';
import { CommentService } from 'src/app/shared/comment.service';
import { PostService } from 'src/app/shared/post.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit {
  currUser: User;
  postSummary: Observable<PostSummary>;
  currPost: PostSummary;
  comment: string;

  constructor(
    private auth: AuthService,
    private commentServie: CommentService,
    private postServive: PostService,
    private route: ActivatedRoute
    ) {}

  ngOnInit(): void {
    this.comment = '';
    let id = this.route.snapshot.queryParamMap.get('id');
    this.auth.getCurrUser().subscribe(u => this.currUser = u);

    this.postSummary = this.postServive.getPostById(id);
    this.postSummary.subscribe(p => this.currPost = p);
  }

  doComment(){
    let comment: Comment = {
      id: null,
      author: this.currUser,
      post: {
        author: this.currPost.post.author,
        id: this.currPost.post.id,
        comments: null,
        content: null,
        imageKey: null,
        likes: null
      },
      content: this.comment};
    this.comment = '';

    this.commentServie.addComment(comment)
      .subscribe(id =>{
        comment.id = id;
        this.currPost.post.comments.push(comment)});
  }
}
