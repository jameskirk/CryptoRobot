package robot.backend.trade.model.dto;

public class CexIoFullPosition {
    public String id;
    public String pair;
    public String ptype;
    public String oprice;
    public String stopLossPrice;
    public String omamount; // 1000 usd
    public String pamount; // 3000 usd
    public String leverage;
    public String tfeeAmount;

}
