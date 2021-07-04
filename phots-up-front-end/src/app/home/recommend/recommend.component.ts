import { Component, OnInit } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-recommend',
  templateUrl: './recommend.component.html',
  styleUrls: ['./recommend.component.css']
})
export class RecommendComponent implements OnInit {
  posts: PostSummary[] = [];
  loading: boolean = true;

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.postService.getRecommendations()
      .subscribe(postList => {
        this.loading = false;
        postList.forEach(el => this.posts.push(el));
        //this.transferService.setPosts(this.posts);
      });

    this.postService.getRecommendations();
  }
}
