package robot.backend.trade.exchange.bitmex.model;

import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.util.SizedStack;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class BitmexTickerInfo {

    private TickerName tickerName;

    public BitmexTickerInfo(TickerName tickerName) {
        this.tickerName = tickerName;
    }

    private Map<String, OrderBookL2Data> orderBookSocket = new ConcurrentHashMap<>();

    private Stack<TradeData> historySocket = new SizedStack<>(20);

    public Map<String, OrderBookL2Data> getOrderBookSocket() {
        return orderBookSocket;
    }

    public void setOrderBookSocket(Map<String, OrderBookL2Data> orderBookSocket) {
        this.orderBookSocket = orderBookSocket;
    }

    public Stack<TradeData> getHistorySocket() {
        return historySocket;
    }

    public void setHistorySocket(Stack<TradeData> historySocket) {
        this.historySocket = historySocket;
    }

    public TickerName getTickerName() {
        return tickerName;
    }

    @Override
    public boolean equals(Object obj) {
        BitmexTickerInfo bitmexTickerInfo = (BitmexTickerInfo) obj;
        return tickerName.equals(bitmexTickerInfo.getTickerName());
    }
}