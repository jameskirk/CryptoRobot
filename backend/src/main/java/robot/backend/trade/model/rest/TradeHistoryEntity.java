package robot.backend.trade.model.rest;

import java.math.BigDecimal;
import java.util.Date;

public class TradeHistoryEntity {
    private String type;
    private BigDecimal amount;
    private BigDecimal price;
    private Date date;

    public TradeHistoryEntity(String type, BigDecimal amount, BigDecimal price, Date date) {
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

