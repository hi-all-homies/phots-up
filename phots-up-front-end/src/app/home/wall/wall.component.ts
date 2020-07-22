import { Component, OnInit } from '@angular/core';
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
  isMore: boolean = true;
  loading: boolean = true;

  page: number = 0;

  constructor(
    private postService: PostService,
    private transferService: DataTransferService
    ) { }

  ngOnInit(): void {
    this.postService.getPostObs()
      .subscribe(item =>{
        this.posts.push(item);
        this.isMore = true;
        this.loading = false});

    this.postService.fetchPosts(this.page++);

    this.transferService.getPublishedPostObs()
      .subscribe(publishedPost => this.posts.unshift(publishedPost));
  }


  onDelete(postSumm: PostSummary){
    let ind = this.posts.indexOf(postSumm);
    this.posts.splice(ind, 1);
  }

  onMore(){
    this.isMore = false;
    this.postService.fetchPosts(this.page++);
  }
}
