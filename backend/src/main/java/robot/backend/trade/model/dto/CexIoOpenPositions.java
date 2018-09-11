package robot.backend.trade.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CexIoOpenPositions {
    public String ok;
    public List<CexIoFullPosition> data;
}
