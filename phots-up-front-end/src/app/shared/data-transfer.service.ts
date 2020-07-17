import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataTransferService {
  private emitter: Subject<void> = new Subject();
  private obs = this.emitter.asObservable();

  constructor() {}

  public getObs(){
    return this.obs;
  }

  public emit(){
    this.emitter.next();
  }
}
