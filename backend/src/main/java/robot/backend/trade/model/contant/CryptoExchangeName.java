package robot.backend.trade.model.contant;

public enum CryptoExchangeName {
    bitfinex, bitmex, cexio, mock;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
