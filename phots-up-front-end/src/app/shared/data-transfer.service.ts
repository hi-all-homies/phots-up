import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { PostSummary } from '../model/post-summary';

@Injectable({
  providedIn: 'root'
})
export class DataTransferService {
  private postPublishedEventEmitter: Subject<PostSummary> = new Subject();
  private publishedPostObs = this.postPublishedEventEmitter.asObservable();

  private notificationEmitter: Subject<string> = new Subject();

  private postsObs: BehaviorSubject<PostSummary[]> =  new BehaviorSubject([]);

  constructor() {}

  public getPublishedPostObs(){
    return this.publishedPostObs;
  }

  public onSuccessPublish(value: PostSummary){
    this.postPublishedEventEmitter.next(value);
  }

  public receivedNotification(id: string){
    this.notificationEmitter.next(id);
  }

  public getNotificationObs(){
    return this.notificationEmitter.asObservable();
  }

  public setPosts(posts: PostSummary[]){
    this.postsObs.next(posts);
  }

  public getPostsObs(){
    return this.postsObs.asObservable();
  }
}
