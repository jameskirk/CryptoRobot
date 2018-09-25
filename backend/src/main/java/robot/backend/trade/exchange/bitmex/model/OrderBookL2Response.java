package robot.backend.trade.exchange.bitmex.model;

import robot.backend.trade.exchange.bitmex.AbstractBitmexResponse;

import java.util.List;

public class OrderBookL2Response extends AbstractBitmexResponse {

    private List<OrderBookL2Data> data;

    public List<OrderBookL2Data> getData() {
        return data;
    }

    public void setData(List<OrderBookL2Data> data) {
        this.data = data;
    }
}
