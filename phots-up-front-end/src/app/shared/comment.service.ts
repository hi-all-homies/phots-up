import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Comment } from '../model/comment';
import { environment as ENV } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {}

  public addComment(comment: Comment){
    return this.http.post<any>(
        ENV.BASE_URL + `${comment.post.id}/comments`, comment, {observe: 'body'});
  }
}
