package robot.backend.trade.model.internal;

public enum PairType {
    BTC_USD, ETH_USD;

    public String getCurrency1() {
        return this.name().split("_")[0];
    }

    public String getCurrency2() {
        return this.name().split("_")[1];
    }

    public String getSimpleString() {
        return  this.toString().replace("_", "/");
    }

    public String getPairTypeTradingView() {
        return this.toString().replace("_", "");
    }
}
