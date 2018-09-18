import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls : ['./login.component.css']
})
export class LoginComponent {

  credentials = {username: '', password: ''};

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    console.log("Login Component constructor");
  }

  async onSubmit() {
    console.log("Login Component onSubmit");
    this.app.login(this.credentials.username, this.credentials.password);
    // TODO: remove delay and make app.login - wait REST response
    await this.delay(1000);
    return this.router.navigateByUrl('/trade');

  }
  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

}
