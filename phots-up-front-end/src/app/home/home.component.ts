import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from './add-post/add-post.component';
import { DataTransferService } from '../shared/data-transfer.service';
import { NotificationService } from '../shared/notification.service';
import { NotificationsService } from 'angular2-notifications';
import { Notification } from '../model/notifications/notification';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private dialog: MatDialog,
    private transferService: DataTransferService,
    private notifyService: NotificationService,
    private toastsService: NotificationsService
    ) {}

  ngOnInit(): void {
    this.notifyService.listen();
    this.notifyService.getNotifications()
      .subscribe(event => this.handleNotifications(event));
  }

  private handleNotifications(event: Notification){
    this.toastsService.info(event.getTitle(), event.getContent());
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
}
