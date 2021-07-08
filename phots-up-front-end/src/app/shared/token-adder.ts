import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';



@Injectable()
export class TokenAdder implements HttpInterceptor {
    
    constructor(private router: Router, private auth: AuthService){}

    intercept(req: HttpRequest<any>, next: HttpHandler):
        Observable<HttpEvent<any>> {
        
        const authReq = req.clone({ withCredentials: true });
        return next.handle(authReq)
          .pipe(
            tap(event => {},
              err => {
                if (err instanceof HttpErrorResponse){
                  if (err.status === 403){
                    this.auth.logOut();
                    this.router.navigate(['/login']);
                  }
                }
              })
          );
        
    }
}