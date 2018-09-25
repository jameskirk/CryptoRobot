package robot.backend.trade.exchange.bitfinex.model;

import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.contant.Currency;
import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;
import robot.backend.trade.util.RestHelper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BitfinexCryptoExhange implements CryptoExchange {
    @Override
    public CryptoExchangeName getExchangeName() {
        return CryptoExchangeName.bitfinex;
    }

    @Override
    public List<TickerName> getTickers() {
        return Arrays.asList(new TickerName(getExchangeName(), Currency.BTC, Currency.USD),
                new TickerName(getExchangeName(), Currency.ETH, Currency.USD),
        new TickerName(getExchangeName(), Currency.XRP, Currency.USD));
    }

    @Override
    public BigDecimal getPrice(TickerName ticker) throws Exception {
        BitfinexPrice bitfinex = new RestHelper().mapDtoFromUrl("https://api.bitfinex.com/v1/pubticker/" + ticker.getCurrency1() + ticker.getCurrency2(), BitfinexPrice.class);
        return new BigDecimal(bitfinex.last_price);
    }

    @Override
    public OrderBook getOrderBook(TickerName pairType) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TradeHistoryEntity> getTradeHistory(TickerName pairType) throws Exception {
        throw new UnsupportedOperationException();
    }

}
