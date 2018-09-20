import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpHeaders,
  HttpParams,
} from '@angular/common/http';
import {environment} from '../environments/environment';

//(window as any).global = window;

@Injectable()
export class AppService {

  private authenticated: boolean;

  constructor(private http: HttpClient) {
    console.log("AppService constructor");
    //TODO: call backend:ping with token
    this.authenticated = localStorage.getItem('token') != null;
  }

  isAuthenticated():boolean {
    return this.authenticated;
  }

  login(username: string, password: string) {
    console.log("AppService login", username, password);

    var headers = new HttpHeaders();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');


    let body = new HttpParams();
    body = body.set('username', username);
    body = body.set('password', password);

    this.http.post(environment.restApiUrl + `/login`, body, {headers: headers, observe: 'response'})
      .subscribe(
        (res) => {
          console.log("token=" + res.headers.get("token"));
          localStorage.setItem('token', res.headers.get("token"));
          this.authenticated = true;
        }
      );
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('token');
    this.authenticated = false;
  }

}
