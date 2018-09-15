import {AfterViewInit, Component, OnInit} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Router } from '@angular/router';
import {TickerName} from "../model/ticker.name";

declare const TradingView: any;
@Component({
  selector: 'trade',
  templateUrl: './trade.component.html'
})



export class TradeComponent implements  OnInit, AfterViewInit {

  tickerNames: Array<TickerName>;

  exchanges: Array<String> = new Array<String>();

  selectedExchange: String;

  tickers: Array<String> = new Array<String>();

  selectedTicker: String;

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
    console.log("Trade Component constructor");
  }

  getTickerNames() {
    return this.http.get<Array<TickerName>>(`http://localhost:8080/ticker_names`);
  }

  ngOnInit() {
    this.getTickerNames().pipe().subscribe(data => {
      this.tickerNames = data;

      for (const t of this.tickerNames) {
        if (this.exchanges.indexOf(t.exchange) == -1) {
          this.exchanges.push(t.exchange);
        }
        console.log(`Ticker: '${t.exchange}' : '${t.ticker1}' / '${t.ticker2}' `);
      }

      this.selectedExchange = this.exchanges[0];

      for (const t of this.tickerNames) {
        if (t.exchange == this.selectedExchange) {
          this.tickers.push(t.ticker1 + t.ticker2);
        }
      }
      this.selectedTicker = this.tickers[0];

    });


  }

  ngAfterViewInit() {
    new TradingView.widget({
      'container_id': 'technical-analysis',
      "width": '100%',
      "height": '610',
      'symbol': 'BTCUSD',
      'interval': '60',
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
