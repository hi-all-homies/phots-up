import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { PostSummary } from '../model/post-summary';

@Injectable({
  providedIn: 'root'
})
export class DataTransferService {
  private publishedPostEmitter: Subject<PostSummary> = new Subject();
  private publishedPostObs = this.publishedPostEmitter.asObservable();

  private notificationEmitter: Subject<string> = new Subject();

  constructor() {}

  public getPublishedPostObs(){
    return this.publishedPostObs;
  }

  public onSuccessPublish(value: PostSummary){
    this.publishedPostEmitter.next(value);
  }

  public receivedNotification(id: string){
    this.notificationEmitter.next(id);
  }

  public getNotificationObs(){
    return this.notificationEmitter.asObservable();
  }
}
