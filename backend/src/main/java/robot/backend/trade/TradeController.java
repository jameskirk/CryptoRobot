package robot.backend.trade;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

@RestController
public class TradeController {

    private static final Log logger = LogFactory.getLog(TradeController.class);

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
        logger.debug("/get_ticker_names ");
        try {
            List<TickerName> retVal = new ArrayList<>();
            List<CryptoExchangeName> names = new ArrayList<>(cryptoExchangeList.keySet());
            names.sort( (a1, a2) -> a1.toString().compareTo(a2.toString()));
            for (CryptoExchangeName entry : names) {
                for (TickerName ticker : cryptoExchangeList.get(entry).getTickers()) {
                    retVal.add(new TickerName(entry, ticker.getCurrency1(), ticker.getCurrency2()));
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
            logger.debug("/getTickerInfo " + body);
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
            int maxSizeOrderBook = 20;
            maxSizeOrderBook = Math.min(maxSizeOrderBook, retVal.getOrderBook().getAsk().size());
            maxSizeOrderBook = Math.min(maxSizeOrderBook, retVal.getOrderBook().getBid().size());
            retVal.getOrderBook().setAsk(retVal.getOrderBook().getAsk().subList(0, maxSizeOrderBook - 1));
            retVal.getOrderBook().setBid(retVal.getOrderBook().getBid().subList(0, maxSizeOrderBook - 1));
            return retVal;
        } catch (Exception e) {
            e.printStackTrace();
            return  new TickerInfo();
        }
    }


}
