package robot.backend.trade.exchange;

import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.internal.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockCryptoExchange implements CryptoExchange {

    private CryptoExchangeName name;

    public MockCryptoExchange(CryptoExchangeName name) {
        this.name = name;
    }

    public CryptoExchangeName getName() {
        return  name;
    }

    @Override
    public BigDecimal getPrice(PairType pairType) throws Exception {
        if (pairType == PairType.BTC_USD) {
            return new BigDecimal(6423);
        } else {
            return new BigDecimal(454);
        }
    }

    @Override
    public List<Position> getOpenPosition(PairType pairType) throws Exception {
        List<Position> retVal = new ArrayList<>();
        if (pairType == PairType.BTC_USD) {
            Position position = new Position();
            position.setPair(pairType);
            position.setType(PositionType.LONG);
            position.setOpenMoneyAmount(new BigDecimal(150));
            position.setLaverage(new BigDecimal(2));
            position.setFee(new BigDecimal(3.40));
            position.setOpenPrice(new BigDecimal(6310));
            position.setCurrentClosePrice(new BigDecimal(6423));
            retVal.add(position);

        } else if (pairType == PairType.ETH_USD) {
            Position position = new Position();
            position.setPair(pairType);
            position.setType(PositionType.LONG);
            position.setOpenMoneyAmount(new BigDecimal(457));
            position.setLaverage(new BigDecimal(2));
            position.setFee(new BigDecimal(1.42));
            position.setOpenPrice(new BigDecimal(437));
            position.setCurrentClosePrice(new BigDecimal("452.31"));
            retVal.add(position);

            Position position2 = new Position();
            position2.setPair(pairType);
            position2.setType(PositionType.LONG);
            position2.setOpenMoneyAmount(new BigDecimal(521));
            position2.setLaverage(new BigDecimal(2));
            position2.setFee(new BigDecimal(7.22));
            position2.setOpenPrice(new BigDecimal(439));
            position2.setCurrentClosePrice(new BigDecimal("452.34"));
            retVal.add(position2);
        }
        return retVal;
    }

    @Override
    public List<PairType> getPairs() throws Exception {
        return Arrays.asList(PairType.BTC_USD, PairType.ETH_USD);
    }

    @Override
    public OrderBook getOrderBook(PairType pairType) throws Exception {
        OrderBook retVal = new OrderBook();
        Order order = new Order();
        order.setAmount(new BigDecimal("123.1"));
        order.setPrice(new BigDecimal(6201));
        order.setTotalCost(new BigDecimal("2414"));
        retVal.getSellOrders().add(order);

        Order order3 = new Order();
        order3.setAmount(new BigDecimal("126.1"));
        order3.setPrice(new BigDecimal(6203));
        order.setTotalCost(new BigDecimal("2524"));
        retVal.getSellOrders().add(order3);

        Order order2 = new Order();
        order2.setAmount(new BigDecimal("243.1"));
        order2.setPrice(new BigDecimal(6197));
        order.setTotalCost(new BigDecimal("2315"));
        retVal.getBuyOrders().add(order2);
        return retVal;
    }
}
