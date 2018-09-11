package robot.backend.trade.model.internal;

import java.math.BigDecimal;

public class Position {
    String id;
    PairType pair;
    PositionType type;
    BigDecimal openMoneyAmount;
    BigDecimal totalPositionAmount;
    BigDecimal laverage;

    BigDecimal openPrice;
    BigDecimal currentClosePrice;
    BigDecimal cryptoExchangeStopLossPrice;

    BigDecimal stopLossPrice;
    BigDecimal takeProfitPrice;

    BigDecimal fee;
    BigDecimal pl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PairType getPair() {
        return pair;
    }

    public void setPair(PairType pair) {
        this.pair = pair;
    }

    public PositionType getType() {
        return type;
    }

    public void setType(PositionType type) {
        this.type = type;
    }

    public BigDecimal getOpenMoneyAmount() {
        return openMoneyAmount;
    }

    public void setOpenMoneyAmount(BigDecimal openMoneyAmount) {
        this.openMoneyAmount = openMoneyAmount;
    }

    public BigDecimal getTotalPositionAmount() {
        return totalPositionAmount;
    }

    public void setTotalPositionAmount(BigDecimal totalPositionAmount) {
        this.totalPositionAmount = totalPositionAmount;
    }

    public BigDecimal getCurrentClosePrice() {
        return currentClosePrice;
    }

    public void setCurrentClosePrice(BigDecimal currentClosePrice) {
        this.currentClosePrice = currentClosePrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getCryptoExchangeStopLossPrice() {
        return cryptoExchangeStopLossPrice;
    }

    public void setCryptoExchangeStopLossPrice(BigDecimal cryptoExchangeStopLossPrice) {
        this.cryptoExchangeStopLossPrice = cryptoExchangeStopLossPrice;
    }

    public BigDecimal getLaverage() {
        return laverage;
    }

    public void setLaverage(BigDecimal laverage) {
        this.laverage = laverage;
    }

    public BigDecimal getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public BigDecimal getTakeProfitPrice() {
        return takeProfitPrice;
    }

    public void setTakeProfitPrice(BigDecimal takeProfitPrice) {
        this.takeProfitPrice = takeProfitPrice;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getPl() {
        return pl;
    }

    public void setPl(BigDecimal pl) {
        this.pl = pl;
    }
}