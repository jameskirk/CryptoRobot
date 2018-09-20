package robot.backend.trade;

import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TradeHistoryEntity;

import java.util.Date;
import java.util.List;

public class CryptoExchangeArchiveDao {

    public void saveOrderBook(OrderBook orderBook) {
        throw new UnsupportedOperationException();
    }

    public void saveTradeHistory(List<TradeHistoryEntity> tradeHistory) {
        throw new UnsupportedOperationException();
    }

    public OrderBook getOrderBook(Date date) {
        throw new UnsupportedOperationException();
    }

    public List<TradeHistoryEntity> getTradeHistory() {
        throw new UnsupportedOperationException();
    }
}
