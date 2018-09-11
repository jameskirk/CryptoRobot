package robot.backend.trade.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CexIoOrderBook {

    public List<List<String>> bids = new ArrayList<>();
    public List<List<String>> asks = new ArrayList<>();

}
