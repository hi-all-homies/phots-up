import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';
import { PostSummary } from '../model/post-summary';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class DataTransferService {
  private publishedPostEmitter: Subject<PostSummary> = new Subject();
  private publishedPostObs = this.publishedPostEmitter.asObservable();

  private notificationEmitter: Subject<string> = new Subject();

  private userEmitter: BehaviorSubject<User> = new BehaviorSubject(null);

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

  public getObservableUser(){
    return this.userEmitter.asObservable();
  }

  public setObservableUser(user: User){
    this.userEmitter.next(user);
  }
}
