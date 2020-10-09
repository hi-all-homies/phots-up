import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { WallComponent } from './home/wall/wall.component';
import { LoginComponent } from './login/login.component';
import { SignInComponent } from './login/sign-in/sign-in.component';
import { SignUpComponent } from './login/sign-up/sign-up.component';
import { PostDetailsComponent } from './home/post-details/post-details.component';
import { RecommendComponent } from './home/recommend/recommend.component';
import { AuthGuard } from './shared/auth.guard';
import { UserProfileComponent } from './home/user-profile/user-profile.component';
import { ConfirmComponent } from './login/confirm/confirm.component';


const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {
    path: 'login',
    component: LoginComponent,
    children: [
      {path: '', component: SignInComponent},
      {path: 'reg', component: SignUpComponent}
    ]
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [AuthGuard],
    children: [
      {path: '', component: WallComponent},
      {path: 'post', component: PostDetailsComponent},
      {path: 'recommend', component: RecommendComponent},
      {path: 'profile', component: UserProfileComponent}
    ]
  },
  {path: 'confirm/:code', component: ConfirmComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
