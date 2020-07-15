import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../model/user';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from "@auth0/angular-jwt";
import { Url } from './base-url';
import { map,tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentUser: BehaviorSubject<User> = new BehaviorSubject(null);
  private readonly url: string = 'login';
  jwtService = new JwtHelperService();

  constructor(
    private http: HttpClient,
    private cookie: CookieService
  ) {}


  public login(loginReq: any): Observable<boolean>{
    return this.http.post<any>(
        Url.BASE_URL + this.url, loginReq, {observe: 'response'})
      .pipe(
        tap(resp => this.handleResponse(resp)),
        map(resp => this.mapToBoolean(resp)));
  }

  private handleResponse(response: HttpResponse<any>){
    if (response.ok){
      let jwt = <string>response.body.token;
      this.cookie.set('jwt', jwt);
      this.setCurrUser(jwt);
    }
  }

  private mapToBoolean(response: HttpResponse<any>): boolean{
    if (response.ok)
      return true;
    else
      return false;
  }

  public getCurrUser(){
    return this.currentUser.asObservable();
  }

  public getToken(): string{
    return this.cookie.get('jwt');
  }

  private setCurrUser(jwt: string){
    let decoded = this.jwtService.decodeToken(jwt.substring(7));
    const user: User = {
      id: decoded.userId,
      username: decoded.sub
    };

    this.currentUser.next(user);
  }
}
