package robot.backend.trade.model.internal;

import robot.backend.trade.model.contant.OrderType;

import java.math.BigDecimal;

public class Order {
    private long id;
    private OrderType type;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal totalCost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
