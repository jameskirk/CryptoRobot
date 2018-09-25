package robot.backend.trade.exchange.bitmex;

import org.thymeleaf.util.StringUtils;
import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.exchange.bitmex.model.BitmexTickerInfo;
import robot.backend.trade.exchange.bitmex.model.OrderBookL2Data;
import robot.backend.trade.exchange.bitmex.model.TradeData;
import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.contant.Currency;
import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BitmexCryptoExchange implements CryptoExchange {

    private Map<TickerName, BitmexTickerInfo> tickerInfoMap = new ConcurrentHashMap<>();

    public BitmexCryptoExchange() {
        for (TickerName t: getTickers()) {
            tickerInfoMap.put(t, new BitmexTickerInfo(t));
        }
        Thread t = new BitmexThread(this);
        t.start();
    }

    public static class BitmexThread extends Thread {

        BitmexCryptoExchange exchange;

        public BitmexThread(BitmexCryptoExchange exchange) {
            super();
            this.exchange = exchange;
        }

        @Override
        public void run() {
            try {
                BitmexWebSocket webSocket = new BitmexWebSocket(exchange);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CryptoExchangeName getExchangeName() {
        return CryptoExchangeName.bitmex;
    }

    @Override
    public List<TickerName> getTickers() {
        return Arrays.asList(new TickerName(getExchangeName(), Currency.XBT, Currency.USD),
                new TickerName(getExchangeName(), Currency.ETH, Currency.USD));
    }

    @Override
    public BigDecimal getPrice(TickerName ticker) throws Exception {
        BigDecimal minAsk = null;
        BigDecimal maxBid = null;
        BitmexTickerInfo bitmexTickerInfo = tickerInfoMap.get(ticker);
        for (OrderBookL2Data data : bitmexTickerInfo.getOrderBookSocket().values()) {
            if (!StringUtils.isEmpty(data.getPrice())) {
                BigDecimal dataPrice = new BigDecimal(data.getPrice());
                if ("Sell".equals(data.getSide())) {
                    if (minAsk == null) {
                        minAsk = dataPrice;
                    }
                    if (minAsk.compareTo(dataPrice) == 1) {
                        minAsk = dataPrice;
                    }
                } else {
                    if (maxBid == null) {
                        maxBid = dataPrice;
                    }
                    if (maxBid.compareTo(dataPrice) == -1) {
                        maxBid = dataPrice;
                    }
                }
            }

        }
        if (minAsk == null || maxBid == null) {
            return null;
        }
        return (minAsk.add(maxBid)).divide(new BigDecimal(2));
    }

    @Override
    public OrderBook getOrderBook(TickerName ticker) throws Exception {
        BigDecimal price = getPrice(ticker);
        if (price == null) {
            return new OrderBook();
        }
        BitmexTickerInfo bitmexTickerInfo = tickerInfoMap.get(ticker);
        OrderBook retVal = new OrderBook();
        final int COUNT = 20;
        List<OrderBook.OrderBookEntity> rawAsk = new ArrayList<>();
        List<OrderBook.OrderBookEntity> rawBid = new ArrayList<>();
        for (OrderBookL2Data data : bitmexTickerInfo.getOrderBookSocket().values()) {
            if (!StringUtils.isEmpty(data.getPrice()) && new BigDecimal(data.getPrice()).compareTo(price.add(new BigDecimal(COUNT))) == -1
                    && new BigDecimal(data.getPrice()).compareTo(price.add(new BigDecimal(-COUNT))) == 1) {
                OrderBook.OrderBookEntity entity = new OrderBook.OrderBookEntity(new BigDecimal(data.getPrice()), new BigDecimal(data.getSize()));
                if ("Sell".equals(data.getSide())) {
                    rawAsk.add(entity);
                } else {
                    rawBid.add(entity);
                }
            }
        }

        if (rawAsk.size() == 0 || rawBid.size() == 0) {
            return retVal;
        }
        rawAsk.sort(Comparator.comparing(a -> a.getPrice()));

        List<OrderBook.OrderBookEntity> ask = new ArrayList<>();
        BigDecimal amountSum = new BigDecimal(0);
        final BigDecimal filter = new BigDecimal("1");
        BigDecimal addToEntryIfPriceGt = new BigDecimal(rawAsk.get(0).getPrice().toBigInteger());
            for (OrderBook.OrderBookEntity e: rawAsk) {
            amountSum = amountSum.add(e.getAmount());
            if (addToEntryIfPriceGt.compareTo(e.getPrice()) == 0 || addToEntryIfPriceGt.compareTo(e.getPrice()) == -1) {
                ask.add(new OrderBook.OrderBookEntity(addToEntryIfPriceGt, amountSum));
                amountSum = new BigDecimal(0);
                addToEntryIfPriceGt = e.getPrice().add(filter);
            }
        }
        retVal.setAsk(ask);


        rawBid.sort(Comparator.comparing(a -> a.getPrice()));
        Collections.reverse(rawBid);

        List<OrderBook.OrderBookEntity> bid = new ArrayList<>();
        amountSum = new BigDecimal(0);
        addToEntryIfPriceGt = new BigDecimal(rawBid.get(0).getPrice().toBigInteger());
        for (OrderBook.OrderBookEntity e: rawBid) {
            amountSum = amountSum.add(e.getAmount());
            if (addToEntryIfPriceGt.compareTo(e.getPrice()) == 0 || addToEntryIfPriceGt.compareTo(e.getPrice()) == 1) {
                bid.add(new OrderBook.OrderBookEntity(addToEntryIfPriceGt, amountSum));
                amountSum = new BigDecimal(0);
                addToEntryIfPriceGt = e.getPrice().add(filter.negate());
            }
        }
        retVal.setBid(bid);

        return retVal;
    }

    @Override
    public List<TradeHistoryEntity> getTradeHistory(TickerName pairType) throws Exception {
        List<TradeHistoryEntity> retVal = new ArrayList<>();
        BitmexTickerInfo bitmexTickerInfo = tickerInfoMap.get(pairType);
        for (TradeData t: bitmexTickerInfo.getHistorySocket()) {
            retVal.add(new TradeHistoryEntity(t.getSide().toLowerCase(), new BigDecimal(t.getSize()), new BigDecimal(t.getPrice()), new Date()));
        }
        Collections.reverse(retVal);
        return retVal;
    }

    public Map<TickerName, BitmexTickerInfo> getTickerInfoMap() {
        return tickerInfoMap;
    }

    public void setTickerInfoMap(Map<TickerName, BitmexTickerInfo> tickerInfoMap) {
        this.tickerInfoMap = tickerInfoMap;
    }
}
