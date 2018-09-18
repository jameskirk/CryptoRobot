import {Injectable, Injector} from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AppService} from "./app.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private inj: Injector) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    let currentUser = localStorage.getItem('token');
    console.log("interceptor, token =" + currentUser);
    if (currentUser ) {
      //TODO: we must validate token and set 'authenticated' -call REST service ping with token.
      this.inj.get(AppService).authenticated = true;
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser}`
        }
      });
    }

    return next.handle(request);
  }
}
