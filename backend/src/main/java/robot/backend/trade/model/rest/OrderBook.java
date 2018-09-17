package robot.backend.trade.model.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderBook {

    private List<OrderBookEntity> ask = new ArrayList<>();

    private List<OrderBookEntity> bid = new ArrayList<>();

    public List<OrderBookEntity> getAsk() {
        return ask;
    }

    public void setAsk(List<OrderBookEntity> ask) {
        this.ask = ask;
    }

    public List<OrderBookEntity> getBid() {
        return bid;
    }

    public void setBid(List<OrderBookEntity> bid) {
        this.bid = bid;
    }

    public static class OrderBookEntity {
        private BigDecimal price;
        private BigDecimal amount;

        public OrderBookEntity(BigDecimal price, BigDecimal amount) {
            this.price = price;
            this.amount = amount;
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
    }
}
