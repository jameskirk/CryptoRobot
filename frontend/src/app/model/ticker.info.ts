export class TickerInfo {
  price: string;
  orderBook: OrderBook;
  tradeHistory: Array<TradeHistoryEntity>;
}

 export class OrderBook {
  ask: Array<OrderBookEntity>;
  bid: Array<OrderBookEntity>;
}

 class OrderBookEntity {
  price: number;
  amount: number;
}

 class TradeHistoryEntity {
  type: string;
  amount: number;
  price: number;
  date: string;
}
