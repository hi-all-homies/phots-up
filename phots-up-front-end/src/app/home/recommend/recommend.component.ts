import { Component, OnInit } from '@angular/core';
import { timeout } from 'rxjs/operators';
import { PostSummary } from 'src/app/model/post-summary';
import { DataTransferService } from 'src/app/shared/data-transfer.service';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-recommend',
  templateUrl: './recommend.component.html',
  styleUrls: ['./recommend.component.css']
})
export class RecommendComponent implements OnInit {
  posts: PostSummary[] = [];
  loading: boolean = true;

  constructor(private postService: PostService, private transferService: DataTransferService) {}

  ngOnInit(): void {
    this.postService.getRecommendations()
      .pipe(timeout(4000))
      .subscribe(
        postList => {
          this.loading = false;
          postList.forEach(el => this.posts.push(el));
          this.transferService.setPosts(this.posts);
        },
        err => this.loading = false);

    this.postService.getRecommendations();
  }
}
