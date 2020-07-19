import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Comment } from '../model/comment';
import { Url } from './base-url';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {}

  public addComment(comment: Comment){
    return this.http.post<any>(
        Url.BASE_URL + `${comment.post.id}/comments`, comment, {observe: 'body'});
  }
}
