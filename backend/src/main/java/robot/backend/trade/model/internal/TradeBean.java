package robot.backend.trade.model.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TradeBean {

    /** variables defined by portal or user selects it*/
    private List<CryptoExchangeName> cryptoExchangeNameList = new ArrayList<>();

    private List<PairType> pairList = new ArrayList<>();

    private CryptoExchangeName selectedCryptoExchangeName;

    private PairType pairType;

    /** from REST services */
    private BigDecimal price;

    private OrderBook orderBook = new OrderBook();

    private List<Position> positions = new ArrayList<>();

    private List<Order> myOrders = new ArrayList<>();

    public List<PairType> getPairList() {
        return pairList;
    }

    public void setPairList(List<PairType> pairList) {
        this.pairList = pairList;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<CryptoExchangeName> getCryptoExchangeNameList() {
        return cryptoExchangeNameList;
    }

    public void setCryptoExchangeNameList(List<CryptoExchangeName> cryptoExchangeNameList) {
        this.cryptoExchangeNameList = cryptoExchangeNameList;
    }

    public CryptoExchangeName getSelectedCryptoExchangeName() {
        return selectedCryptoExchangeName;
    }

    public void setSelectedCryptoExchangeName(CryptoExchangeName selectedCryptoExchangeName) {
        this.selectedCryptoExchangeName = selectedCryptoExchangeName;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public List<Order> getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    public PairType getPairType() {
        return pairType;
    }

    public void setPairType(PairType pairType) {
        this.pairType = pairType;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
