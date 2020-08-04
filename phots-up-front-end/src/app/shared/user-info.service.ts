import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserInfo } from '../model/user-info';
import { environment as ENV } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  constructor(private http: HttpClient) {}

  public getUserInfoByUserId(userId: any){
    return this.http.get<UserInfo>(
      ENV.BASE_URL + `profile/${userId}`, {observe: 'response'})
  }
}
