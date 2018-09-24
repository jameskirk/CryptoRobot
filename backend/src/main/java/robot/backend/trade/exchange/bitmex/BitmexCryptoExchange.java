package robot.backend.trade.exchange.bitmex;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.thymeleaf.util.StringUtils;
import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.contant.Currency;
import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;
import robot.backend.trade.util.SizedStack;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BitmexCryptoExchange implements CryptoExchange {

    private Map<TickerName, TickerInfo> tickerInfoMap = new ConcurrentHashMap<>();

    //private Map<String, OrderBookL2Data> orderBookSocket = new ConcurrentHashMap<>();

    //private Stack<TradeData> historySocket = new SizedStack<>(20);

    public BitmexCryptoExchange() {
        for (TickerName t: getTickers()) {
            tickerInfoMap.put(t, new TickerInfo(t));
        }
        Thread t = new BitmexThread(this);
        t.start();
    }

    public static class TickerInfo {

        private TickerName tickerName;

        public TickerInfo(TickerName tickerName) {
            this.tickerName = tickerName;
        }

        private Map<String, OrderBookL2Data> orderBookSocket = new ConcurrentHashMap<>();

        private Stack<TradeData> historySocket = new SizedStack<>(20);

        public Map<String, OrderBookL2Data> getOrderBookSocket() {
            return orderBookSocket;
        }

        public void setOrderBookSocket(Map<String, OrderBookL2Data> orderBookSocket) {
            this.orderBookSocket = orderBookSocket;
        }

        public Stack<TradeData> getHistorySocket() {
            return historySocket;
        }

        public void setHistorySocket(Stack<TradeData> historySocket) {
            this.historySocket = historySocket;
        }

        public TickerName getTickerName() {
            return tickerName;
        }

        @Override
        public boolean equals(Object obj) {
            TickerInfo tickerInfo = (TickerInfo) obj;
            return tickerName.equals(tickerInfo.getTickerName());
        }
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
        TickerInfo tickerInfo = tickerInfoMap.get(ticker);
        for (OrderBookL2Data data : tickerInfo.orderBookSocket.values()) {
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
        return minAsk;
    }

    @Override
    public OrderBook getOrderBook(TickerName ticker) throws Exception {
        BigDecimal price = getPrice(ticker);
        TickerInfo tickerInfo = tickerInfoMap.get(ticker);
        OrderBook retVal = new OrderBook();
        final int COUNT = 20;
        List<OrderBook.OrderBookEntity> rawAsk = new ArrayList<>();
        List<OrderBook.OrderBookEntity> rawBid = new ArrayList<>();
        for (OrderBookL2Data data : tickerInfo.orderBookSocket.values()) {
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
        TickerInfo tickerInfo = tickerInfoMap.get(pairType);
        for (TradeData t: tickerInfo.getHistorySocket()) {
            retVal.add(new TradeHistoryEntity(t.getSide().toLowerCase(), new BigDecimal(t.getSize()), new BigDecimal(t.getPrice()), new Date()));
        }
        Collections.reverse(retVal);
        return retVal;
    }

    public static class BitmexWebSocket extends WebSocketClient {

        BitmexCryptoExchange exchange;

        public BitmexWebSocket(BitmexCryptoExchange exchange) throws URISyntaxException {
            super(new URI("wss://www.bitmex.com/realtime"));
            this.connect();
            this.exchange = exchange;
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            send("{\"op\": \"subscribe\", \"args\": [\"orderBookL2:XBTUSD\", \"trade:XBTUSD\", \"orderBookL2:ETHUSD\", \"trade:ETHUSD\"]}");
            System.out.println("new connection opened");
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("closed with exit code " + code + " additional info: " + reason);
        }

        @Override
        public void onMessage(String message) {
            try {
                //System.out.println("received message: " + message);
                AbstractBitmexResponse abstractResponse = new Gson().fromJson(message, AbstractBitmexResponse.class);
                if ("orderBookL2".equals(abstractResponse.getTable())) {
                    OrderBookL2Response response = new Gson().fromJson(message, OrderBookL2Response.class);

                    TickerInfo t =  exchange.tickerInfoMap.get(new TickerName(Currency.XBT, Currency.USD));
                    if ("XBTUSD".equals(response.getData().get(0).getSymbol())) {
                        t = exchange.tickerInfoMap.get(new TickerName(Currency.XBT, Currency.USD));
                    } else if ("ETHUSD".equals(response.getData().get(0).getSymbol())) {
                        t = exchange.tickerInfoMap.get(new TickerName(Currency.ETH, Currency.USD));
                    }
                    if ("partial".equals(response.getAction())) {

                        for (OrderBookL2Data data : response.getData()) {
                            t.orderBookSocket.put(data.getId(), data);
                        }
                    } else if ("update".equals(response.getAction())) {
                        for (OrderBookL2Data data : response.getData()) {
                            OrderBookL2Data dataFromMap = t.orderBookSocket.get(data.getId());
                            if (dataFromMap == null) {
                                dataFromMap = new OrderBookL2Data();
                                t.orderBookSocket.put(data.getId(), dataFromMap);
                            }
                            dataFromMap.setSide(data.getSide());
                            dataFromMap.setSize(data.getSize());
                        }
                    }  else if ("insert".equals(response.getAction())) {
                        for (OrderBookL2Data data : response.getData()) {
                            OrderBookL2Data dataFromMap = new OrderBookL2Data();
                            t.orderBookSocket.put(data.getId(), dataFromMap);
                            dataFromMap.setSide(data.getSide());
                            dataFromMap.setSize(data.getSize());
                        }
                    }
                }

                else if ("trade".equals(abstractResponse.getTable())) {
                    TradeResponse response = new Gson().fromJson(message, TradeResponse.class);
                    TickerInfo t =  exchange.tickerInfoMap.get(new TickerName(Currency.XBT, Currency.USD));
                    if ("XBTUSD".equals(response.getData().get(0).getSymbol())) {
                        t = exchange.tickerInfoMap.get(new TickerName(Currency.XBT, Currency.USD));
                    } else if ("ETHUSD".equals(response.getData().get(0).getSymbol())) {
                        t = exchange.tickerInfoMap.get(new TickerName(Currency.ETH, Currency.USD));
                    }
                    for (TradeData data : response.getData()) {
                        t.historySocket.push(data);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onMessage(ByteBuffer message) {
            System.out.println("received ByteBuffer");
        }

        @Override
        public void onError(Exception ex) {
            System.err.println("an error occurred:" + ex);
        }
    }
}
