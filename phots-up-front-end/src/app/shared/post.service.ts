import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostSummary } from '../model/post-summary';
import { Post } from '../model/post';
import { environment as ENV } from 'src/environments/environment';
import { AuthService } from './auth.service';
import { concatMap } from 'rxjs/operators';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(
    private http: HttpClient,
    private auth: AuthService
    ) {}

  public fetchPosts(page: number){
    const url = ENV.BASE_URL + `posts?page=${page}`;
    return this.getPostSummaryObs(url);
  }

  public getRecommendations(){
    const url = ENV.BASE_URL + `recommend`;
    return this.getPostSummaryObs(url);
  }

  private getPostSummaryObs(url: string){
    return this.http.get<PostSummary[]>(url, {observe: 'body'})
      .pipe(
        concatMap(list => list));
  }


  public publishPost(post: Post, image: File){
    let formData = new FormData();
    this.auth.getCurrUser().subscribe(u =>{
      post.author = u;
      formData.append('post', JSON.stringify(post));
    });
    formData.append('image',image);

    return this.http.post<Post>(
        ENV.BASE_URL + 'posts', formData, {observe: 'body'})
  }


  getPostById(id: string) {
    return this.http.get<PostSummary>(
        ENV.BASE_URL + `posts/${id}`, {observe: 'body'});
  }


  public updatePost(post: Post, image?: File){
    let formData = new FormData();

    let updatedPost: Post = { ...post };
    formData.append('post', JSON.stringify(updatedPost));

    if (image)
      formData.append('image', image);

    return this.http.put<any>(
      ENV.BASE_URL + `posts/${post.id}`, formData, {observe: 'response'});
  }

  deletePost(post: Post) {
    return this.http.delete<any>(
      ENV.BASE_URL + `posts/${post.id}`, {observe: 'response'});
  }

  addLike(post: Post, user: User){
    let likedPost: Post = {
      ...post
    };
    
    let likeReq = {
      post: likedPost,
      user: user
    }

    return this.http.post<any>(
      ENV.BASE_URL + `${likeReq.post.id}/likes`, likeReq, {observe: 'response'});
  }
}
