import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../model/user';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { environment as ENV } from 'src/environments/environment';
import { map,tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser: BehaviorSubject<User> = new BehaviorSubject(null);
  private readonly login_url: string = 'login';
  private readonly reg_url: string = 'signup';

  constructor(
    private http: HttpClient,
    private cookie: CookieService
  ) {}


  public registrateUser(regRequest: any){
    return this.http.post<any>(
      ENV.BASE_URL + this.reg_url, regRequest, {observe: 'response'});
  }


  confirmCode(code: string): Observable<boolean> {
    return this.http.get<boolean>(ENV.BASE_URL + `confirm/${code}`, {observe: 'body'});
  }


  public login(loginReq: any): Observable<boolean>{
    return this.http.post<any>(
        ENV.BASE_URL + this.login_url, loginReq, {observe: 'response', withCredentials: true})
      .pipe(
        tap(resp => this.handleResponse(resp)),
        map(resp => this.mapToBoolean(resp)));
  }

  private handleResponse(response: HttpResponse<any>){
    if (response.ok){
      let userId: number = response.body.userId;
      let username: string = response.body.username;
      let avatarUrl: string = response.body.avatarUrl;

      const user: User = {
        id: userId,
        username: username,
        userInfo: {
          id: 0,
          aboutMe: '',
          avatarUrl: avatarUrl}};
        
      const cookieUser = JSON.stringify(user);
      this.cookie.set('user', cookieUser, 1);
      this.currentUser.next(user);
    }
  }

  private mapToBoolean(response: HttpResponse<any>): boolean{
    if (response.ok)
      return true;
    else
      return false;
  }

  public getCurrentUser(){
    this.setCurrentUser(this.cookie.get('user'));

    return this.currentUser.asObservable();
  }

  public logOut(){
    this.currentUser.next(null);
    this.cookie.delete('user');
  }

  private setCurrentUser(user: string){
    if (user){
      const storedUser: User = JSON.parse(user);
      this.currentUser.next(storedUser);
    }
  }
}
