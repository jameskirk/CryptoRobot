package robot.backend.trade.exchange.bitmex;

public class AbstractBitmexResponse {
    private String table;
    private String action;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
