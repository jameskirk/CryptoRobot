package robot.backend.trade.model.rest;

public class TickerName {

    private String exchange;

    private String currency1;

    private String currency2;

    public TickerName(String exchange, String currency1, String currency2) {
        this.exchange = exchange;
        this.currency1 = currency1;
        this.currency2 = currency2;
    }

    public String getCurrency1() {
        return currency1;
    }

    public void setCurrency1(String currency1) {
        this.currency1 = currency1;
    }

    public String getCurrency2() {
        return currency2;
    }

    public void setCurrency2(String currency2) {
        this.currency2 = currency2;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
