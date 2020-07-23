import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from './add-post/add-post.component';
import { DataTransferService } from '../shared/data-transfer.service';
import { NotificationService } from '../shared/notification.service';
import { NotificationsService } from 'angular2-notifications';
import { Notification } from '../model/notifications/notification';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy{

  constructor(
    private dialog: MatDialog,
    private transferService: DataTransferService,
    private notifyService: NotificationService,
    private toastsService: NotificationsService,
    private router: Router
    ) {}

  ngOnInit(): void {
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
    
    notif.click.subscribe(
      click => this.router.navigate(['home/post'], {
        queryParams: {id: notif.context.id}
      }))
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
    this.router.navigate(['home/recommend']);
  }
}
