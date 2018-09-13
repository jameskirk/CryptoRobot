import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaderResponse,
  HttpHeaders,
  HttpParams,
  HttpResponse
} from '@angular/common/http';
import {catchError, map, tap} from "rxjs/operators";

@Injectable()
export class AppService {

  public authenticated = false;

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    console.log("AppService login", username, password);

    var headers = new HttpHeaders();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');


    let body = new HttpParams();
    body = body.set('username', username);
    body = body.set('password', password);

    this.http.post(`http://localhost:8080/login`, body, {headers:headers,observe: 'response' })
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
