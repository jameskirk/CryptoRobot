package robot.backend.trade.exchange.bitmex;

import java.util.List;

public class TradeResponse extends AbstractBitmexResponse {
    private List<TradeData> data;

    public List<TradeData> getData() {
        return data;
    }

    public void setData(List<TradeData> data) {
        this.data = data;
    }

}
