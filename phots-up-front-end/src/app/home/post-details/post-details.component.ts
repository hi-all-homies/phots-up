import { Component, OnInit } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { AuthService } from 'src/app/shared/auth.service';
import { User } from 'src/app/model/user';
import { Comment } from 'src/app/model/comment';
import { CommentService } from 'src/app/shared/comment.service';
import { PostService } from 'src/app/shared/post.service';
import { ActivatedRoute } from '@angular/router';
import { DataTransferService } from 'src/app/shared/data-transfer.service';

@Component({
  selector: 'post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit{
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
    private postService: PostService
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

  addComment(){
    let comment = this.gatherCommentObj();

    this.commentServie.addComment(comment)
      .subscribe(id =>{
        comment.id = id;
        this.postSummary.post.comments.push(comment);
        this.comment = ''});
  }


  addLike(){
    this.postService.addLike(this.postSummary.post, this.currUser)
      .subscribe(resp =>{
        this.postSummary.meLiked = !this.postSummary.meLiked;
        if (this.postSummary.meLiked){
          this.postSummary.likes++;
          this.postSummary.post.likes.push(this.currUser);
        }
        else{
          this.postSummary.likes--;
          let ind = this.postSummary.post.likes.indexOf(this.currUser);
          this.postSummary.post.likes.splice(ind, 1);
        }})
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
        image: null,
        likes: null,
        created: null
      },
      content: this.comment
    }
  }
}