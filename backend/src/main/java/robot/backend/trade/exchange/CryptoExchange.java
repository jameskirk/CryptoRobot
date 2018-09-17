package robot.backend.trade.exchange;

import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoExchange {

    public CryptoExchangeName getExchangeName();

    public List<TickerName> getTickers();

    public BigDecimal getPrice(TickerName ticker) throws Exception;

    public OrderBook getOrderBook(TickerName pairType) throws Exception;

    public List<TradeHistoryEntity>  getTradeHistory(TickerName pairType) throws Exception;


}
