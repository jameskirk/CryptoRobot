import {AfterViewInit, Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TickerName} from '../model/ticker.name';
import {TickerInfo} from '../model/ticker.info';
import {environment} from '../../environments/environment';
import {Observable, Subscription} from 'rxjs';
import { timer } from 'rxjs';
import { forkJoin } from 'rxjs';
import {map, window} from 'rxjs/operators';
import {_document} from '@angular/platform-browser/src/browser';


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

    const resp1 = await this.http.get<Array<TickerName>>(environment.restApiUrl +`/get_ticker_names`).toPromise();
    this.tickerNames = resp1;

    this.handleTickerNames(this.tickerNames, exchangeFromUrl, currency1FromUrl, currency2FromUrl);

    this.refresh();


    await new TradingView.widget({
      'container_id': 'technical-analysis',
      "width": '100%',
      "height": '500',
      'symbol': '' + this.selectedTickerCurrency1 + this.selectedTickerCurrency2,
      'interval': '60',
      'timezone': 'Europe/Moscow',
      'theme': 'Light',
      'style': '1',
      'toolbar_bg': '#f1f3f6',
      //'withdateranges': true,
      //'hide_side_toolbar': false,
      'save_image': false,
      'hideideas': true,
      'studies': [
        'MASimple@tv-basicstudies'],
      //'show_popup_button': true,
      'popup_width': '1000',
      'popup_height': '650'
    });
    let t = (<any>window).isMobile;
    console.log(t);
  }

  refresh() {
    if (this.selectedExchange == null || this.selectedTickerCurrency1 == null || this.selectedTickerCurrency2 == null) {
      return;
    }
    const body = {
      exchange: this.selectedExchange, currency1: this.selectedTickerCurrency1, currency2: this.selectedTickerCurrency2
    }
    this.http.post<TickerInfo>(environment.restApiUrl +`/get_ticker_info`, body).pipe(map((response:TickerInfo) => response)).subscribe(
      data => {
        this.tickerInfo = data;
      }
    );
  }

  handleTickerNames(tickerNames:Array<TickerName> ,exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl: String) {
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
    this.subscriptionTimer = timer(2000,1000).subscribe((val) => { console.log('called'); this.refresh();});
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
