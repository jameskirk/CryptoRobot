package robot.backend.trade.exchange;

import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.internal.OrderBook;
import robot.backend.trade.model.internal.PairType;
import robot.backend.trade.model.internal.Position;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoExchange {

    public CryptoExchangeName getName();

    public List<PairType> getPairs() throws Exception;

    public BigDecimal getPrice(PairType pairType) throws Exception;

    public List<Position> getOpenPosition(PairType pairType) throws Exception;

    public OrderBook getOrderBook(PairType pairType) throws Exception;


}
