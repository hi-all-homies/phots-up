import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import{ map } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private auth: AuthService,
    private router: Router
    ){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    
    return this.auth.getCurrentUser()
      .pipe(map(
        user => this.isAuthorized(user)));
  }

  private isAuthorized(user: User): boolean{
    if (user == null){
      this.router.navigate(['/login']);
      return false;
    }
    else
      return true;
  }
  
}
