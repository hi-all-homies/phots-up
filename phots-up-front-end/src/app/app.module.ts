import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { WallComponent } from './home/wall/wall.component';
import { LoginComponent } from './login/login.component';
import { SignInComponent } from './login/sign-in/sign-in.component';
import { SignUpComponent } from './login/sign-up/sign-up.component';
import { AddPostComponent } from './home/add-post/add-post.component';
import { MatDialogRef } from '@angular/material/dialog';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from './shared/auth.service';
import { TokenAdder } from './shared/token-adder';
import { EventSourceService } from './shared/event-source.service';
import { PostService } from './shared/post.service';
import { DataTransferService } from './shared/data-transfer.service';
import { PostDetailsComponent } from './home/post-details/post-details.component';
import { CommentService } from './shared/comment.service';
import { NotificationService } from './shared/notification.service';
import { SimpleNotificationsModule } from 'angular2-notifications';
import { PostCardComponent } from './home/post-card/post-card.component';
import { RecommendComponent } from './home/recommend/recommend.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    WallComponent,
    LoginComponent,
    SignInComponent,
    SignUpComponent,
    AddPostComponent,
    PostDetailsComponent,
    PostCardComponent,
    RecommendComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    SimpleNotificationsModule.forRoot()
  ],
  providers: [
    CookieService,
    AuthService,
    EventSourceService,
    PostService,
    CommentService,
    DataTransferService,
    NotificationService,
    {provide: MatDialogRef, useValue: {}},
    {provide: HTTP_INTERCEPTORS, useClass: TokenAdder, multi:true}
  ],
  entryComponents: [AddPostComponent],
  bootstrap: [AppComponent]
})
export class AppModule {}