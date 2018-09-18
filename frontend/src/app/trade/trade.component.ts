import {AfterViewInit, Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { AppService } from '../app.service';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {TickerName} from "../model/ticker.name";
import {TickerInfo} from "../model/ticker.info";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

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

  init(exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl: String) {
    this.exchanges = [];
    this.tickersOfSelectedExchange = [];

    Observable.forkJoin(
      this.http.get<Array<TickerName>>(`http://localhost:8080/get_ticker_names`).map((response:Array<TickerName>) => response),
      this.http.get<TickerInfo>(`http://localhost:8080/get_ticker_info`).map((response:TickerInfo) => response)
    ).subscribe(
      data => {
        this.tickerNames = data[0];
        this.handleTickerNames(this.tickerNames, exchangeFromUrl, currency1FromUrl, currency2FromUrl);
        this.tickerInfo = data[1];
        new TradingView.widget({
          'container_id': 'technical-analysis',
          "width": '100%',
          "height": '500',
          'symbol': ''+this.selectedTickerCurrency1 + this.selectedTickerCurrency2,
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
      },
      error => console.error(error)
    );
  }

  handleTickerNames(tickerNames:Array<TickerName>,exchangeFromUrl: String, currency1FromUrl: String, currency2FromUrl: String) {
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
  }

  getSelectedTicker() {
    return '' + this.selectedTickerCurrency1 + this.selectedTickerCurrency2;
  }

  ngAfterViewInit() {
    // new TradingView.widget({
    // moved to init
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log("on changes");
  }

}
