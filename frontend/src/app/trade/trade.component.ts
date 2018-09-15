import {AfterViewInit, Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {TickerName} from "../model/ticker.name";

declare const TradingView: any;
@Component({
  selector: 'trade',
  templateUrl: './trade.component.html'
})



export class TradeComponent implements  OnInit, AfterViewInit, OnChanges {

  tickerNames: Array<TickerName>;

  exchanges: Array<String> = new Array<String>();

  selectedExchange: String;

  tickersOfSelectedExchange: Array<TickerName> = new Array<TickerName>();

  selectedTickerCurrency1: String;

  selectedTickerCurrency2: String;

  constructor(private app: AppService, private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    console.log("Trade Component constructor");
  }

  getTickerNames() {
    return this.http.get<Array<TickerName>>(`http://localhost:8080/ticker_names`);
  }

  ngOnInit() {
    let exchange = this.route.snapshot.paramMap.get('exchange');
    let currency1 = this.route.snapshot.paramMap.get('currency1');
    let currency2 = this.route.snapshot.paramMap.get('currency2');

    this.getTickerNames().pipe().subscribe(data => {
      this.tickerNames = data;

      for (const t of this.tickerNames) {
        if (this.exchanges.indexOf(t.exchange) == -1) {
          this.exchanges.push(t.exchange);
        }
        console.log(`Ticker: '${t.exchange}' : '${t.ticker1}' / '${t.ticker2}' `);
      }

      if (exchange != null) {
        this.selectedExchange = exchange;
      } else {
        this.selectedExchange = this.exchanges[0];
      }
      console.log(this.selectedExchange);

      for (const t of this.tickerNames) {
        if (t.exchange == this.selectedExchange) {
          this.tickersOfSelectedExchange.push(t);
          console.log("added to tickersOfSelectedExchange"+ t.ticker1 + t.ticker2);
        }
      }

      if (currency1 != null && currency2 != null) {
        this.selectedTickerCurrency1 = currency1;
        this.selectedTickerCurrency2 = currency2;
      } else {
        this.selectedTickerCurrency1 = this.tickerNames[0].ticker1;
        this.selectedTickerCurrency2 = this.tickerNames[0].ticker2;
      }

    });
  }

  getSelectedTicker() {
    return '' + this.selectedTickerCurrency1 + this.selectedTickerCurrency2;
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

  ngOnChanges(changes: SimpleChanges): void {
    console.log("on changes");
  }

}
