package robot.backend.trade.model.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderBook {

    private List<Order> sellOrders = new ArrayList<>();

    private List<Order> buyOrders = new ArrayList<>();

    private BigDecimal sellTotal;

    private BigDecimal buyTotal;

    public List<Order> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<Order> sellOrders) {
        this.sellOrders = sellOrders;
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(List<Order> buyOrders) {
        this.buyOrders = buyOrders;
    }

    public BigDecimal getSellTotal() {
        return sellTotal;
    }

    public void setSellTotal(BigDecimal sellTotal) {
        this.sellTotal = sellTotal;
    }

    public BigDecimal getBuyTotal() {
        return buyTotal;
    }

    public void setBuyTotal(BigDecimal buyTotal) {
        this.buyTotal = buyTotal;
    }
}
