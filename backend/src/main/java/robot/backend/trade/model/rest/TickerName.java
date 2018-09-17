package robot.backend.trade.model.rest;

import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.contant.Currency;

public class TickerName {

    private String exchange;

    private String currency1;

    private String currency2;

    public TickerName(CryptoExchangeName exchange, Currency currency1, Currency currency2) {
        this.exchange = exchange.toString();
        this.currency1 = currency1.toString();
        this.currency2 = currency2.toString();
    }

    public TickerName(Currency currency1, Currency currency2) {
        this.currency1 = currency1.toString();
        this.currency2 = currency2.toString();
    }

    public TickerName(String exchange, String currency1, String currency2) {
        this.exchange = exchange;
        this.currency1 = currency1;
        this.currency2 = currency2;
    }

    public TickerName(CryptoExchangeName exchange, String currency1, String currency2) {
        this.exchange = exchange.toString();
        this.currency1 = currency1;
        this.currency2 = currency2;
    }

    public String getPairTypeTradingView() {
        return currency1 + currency2;
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
