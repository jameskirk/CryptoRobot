package robot.backend.trade.model.contant;

public enum CryptoExchangeName {
    bitfinex, bitmex, cexio;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
