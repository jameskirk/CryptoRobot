package robot.backend.trade.model.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TickerInfo {
    private BigDecimal price;
    private OrderBook orderBook = new OrderBook();
    private List<TradeHistoryEntity> tradeHistory = new ArrayList<>();

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public List<TradeHistoryEntity> getTradeHistory() {
        return tradeHistory;
    }

    public void setTradeHistory(List<TradeHistoryEntity> tradeHistory) {
        this.tradeHistory = tradeHistory;
    }
}
