import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'trade',
  templateUrl: './trade.component.html'
})
export class TradeComponent {

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    console.log("Trade Component constructor");
  }

}
