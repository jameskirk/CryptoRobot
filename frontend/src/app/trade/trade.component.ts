import {AfterViewInit, Component, OnInit} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';

declare const TradingView: any;
@Component({
  selector: 'trade',
  templateUrl: './trade.component.html'
})



export class TradeComponent implements AfterViewInit {


  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    console.log("Trade Component constructor");
  }

  ngAfterViewInit() {
    new TradingView.widget({
      'container_id': 'technical-analysis',
      "width": '100%',
      "height": '610',
      'symbol': 'BTCUSD',
      'interval': '120',
      'timezone': 'exchange',
      'theme': 'Light',
      'style': '1',
      'toolbar_bg': '#f1f3f6',
      'withdateranges': true,
      'hide_side_toolbar': false,
      'allow_symbol_change': true,
      'save_image': false,
      'hideideas': true,
      'studies': [
        'MASimple@tv-basicstudies' ],
      'show_popup_button': true,
      'popup_width': '1000',
      'popup_height': '650'
    });
  }

}
