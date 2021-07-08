import { Injectable } from '@angular/core';
import { WebSocketSubject, WebSocketSubjectConfig } from 'rxjs/webSocket';
import { environment as ENV } from 'src/environments/environment';
import { Observable, Subject, interval } from 'rxjs';
import { Notification } from '../model/notifications/notification';
import { NotificationType } from '../model/notifications/notification-type';
import { PostLikedNotification } from '../model/notifications/post-liked-notification';
import { PostCommentedNotification } from '../model/notifications/post-commented-notification';
import { takeWhile } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private socket: WebSocketSubject<any>;

  private username: string;

  private config : WebSocketSubjectConfig<any>;

  private notifications: Subject<Notification> = new Subject();

  private reconnections: Observable<number>;

  constructor() {}

  public listen(username: string){
    this.username = username;
    this.connect();
  }

  private connect(){
    this.config = {
      url: ENV.WS_URL + `?username=${this.username}`,
      closeObserver: {next: (event: CloseEvent) => this.socket = null},
      openObserver: {next: (event: Event) => {}}};
    
    this.socket = new WebSocketSubject(this.config);

    this.socket.subscribe(event =>{
      let notif: Notification;
      if (event.type == NotificationType.POST_LIKED)
        notif = Object.assign(new PostLikedNotification(), event);
      else
        notif = Object.assign(new PostCommentedNotification(), event);
      this.notifications.next(notif);
    },
      err => { if (!this.socket) this.reconnect() });
  }

  private reconnect(){
    this.reconnections = interval(3000)
            .pipe(takeWhile((v, index) => index < 10 && !this.socket));

    this.reconnections.subscribe(
      () => this.connect(),
      null,
      () => {
        this.reconnections = null;
        if (!this.socket)
          this.notifications.complete();
      });
  }

  public getNotifications(){
    return this.notifications.asObservable();
  }

  public closeSocket(){
    if (this.socket)
      this.socket.unsubscribe();
    this.notifications.complete();
  }
}
