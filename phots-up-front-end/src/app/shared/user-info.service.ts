import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { UserInfo } from '../model/user-info';
import { environment as ENV } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { StringUtils } from './string-utils';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  constructor(private http: HttpClient) {}

  public getUserInfoByUserId(userId: any){
    return this.http.get<UserInfo>(
        ENV.BASE_URL + `profile/${userId}`, {observe: 'response'})
      .pipe(
        map(resp => this.handleAvatar(resp)))
  }

  private handleAvatar(resp: HttpResponse<UserInfo>): UserInfo | null{
    let userInfo = resp.body;
    if (userInfo){
      if (userInfo.avatar){
        let img = StringUtils.getImageString64(
          userInfo.avatarKey, userInfo.avatar);
        userInfo.avatar = img;
      }
      return userInfo;
    }
    else return null;
  }

  public saveUserInfo(userInfo: UserInfo, avatar?: File){
    let fd = new FormData();

    let jsonUserInfo = JSON.stringify(userInfo);
    fd.append('userInfo', jsonUserInfo);

    if (avatar)
      fd.append('avatar', avatar);
    
    return this.http.post<UserInfo>(
      ENV.BASE_URL+'profile', fd, {observe: 'response'})
  }
}
