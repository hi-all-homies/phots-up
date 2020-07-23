import { Injectable, NgZone } from '@angular/core';
import { Subject } from 'rxjs';
import { PostSummary } from '../model/post-summary';
import { AuthService } from './auth.service';
import { Url } from './base-url';
import { StringUtils } from './string-utils';

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {
  private isOpenedOnce = false;
  private postObs: Subject<PostSummary> = new Subject();

  constructor(private auth: AuthService, private zone: NgZone) {}

  public fetchPosts(page: number){
    let token = this.auth.getToken();
    const source = new EventSource(
      Url.BASE_URL + `posts?page=${page}&jwt=${token}`);

    source.onopen = this.onOpen(source);
    source.onmessage = this.onMessage;
  }

  public fetchRecommendations(){
    let token = this.auth.getToken();
    const source = new EventSource(
      Url.BASE_URL + `recommend?jwt=${token}`);

    source.onopen = this.onOpen(source);
    source.onmessage = this.onMessage;
  }

  private onOpen (source: EventSource){
    return event =>{
      if (this.isOpenedOnce){
        source.close();
        this.isOpenedOnce = false;
      }
      else
        this.isOpenedOnce = true;
    };
  }

  private onMessage = (event: MessageEvent) =>{
    let postSummary = <PostSummary>JSON.parse(event.data);
      postSummary.image =
        StringUtils.getImageString64(postSummary.post.imageKey, postSummary.image);
      this.zone.run(() => this.postObs.next(postSummary));
  }

  public getPostObs(){
    return this.postObs.asObservable();
  }
}
