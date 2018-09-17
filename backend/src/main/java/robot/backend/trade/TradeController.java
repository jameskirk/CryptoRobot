package robot.backend.trade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.exchange.MockCryptoExchange;
import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.rest.TickerInfo;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class TradeController {

    private Map<String, CryptoExchange> cryptoExchangeList = new HashMap<>();

    public TradeController() {
        cryptoExchangeList.put("bitmex", new MockCryptoExchange(CryptoExchangeName.bitmex));
        cryptoExchangeList.put("bitfinex", new MockCryptoExchange(CryptoExchangeName.bitfinex));
    }

    @RequestMapping(value = "/get_ticker_names")
    @ResponseBody
    public Collection<TickerName> getTickerNames() {
        return Arrays.asList(new TickerName("bitmex","BTC", "USD"),
        new TickerName("bitmex","ETH", "USD"),
                new TickerName("bitfinex", "BTC", "USDT"),
                new TickerName("bitfinex", "ETC", "USDT"));
    }

    @RequestMapping(value = "/get_ticker_info")
    @ResponseBody
    public TickerInfo getTickerInfo() {
        TickerInfo retVal = new TickerInfo();
        // TODO parse param 'ticker'
        int randomInt = ThreadLocalRandom.current().nextInt(0, 10 + 1);
        retVal.setPrice(new BigDecimal(6000+randomInt));
        for (int i =0; i< getRand(); i++) {
            retVal.getOrderBook().getAsk().add(new robot.backend.trade.model.rest.OrderBook.OrderBookEntity(new BigDecimal(6000+getRand()*2), new BigDecimal(getRand()*1.23)));
            retVal.getOrderBook().getBid().add(new robot.backend.trade.model.rest.OrderBook.OrderBookEntity(new BigDecimal(6000-getRand()*2), new BigDecimal(getRand()*1.47)));
        }
        for (int i =0; i< getRand(); i++) {
            retVal.getTradeHistory().add(new TradeHistoryEntity("sell", new BigDecimal(getRand() * 0.45), new BigDecimal(6000 + getRand() * 1.2), new Date()));
        }
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
//                if (ce.getName().equals(cryptoExchangeName)) {
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
//                cryptoExchangeNames.add(ce.getName());
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
