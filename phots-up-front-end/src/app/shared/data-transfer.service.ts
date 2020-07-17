import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { PostSummary } from '../model/post-summary';

@Injectable({
  providedIn: 'root'
})
export class DataTransferService {
  private publishedPostEmitter: Subject<PostSummary> = new Subject();
  private publishedPostObs = this.publishedPostEmitter.asObservable();

  constructor() {}

  public getPublishedPostObs(){
    return this.publishedPostObs;
  }

  public onSuccessPublish(value: PostSummary){
    this.publishedPostEmitter.next(value);
  }
}
