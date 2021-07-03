import { Component, OnInit } from '@angular/core';
import { PostSummary } from 'src/app/model/post-summary';
import { PostService } from 'src/app/shared/post.service';
import { DataTransferService } from 'src/app/shared/data-transfer.service';
import { BehaviorSubject } from 'rxjs';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'app-wall',
  templateUrl: './wall.component.html',
  styleUrls: ['./wall.component.css']
})
export class WallComponent implements OnInit {
  posts: PostSummary[] =[];
  loading: boolean = true;

  page: number = 0;
  pageSubj: BehaviorSubject<number> = new BehaviorSubject(this.page);

  constructor(
    private postService: PostService,
    private transferService: DataTransferService
    ) { }

  ngOnInit(): void {
    this.pageSubj.asObservable()
      .pipe(
        flatMap(p => this.postService.fetchPosts(p)))
      .subscribe(item => {
        if (this.posts.length > 0) this.loading = false;
        this.posts.push(item)});

    this.transferService.getPublishedPostObs()
      .subscribe(publishedPost => this.posts.unshift(publishedPost));
  }


  onDelete(postSumm: PostSummary){
    let ind = this.posts.indexOf(postSumm);
    this.posts.splice(ind, 1);
  }

  onMore(){
    this.loading = true;
    this.pageSubj.next(++this.page);
  }
}
