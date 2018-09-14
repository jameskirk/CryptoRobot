package robot.backend.trade.model.rest;

public class TickerName {

    private String exchange;

    private String ticker1;

    private String ticker2;

    public TickerName(String exchange, String ticker1, String ticker2) {
        this.exchange = exchange;
        this.ticker1 = ticker1;
        this.ticker2 = ticker2;
    }

    public String getTicker1() {
        return ticker1;
    }

    public void setTicker1(String ticker1) {
        this.ticker1 = ticker1;
    }

    public String getTicker2() {
        return ticker2;
    }

    public void setTicker2(String ticker2) {
        this.ticker2 = ticker2;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
