import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'home',
  templateUrl: './home.component.html'
})
export class HomeComponent {

  constructor(public app: AppService, private http: HttpClient, private router: Router) {
    console.log("Home Component constructor");
  }

}
