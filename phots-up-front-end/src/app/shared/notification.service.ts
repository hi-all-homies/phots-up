import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import {webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { environment as ENV } from 'src/environments/environment';
import { Subject } from 'rxjs';
import { Notification } from '../model/notifications/notification';
import { NotificationType } from '../model/notifications/notification-type';
import { PostLikedNotification } from '../model/notifications/post-liked-notification';
import { PostCommentedNotification } from '../model/notifications/post-commented-notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private socket: WebSocketSubject<any>;
  private notifications: Subject<Notification> = new Subject();

  constructor(private auth: AuthService) {
  }

  public listen(){
    let jwt = this.auth.getToken();
    this.socket = webSocket(ENV.WS_URL + `?jwt=${jwt}`);

    this.socket.subscribe(event =>{
      let notif: Notification;
      if (event.type == NotificationType.POST_LIKED)
        notif = Object.assign(new PostLikedNotification(), event);
      else
        notif = Object.assign(new PostCommentedNotification(), event);
      this.notifications.next(notif);
    });
  }

  public getNotifications(){
    return this.notifications.asObservable();
  }

  public closeSocket(){
    this.socket.unsubscribe();
  }
}
