package robot.backend.trade;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import robot.backend.trade.exchange.CexIoCryptoExchange;
import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.exchange.MockCryptoExchange;
import robot.backend.trade.model.internal.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class TradeController {

    private TradeBean tradeBean;

    private List<CryptoExchange> cryptoExchangeList = Arrays.asList(
            new MockCryptoExchange(CryptoExchangeName.Bitfinex), new CexIoCryptoExchange());


    @RequestMapping("/info")
    @ResponseBody
    @CrossOrigin(origins = "http://localhost:4200")
    public String info() {
        return trade(cryptoExchangeList.get(0).getName().toString(), PairType.BTC_USD.toString());
    }

    public String trade(String cryptoExhangeNameAsString, String pairAsString) {
        try {
            PairType pair;
            try {
                pair = PairType.valueOf(pairAsString);
            } catch (IllegalStateException e) {
                return "pair not found";
            }

            //CryptoExchange cryptoExchange = new CexIoCryptoExchange();
            CryptoExchangeName cryptoExchangeName;
            try {
                cryptoExchangeName = CryptoExchangeName.valueOf(cryptoExhangeNameAsString);
            } catch (IllegalStateException e) {
                return "exhange not found";
            }

            CryptoExchange cryptoExchange = null;
            for (CryptoExchange ce : cryptoExchangeList) {
                if (ce.getName().equals(cryptoExchangeName)) {
                    cryptoExchange = ce;
                }
            }
            if (cryptoExchange == null) {
                return "exhange not found";
            }

            tradeBean = new TradeBean();

            tradeBean.setSelectedCryptoExchangeName(cryptoExchangeName);
            tradeBean.setPairType(pair);
            List<CryptoExchangeName> cryptoExchangeNames = new ArrayList<>();
            for (CryptoExchange ce : cryptoExchangeList) {
                cryptoExchangeNames.add(ce.getName());
            }
            tradeBean.setCryptoExchangeNameList(cryptoExchangeNames);


            for (PairType p : cryptoExchange.getPairs()) {
                tradeBean.getPairList().add(p);
            }

            BigDecimal price = cryptoExchange.getPrice(pair);
            tradeBean.setPrice(price);

            OrderBook orderBook = cryptoExchange.getOrderBook(pair);
            tradeBean.setOrderBook(orderBook);

            List<Position> postionList = cryptoExchange.getOpenPosition(pair);
            tradeBean.setPositions(postionList);
            return tradeBean.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
