import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  credentials = {username: '', password: ''};

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    console.log("Login Component constructor");
  }

  onSubmit() {
    console.log("Login Component onSubmit");
    this.app.login(this.credentials.username, this.credentials.password);

    return this.router.navigateByUrl('/login');

  }

}
