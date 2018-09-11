package robot.backend.trade.exchange;

import robot.backend.trade.RestHelper;
import robot.backend.trade.model.dto.BitfinexPrice;
import robot.backend.trade.model.internal.CryptoExchangeName;
import robot.backend.trade.model.internal.OrderBook;
import robot.backend.trade.model.internal.PairType;
import robot.backend.trade.model.internal.Position;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class BitfinexCryptoExhange implements CryptoExchange {
    @Override
    public CryptoExchangeName getName() {
        return CryptoExchangeName.Bitfinex;
    }

    @Override
    public BigDecimal getPrice(PairType pairType) throws Exception {
        BitfinexPrice bitfinex = new RestHelper().mapDtoFromUrl("https://api.bitfinex.com/v1/pubticker/" + pairType.getCurrency1() + pairType.getCurrency2(), BitfinexPrice.class);
        return new BigDecimal(bitfinex.last_price);
    }

    @Override
    public List<Position> getOpenPosition(PairType pairType) throws Exception {
        return null;
    }

    @Override
    public List<PairType> getPairs() throws Exception {
        return Arrays.asList(PairType.BTC_USD, PairType.ETH_USD);
    }

    @Override
    public OrderBook getOrderBook(PairType pairType) throws Exception {
        OrderBook retVal = new OrderBook();
        return retVal;
    }


}
