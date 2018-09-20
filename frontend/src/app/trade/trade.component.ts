import {AfterViewInit, Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TickerName} from "../model/ticker.name";
import {TickerInfo} from "../model/ticker.info";
import {Observable, Subscribable} from "rxjs/Observable";
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {Subscription} from "rxjs/Subscription";
import {environment} from "../../environments/environment";

declare const TradingView: any;
@Component({
  selector: 'trade',
  templateUrl: './trade.component.html'
})

export class TradeComponent implements  OnInit, OnDestroy , AfterViewInit, OnChanges {

  tickerNames: Array<TickerName>;

  exchanges: Array<String> = new Array<String>();

  tickersOfSelectedExchange: Array<TickerName> = new Array<TickerName>();

  selectedExchange: String;

  selectedTickerCurrency1: String;

  selectedTickerCurrency2: String;

  tickerInfo: TickerInfo;

  params: Params;

  subscriptionTimer: Subscription;

  constructor(private app: AppService, private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    console.log("Trade Component constructor");
    console.log("apiURL=" + environment.restApiUrl);

    this.route.params.subscribe(
      (params : Params) => {
        this.params = params;
        this.init(params["exchange"], params["currency1"], params["currency2"]);
      }
    );
  }

  delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }

  async init(exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl) {
    this.exchanges = [];
    this.tickersOfSelectedExchange = [];

    //await this.delay(3000);

    Observable.forkJoin(
      this.http.get<Array<TickerName>>(environment.restApiUrl +`/get_ticker_names`).map((response: Array<TickerName>) => response),
      this.http.get<TickerInfo>(environment.restApiUrl +`/get_ticker_info`).map((response: TickerInfo) => response)
    ).subscribe(
      data => {
        this.tickerNames = data[0];
        this.handleTickerNames(this.tickerNames, exchangeFromUrl, currency1FromUrl, currency2FromUrl);
        this.tickerInfo = data[1];
        new TradingView.widget({
          'container_id': 'technical-analysis',
          "width": '100%',
          "height": '500',
          'symbol': '' + this.selectedTickerCurrency1 + this.selectedTickerCurrency2,
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
            'MASimple@tv-basicstudies'],
          'show_popup_button': true,
          'popup_width': '1000',
          'popup_height': '650'
        });
      },
      error => console.error(error)
    );
  }

  refresh(exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl) {
    this.http.get<TickerInfo>(environment.restApiUrl +`/get_ticker_info`).map((response:TickerInfo) => response).subscribe(
      data => {
        this.tickerInfo = data;
      }
    );
  }

  handleTickerNames(tickerNames:Array<TickerName>,exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl: String) {
    console.log("URL params:"+exchangeFromUrl+ currency2FromUrl+ currency1FromUrl)
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
  }

  ngOnInit() {
    this.subscriptionTimer = Observable.timer(2000,1000) // 1 sec reload orderBook, tradeHistory
      .subscribe((val) => { console.log('called'); this.refresh(this.params["exchange"], this.params["currency1"], this.params["currency2"]);});
  }

  ngOnDestroy(){
    console.log("Destroy timer");
    this.subscriptionTimer.unsubscribe();

  }


  ngAfterViewInit() {
    // new TradingView.widget({
    // moved to init
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log("on changes");
  }

}
