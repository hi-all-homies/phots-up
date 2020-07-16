import { Component, OnInit, NgZone } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from '../add-post/add-post.component';
import { AuthService } from 'src/app/shared/auth.service';
import { Url } from 'src/app/shared/base-url';
import { PostSummary } from 'src/app/model/post-summary';

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.css']
})
export class WallComponent implements OnInit {
  posts: PostSummary[] =[];
  liked: boolean = false;

  page: number = 0;
  token: string;
  isOpenOnce: boolean = false;

  constructor(
    private dialog: MatDialog,
    private auth: AuthService,
    private zone: NgZone
    ) { }

  ngOnInit(): void {
    this.token = this.auth.getToken();
    let source =
      new EventSource(Url.BASE_URL + `posts?page=${this.page}&jwt=${this.token}`);

    source.onopen = openEvent =>{
      if (this.isOpenOnce)
        source.close();
      else
        this.isOpenOnce = true;
    }

    source.onmessage = message =>{
      let postSummary = <PostSummary>JSON.parse(message.data);
      let ind = postSummary.post.imageKey.lastIndexOf('.');
      let ext = postSummary.post.imageKey.substring(ind+1);
      
      postSummary.image = `data:image/${ext};base64,`+postSummary.image;
      this.zone.run(() => this.posts.unshift(postSummary));
    }
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
