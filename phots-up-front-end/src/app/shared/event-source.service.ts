import { Injectable, NgZone } from '@angular/core';
import { Subject } from 'rxjs';
import { PostSummary } from '../model/post-summary';
import { environment as ENV } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EventSourceService {
  private isOpenedOnce = false;
  private eventSource: EventSource;
  private postObs: Subject<PostSummary> = new Subject();

  private theEnd: PostSummary = new PostSummary();

  constructor(private zone: NgZone) {}

  public fetchPosts(page: number){
    
    const source = new EventSource(
      ENV.BASE_URL + `posts?page=${page}`);

    source.onopen = this.onOpen(source);
    source.onmessage = this.onMessage;
    this.eventSource = source;
  }

  public fetchRecommendations(){
    
    const source = new EventSource(ENV.BASE_URL + `recommend?`);

    source.onopen = this.onOpen(source);
    source.onmessage = this.onMessage;
    this.eventSource = source;
  }

  private onOpen (source: EventSource){
    return event =>{
      if (this.isOpenedOnce){
        source.close();
        this.zone.run(() => this.postObs.next(this.theEnd));
        this.isOpenedOnce = false;
      }
      else
        this.isOpenedOnce = true;
    };
  }

  private onMessage = (event: MessageEvent) =>{
    let postSummary = <PostSummary>JSON.parse(event.data);
    this.zone.run(() => this.postObs.next(postSummary));
  }

  public getPostObs(){
    return this.postObs.asObservable();
  }

  public closeEventSource(){
    if (this.eventSource){
      this.eventSource.close();
      this.eventSource = null;
      this.isOpenedOnce = false;
    }
  }
}
