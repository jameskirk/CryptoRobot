package robot.backend.trade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import robot.backend.trade.exchange.BitfinexCryptoExhange;
import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.exchange.MockCryptoExchange;
import robot.backend.trade.exchange.bitmex.BitmexCryptoExchange;
import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.contant.Currency;
import robot.backend.trade.model.rest.TickerInfo;
import robot.backend.trade.model.rest.TickerName;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TradeController {

    private Map<CryptoExchangeName, CryptoExchange> cryptoExchangeList = new HashMap<>();

    public TradeController() {
        cryptoExchangeList.put(CryptoExchangeName.bitmex, new BitmexCryptoExchange());
        cryptoExchangeList.put(CryptoExchangeName.bitfinex, new BitfinexCryptoExhange());

        cryptoExchangeList.put(CryptoExchangeName.mock, new MockCryptoExchange(CryptoExchangeName.bitmex, Arrays.asList(new TickerName(robot.backend.trade.model.contant.Currency.BTC, robot.backend.trade.model.contant.Currency.USD),
                new TickerName(robot.backend.trade.model.contant.Currency.ETH, Currency.USD))));
    }

    @RequestMapping(value = "/get_ticker_names")
    @ResponseBody
    public Collection<TickerName> getTickerNames() {
        List<TickerName> retVal = new ArrayList<>();
        for (Map.Entry<CryptoExchangeName, CryptoExchange> entry: cryptoExchangeList.entrySet()) {
            for (TickerName ticker: entry.getValue().getTickers()) {
                retVal.add(new TickerName(entry.getKey(), ticker.getCurrency1(), ticker.getCurrency2()));
            }

        }
        return retVal;
    }

    @RequestMapping(value = "/get_ticker_info")
    @ResponseBody
    public TickerInfo getTickerInfo() throws Exception {
        CryptoExchangeName cryptoExchangeNameFromInput = CryptoExchangeName.bitmex;
        TickerName tickerNameFromInput = new TickerName(Currency.BTC, Currency.USD);

        TickerInfo retVal = new TickerInfo();
        CryptoExchange cryptoExchange = cryptoExchangeList.get(cryptoExchangeNameFromInput);
        retVal.setPrice(cryptoExchange.getPrice(tickerNameFromInput));
        retVal.setOrderBook(cryptoExchange.getOrderBook(tickerNameFromInput));
        retVal.setTradeHistory(cryptoExchange.getTradeHistory(tickerNameFromInput));
        return retVal;
    }



//    @RequestMapping("/trade_info")
//    @ResponseBody
//    public String getTradeInfo(String cryptoExhangeNameAsString, String pairAsString) {
//        try {
//            PairType pair;
//            try {
//                pair = PairType.valueOf(pairAsString);
//            } catch (IllegalStateException e) {
//                return "pair not found";
//            }
//
//            //CryptoExchange cryptoExchange = new CexIoCryptoExchange();
//            CryptoExchangeName cryptoExchangeName;
//            try {
//                cryptoExchangeName = CryptoExchangeName.valueOf(cryptoExhangeNameAsString);
//            } catch (IllegalStateException e) {
//                return "exhange not found";
//            }
//
//            CryptoExchange cryptoExchange = null;
//            for (CryptoExchange ce : cryptoExchangeList) {
//                if (ce.getExchangeName().equals(cryptoExchangeName)) {
//                    cryptoExchange = ce;
//                }
//            }
//            if (cryptoExchange == null) {
//                return "exhange not found";
//            }
//
//            tradeBean = new TradeBean();
//
//            tradeBean.setSelectedCryptoExchangeName(cryptoExchangeName);
//            tradeBean.setPairType(pair);
//            List<CryptoExchangeName> cryptoExchangeNames = new ArrayList<>();
//            for (CryptoExchange ce : cryptoExchangeList) {
//                cryptoExchangeNames.add(ce.getExchangeName());
//            }
//            tradeBean.setCryptoExchangeNameList(cryptoExchangeNames);
//
//
//            for (PairType p : cryptoExchange.getPairs()) {
//                tradeBean.getPairList().add(p);
//            }
//
//            BigDecimal price = cryptoExchange.getPrice(pair);
//            tradeBean.setPrice(price);
//
//            OrderBook orderBook = cryptoExchange.getOrderBook(pair);
//            tradeBean.setOrderBook(orderBook);
//
//            List<Position> postionList = cryptoExchange.getOpenPosition(pair);
//            tradeBean.setPositions(postionList);
//            return tradeBean.toString();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private int getRand() {
        return ThreadLocalRandom.current().nextInt(4, 10 + 1);
    }

}
