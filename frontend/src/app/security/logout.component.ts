import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'logout',
  templateUrl: './logout.component.html'
})
export class LogoutComponent {

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    console.log("LogoutComponent constructor");
    this.app.logout();
  }

}
