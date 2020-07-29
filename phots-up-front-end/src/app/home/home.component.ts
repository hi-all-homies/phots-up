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
    private auth: AuthService
    ) {}

  ngOnInit(): void {
    this.auth.getCurrUser().subscribe(u => this.currUser = u);
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
}
