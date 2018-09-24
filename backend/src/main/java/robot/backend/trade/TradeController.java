package robot.backend.trade;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
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
        //cryptoExchangeList.put(CryptoExchangeName.bitfinex, new BitfinexCryptoExhange());

        cryptoExchangeList.put(CryptoExchangeName.mock, new MockCryptoExchange(CryptoExchangeName.bitmex, Arrays.asList(new TickerName(robot.backend.trade.model.contant.Currency.BTC, robot.backend.trade.model.contant.Currency.USD),
                new TickerName(robot.backend.trade.model.contant.Currency.ETH, Currency.USD))));
    }

    @RequestMapping(value = "/get_ticker_names")
    @ResponseBody
    public Collection<TickerName> getTickerNames() {
        try {
            List<TickerName> retVal = new ArrayList<>();
            for (Map.Entry<CryptoExchangeName, CryptoExchange> entry : cryptoExchangeList.entrySet()) {
                for (TickerName ticker : entry.getValue().getTickers()) {
                    retVal.add(new TickerName(entry.getKey(), ticker.getCurrency1(), ticker.getCurrency2()));
                }

            }
            return retVal;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/get_ticker_info", method = RequestMethod.POST)
    @ResponseBody
    public TickerInfo getTickerInfo(@RequestBody String body) throws Exception {
        try {
            System.out.println("/getTickerInfo " + body);
            if (body == null) {
                return new TickerInfo();
            }
            TickerName tickerInput = new Gson().fromJson(body, TickerName.class);
            if (tickerInput == null || StringUtils.isEmpty(tickerInput.getExchange()) || StringUtils.isEmpty(tickerInput.getCurrency1())
                    || StringUtils.isEmpty(tickerInput.getCurrency2())) {
                return new TickerInfo();
            }

            CryptoExchangeName cryptoExchangeNameFromInput = null;
            Currency currency1 = null;
            Currency currency2 = null;
            try {
                cryptoExchangeNameFromInput = CryptoExchangeName.valueOf(tickerInput.getExchange());
                currency1 = Currency.valueOf(tickerInput.getCurrency1().toUpperCase());
                currency2 = Currency.valueOf(tickerInput.getCurrency2().toUpperCase());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return new TickerInfo();
            }
            TickerName tickerNameFromInput = new TickerName(currency1, currency2);

            TickerInfo retVal = new TickerInfo();
            CryptoExchange cryptoExchange = cryptoExchangeList.get(cryptoExchangeNameFromInput);
            retVal.setPrice(cryptoExchange.getPrice(tickerNameFromInput));
            retVal.setOrderBook(cryptoExchange.getOrderBook(tickerNameFromInput));
            retVal.setTradeHistory(cryptoExchange.getTradeHistory(tickerNameFromInput));
            return retVal;
        } catch (Exception e) {
            e.printStackTrace();
            return  new TickerInfo();
        }
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
