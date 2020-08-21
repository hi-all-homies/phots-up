import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from './add-post/add-post.component';
import { DataTransferService } from '../shared/data-transfer.service';
import { NotificationService } from '../shared/notification.service';
import { NotificationsService } from 'angular2-notifications';
import { Notification } from '../model/notifications/notification';
import { Router } from '@angular/router';
import { EventSourceService } from '../shared/event-source.service';
import { AuthService } from '../shared/auth.service';
import { User } from '../model/user';
import { UserInfoService } from '../shared/user-info.service';
import { UserInfo } from '../model/user-info';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy{
  currUser: User;

  constructor(
    private dialog: MatDialog,
    private transferService: DataTransferService,
    private notifyService: NotificationService,
    private toastsService: NotificationsService,
    private router: Router,
    private sourceService: EventSourceService,
    private auth: AuthService,
    private userService: UserInfoService
    ) {}

  ngOnInit(): void {
    this.auth.getCurrUser().subscribe(
      u => this.initUserAndAvatar(u));
      
    this.notifyService.listen();
    this.notifyService.getNotifications()
      .subscribe(event => this.handleNotifications(event));
  }

  ngOnDestroy(): void {
    this.notifyService.closeSocket();
  }

  private handleNotifications(event: Notification){
    let notif = this.toastsService.info(
      event.getTitle(), event.getContent(),null, {id: event.getPostId()});
    
    let currentPath = this.router.url;
    notif.click.subscribe(
      click => {
        this.router.navigate(['home/post'], {queryParams: {id: notif.context.id}})
        if (currentPath.includes('post'))
          this.transferService.receivedNotification(notif.context.id);
      })
  }

  newPost(){
    const config = new MatDialogConfig();
    config.disableClose = true;
    config.width = '40%';
    
    let dialogRef = this.dialog.open(AddPostComponent,config);
    dialogRef.afterClosed()
      .subscribe(postSummary =>{
        if (postSummary)
          this.transferService.onSuccessPublish(postSummary)});
  }

  getRecommend(){
    this.sourceService.closeEventSource();
    this.router.navigate(['home/recommend']);
  }

  readonly blankAvatar: string = 'assets/logo/blank.png';

  private initUserAndAvatar(user: User){
	if (this.currUser)
		return;
	this.currUser = user;
    this.userService.getUserInfoByUserId(user.id)
      .subscribe(usr => {
        if (!usr.userInfo)
          usr.userInfo = new UserInfo(-1, null, '', this.blankAvatar);
        if (!usr.userInfo.avatar)
          usr.userInfo.avatar = this.blankAvatar;

        this.currUser.userInfo = usr.userInfo;
      })
  }

  avatarUrl(): string{
    return `background-image: url(${this.currUser.userInfo.avatar});`
  }
}
