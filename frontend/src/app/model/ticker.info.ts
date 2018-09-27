export class TickerInfo {
  price: string;
  orderBook: OrderBook;
  tradeHistory: Array<TradeHistoryEntity>;
}

 export class OrderBook {
  ask: Array<OrderBookEntity>;
  askSum: number;
  bid: Array<OrderBookEntity>;
  bidSum: number;


}

export class OrderBookEntity {
  price: number;
  amount: number;
  sum: number;
}

export class TradeHistoryEntity {
  type: string;
  amount: number;
  price: number;
  date: string;
}
