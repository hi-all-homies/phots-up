import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { WallComponent } from './home/wall/wall.component';
import { LoginComponent } from './login/login.component';
import { SignInComponent } from './login/sign-in/sign-in.component';
import { SignUpComponent } from './login/sign-up/sign-up.component';
import { PostDetailsComponent } from './home/post-details/post-details.component';
import { RecommendComponent } from './home/recommend/recommend.component';


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
    children: [
      {path: '', component: WallComponent},
      {path: 'post', component: PostDetailsComponent},
      {path: 'recommend', component: RecommendComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
