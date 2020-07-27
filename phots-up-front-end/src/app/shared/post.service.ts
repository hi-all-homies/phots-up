import { Injectable } from '@angular/core';
import { EventSourceService } from './event-source.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostSummary } from '../model/post-summary';
import { Post } from '../model/post';
import { Url } from './base-url';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';
import { StringUtils } from './string-utils';

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

  public getRecommendations(){
    this.eventSource.fetchRecommendations();
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

    return this.http.post<Post>(
        Url.BASE_URL + 'posts', formData, {observe: 'body'})
  }


  getPostById(id: string) {
    return this.http.get<PostSummary>(
        Url.BASE_URL + `posts/${id}`, {observe: 'body'})
      .pipe(map(p =>{
        p.image = StringUtils.getImageString64(p.post.imageKey, p.image);
        return p}));
  }


  public updatePost(post: Post, image?: File){
    let formData = new FormData();
    formData.append('post', JSON.stringify(post));
    if (image)
      formData.append('image', image);

    return this.http.put<any>(
      Url.BASE_URL + `posts/${post.id}`, formData, {observe: 'response'});
  }

  deletePost(post: Post) {
    return this.http.delete<any>(
      Url.BASE_URL + `posts/${post.id}`, {observe: 'response'});
  }

  addLike(likeReq: any){
    return this.http.post<any>(
      Url.BASE_URL + `${likeReq.post.id}/likes`, likeReq, {observe: 'response'});
  }
}
