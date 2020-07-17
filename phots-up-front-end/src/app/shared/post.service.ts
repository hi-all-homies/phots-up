import { Injectable } from '@angular/core';
import { EventSourceService } from './event-source.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostSummary } from '../model/post-summary';
import { Post } from '../model/post';
import { Url } from './base-url';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(
    private eventSource: EventSourceService,
    private http: HttpClient,
    private auth: AuthService
    ) {}

  public fetchPosts(page: number){
    this.eventSource.fetchPosts(page);
  }

  public getPostObs(): Observable<PostSummary>{
    return this.eventSource.getPostObs();
  }

  public publishPost(post: Post, image: File){
    let formData = new FormData();
    this.auth.getCurrUser().subscribe(u =>{
      post.author = u;
      formData.append('post', JSON.stringify(post));
    });
    formData.append('image',image);

    return this.http.post<any>(
        Url.BASE_URL + 'posts', formData, {observe: 'response'})
      .pipe(map(resp =>{
        if (resp.ok)
          return post;
      }))
  }

  public updatePost(post: Post, image?: File){
    let formData = new FormData();
    formData.append('post', JSON.stringify(post));
    if (image)
      formData.append('image', image);

    console.log(JSON.stringify(post))
    return this.http.put<any>(
      Url.BASE_URL + `posts/${post.id}`, formData, {observe: 'response'});
  }
}
