import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { UserInfo } from '../model/user-info';
import { environment as ENV } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { StringUtils } from './string-utils';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  constructor(private http: HttpClient) {}

  public getUserInfoByUserId(userId: any){
    return this.http.get<User>(
        ENV.BASE_URL + `profile/${userId}`, {observe: 'response'})
      .pipe(
        map(resp => this.handleAvatar(resp)))
  }

  private handleAvatar(resp: HttpResponse<User>): User{
    let user = resp.body;
    if (user.userInfo){
      if (user.userInfo.avatar){
        let img = StringUtils.getImageString64(
          user.userInfo.avatarKey, user.userInfo.avatar);
        user.userInfo.avatar = img;
      }
    }
    return user;
  }

  public saveUserInfo(userInfo: UserInfo, avatar?: File){
    let fd = new FormData();

    let jsonUserInfo = JSON.stringify(userInfo);
    fd.append('userInfo', jsonUserInfo);

    if (avatar)
      fd.append('avatar', avatar);
    
    return this.http.post<User>(
      ENV.BASE_URL+'profile', fd, {observe: 'response'})
  }
}
