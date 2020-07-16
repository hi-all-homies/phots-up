import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';


@Injectable()
export class TokenAdder implements HttpInterceptor {
    
    constructor(
        private auth: AuthService,
        private router: Router
        ){}

    intercept(req: HttpRequest<any>, next: HttpHandler):
        Observable<HttpEvent<any>> {
        
        let path = this.router.url;
        if (path === '/login')
            return next.handle(req);
        else{
            let jwt = this.auth.getToken();
            let authReq = req.clone({
                headers: req.headers.set('Authorization', jwt)
            });
            return next.handle(authReq);
        }
    }
}