package robot.backend.trade.exchange;

import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MockCryptoExchange implements CryptoExchange {

    private CryptoExchangeName name;

    private List<TickerName> tickers;

    public MockCryptoExchange(CryptoExchangeName name, List<TickerName> tickers) {
        this.name = name;
        this.tickers = tickers;
    }

    public CryptoExchangeName getExchangeName() {
        return  name;
    }

    @Override
    public List<TickerName> getTickers() {
        return tickers;
    }

    @Override
    public BigDecimal getPrice(TickerName ticker) throws Exception {
        return (new BigDecimal(6000 + getRand()*4.5));
    }

    @Override
    public OrderBook getOrderBook(TickerName pairType) throws Exception {
        OrderBook retVal = new OrderBook();
        for (int i =0; i< getRand(); i++) {
            retVal.getAsk().add(new robot.backend.trade.model.rest.OrderBook.OrderBookEntity(new BigDecimal(6000+getRand()*2), new BigDecimal(getRand()*1.23)));
            retVal.getBid().add(new robot.backend.trade.model.rest.OrderBook.OrderBookEntity(new BigDecimal(6000-getRand()*2), new BigDecimal(getRand()*1.47)));
        }
        return retVal;
    }

    @Override
    public List<TradeHistoryEntity> getTradeHistory(TickerName pairType) throws Exception {
        List<TradeHistoryEntity> retVal = new ArrayList<>();
        for (int i =0; i< getRand()*1.4; i++) {
            retVal.add(new TradeHistoryEntity("sell", new BigDecimal(getRand() * 0.45), new BigDecimal(6000 + getRand() * 1.2), new Date()));
        }
        return retVal;
    }

    private int getRand() {
        return ThreadLocalRandom.current().nextInt(4, 10 + 1);
    }
}
