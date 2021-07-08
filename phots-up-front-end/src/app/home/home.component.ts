import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddPostComponent } from './add-post/add-post.component';
import { DataTransferService } from '../shared/data-transfer.service';
import { NotificationService } from '../shared/notification.service';
import { NotificationsService } from 'angular2-notifications';
import { Notification } from '../model/notifications/notification';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { User } from '../model/user';
import { first } from 'rxjs/operators';
import { PostDetailsComponent } from './post-details/post-details.component';
import { PostService } from '../shared/post.service';

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
    private auth: AuthService,
    private postService: PostService
    ) {}

  ngOnInit(): void {
    this.auth.getCurrentUser()
      .pipe(first())  
      .subscribe(u => {
        this.initUserAndAvatar(u);
        this.notifyService.listen(u.username);
      });
      
    this.notifyService.getNotifications()
      .subscribe(event => this.handleNotifications(event));
  }

  ngOnDestroy(): void {
    this.notifyService.closeSocket();
  }

  private handleNotifications(event: Notification){
    let notif = this.toastsService.info(
      event.getTitle(), event.getContent(),null, {id: event.getPostId()});
    
    notif.click
      .pipe(first())
      .subscribe(click => {
        const id: number = notif.context.id;
        this.openDetailsDialog(id)});
  }

  private openDetailsDialog(id: number){
    this.transferService.getPostsObs()
          .pipe(first())
          .subscribe(posts => {
            let index: number;
            let currentPost = posts.find((v, ind) => {
              index = ind;
              return v.post.id === id})
            
            if (!currentPost){
              this.postService.getPostById(id)
                .pipe(first())
                .subscribe(post => {
                  posts.unshift(post);
                  index = 0;
                  currentPost = post})
            }

            const config: MatDialogConfig = {
              width: '100%',
              position: {top: '2rem'},
              closeOnNavigation: true,
              backdropClass: 'details-backdrop',
              data:{
                index: index,
                currentPost: currentPost,
                posts: posts}};
            
            this.dialog.open(PostDetailsComponent, config)})
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

  readonly blankAvatar: string = 'assets/logo/blank.png';

  private initUserAndAvatar(user: User){
	  this.currUser = user;
    if (!this.currUser.userInfo.avatarUrl)
      this.currUser.userInfo.avatarUrl = this.blankAvatar;
  }

  avatarUrl(): string{
    return `background-image: url(${this.currUser.userInfo.avatarUrl});`
  }
}
