package robot.backend.trade.model.mapping;

import robot.backend.trade.model.internal.PairType;
import robot.backend.trade.model.internal.Position;
import robot.backend.trade.model.dto.CexIoFullPosition;

import java.math.BigDecimal;

public class PositionMapper {

    public Position mapFromCexIo(CexIoFullPosition cexIoFullPosition) {
        Position retVal = new Position();
        retVal.setId(cexIoFullPosition.id);
        String pair = cexIoFullPosition.pair.replace(":", "_");
        retVal.setPair(PairType.valueOf(pair));
        retVal.setOpenMoneyAmount(new BigDecimal(cexIoFullPosition.omamount));
        retVal.setLaverage(new BigDecimal(cexIoFullPosition.leverage));
        retVal.setOpenPrice(new BigDecimal(cexIoFullPosition.oprice));
        retVal.setStopLossPrice(new BigDecimal(cexIoFullPosition.stopLossPrice));
        return  retVal;
    }
}
