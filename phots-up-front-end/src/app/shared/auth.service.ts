import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../model/user';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from "@auth0/angular-jwt";
import { environment as ENV } from 'src/environments/environment';
import { map,tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUser: BehaviorSubject<User> = new BehaviorSubject(null);
  private readonly login_url: string = 'login';
  private readonly reg_url: string = 'signup';
  private jwtService = new JwtHelperService();

  constructor(
    private http: HttpClient,
    private cookie: CookieService
  ) {}


  public registrateUser(regRequest: any){
    return this.http.post<any>(
      ENV.BASE_URL + this.reg_url, regRequest, {observe: 'response'});
  }


  confirmCode(code: string): Observable<Boolean> {
    return this.http.get<Boolean>(ENV.BASE_URL + `confirm/${code}`, {observe: 'body'});
  }


  public login(loginReq: any): Observable<boolean>{
    return this.http.post<any>(
        ENV.BASE_URL + this.login_url, loginReq, {observe: 'response'})
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
    this.setCurrUser(this.cookie.get('jwt'));

    return this.currentUser.asObservable();
  }

  public getToken(): string{
    return this.cookie.get('jwt');
  }

  public logOut(){
    this.currentUser.next(null);
    this.cookie.delete('jwt');
  }

  private setCurrUser(jwt: string){
    if (jwt){
      let decoded = this.jwtService.decodeToken(jwt.substring(7));
      const user: User = {
        id: decoded.userId,
        username: decoded.sub,
        userInfo: null
      };

      this.currentUser.next(user);
    }
  }
}
