import {AfterViewInit, Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TickerName} from "../model/ticker.name";
import {TickerInfo} from "../model/ticker.info";

declare const TradingView: any;
@Component({
  selector: 'trade',
  templateUrl: './trade.component.html'
})



export class TradeComponent implements  OnInit, AfterViewInit, OnChanges {

  tickerNames: Array<TickerName>;

  exchanges: Array<String> = new Array<String>();

  tickersOfSelectedExchange: Array<TickerName> = new Array<TickerName>();

  selectedExchange: String;

  selectedTickerCurrency1: String;

  selectedTickerCurrency2: String;

  tickerInfo: TickerInfo;


  constructor(private app: AppService, private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    console.log("Trade Component constructor");

    this.route.params.subscribe(
      (params : Params) => {
        this.init(params["exchange"], params["currency1"], params["currency2"]);
      }
    );
  }

  getTickerNames() {
    return this.http.get<Array<TickerName>>(`http://localhost:8080/get_ticker_names`);
  }

  init(exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl: String) {
    this.exchanges = [];
    this.tickersOfSelectedExchange = [];

    this.getTickerNames().pipe().subscribe(data => {
      this.tickerNames = data;

      for (const t of this.tickerNames) {
        if (this.exchanges.indexOf(t.exchange) == -1) {
          this.exchanges.push(t.exchange);
        }
        console.log(`Ticker: '${t.exchange}' : '${t.currency1}' / '${t.currency2}' `);
      }

      if (exchangeFromUrl != null) {
        this.selectedExchange = exchangeFromUrl;
      } else {
        this.selectedExchange = this.exchanges[0];
      }
      console.log(this.selectedExchange);

      for (const t of this.tickerNames) {
        if (t.exchange == this.selectedExchange) {
          this.tickersOfSelectedExchange.push(t);
          console.log("added to tickersOfSelectedExchange"+ t.currency1 + t.currency2);
        }
      }

      if (currency1FromUrl != null && currency2FromUrl != null) {
        this.selectedTickerCurrency1 = currency1FromUrl;
        this.selectedTickerCurrency2 = currency2FromUrl;
      } else {
        this.selectedTickerCurrency1 = this.tickerNames[0].currency1;
        this.selectedTickerCurrency2 = this.tickerNames[0].currency2;
      }

    });

    this.http.get<TickerInfo>(`http://localhost:8080/get_ticker_info`).pipe().subscribe(data => {
      this.tickerInfo = data;
      console.log("get_ticker_info: " + data.price);
    });
  }

  ngOnInit() {
    let exchangeFromUrl = this.route.snapshot.paramMap.get('exchange');
    let currency1FromUrl = this.route.snapshot.paramMap.get('currency1');
    let currency2FromUrl = this.route.snapshot.paramMap.get('currency2');

    //this.init(exchangeFromUrl, currency1FromUrl, currency2FromUrl);
  }

  getSelectedTicker() {
    return '' + this.selectedTickerCurrency1 + this.selectedTickerCurrency2;
  }

  ngAfterViewInit() {
    new TradingView.widget({
      'container_id': 'technical-analysis',
      "width": '100%',
      "height": '500',
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
