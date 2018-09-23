package robot.backend.trade.exchange.bitmex;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import robot.backend.trade.exchange.CryptoExchange;
import robot.backend.trade.model.contant.CryptoExchangeName;
import robot.backend.trade.model.contant.Currency;
import robot.backend.trade.model.rest.OrderBook;
import robot.backend.trade.model.rest.TickerName;
import robot.backend.trade.model.rest.TradeHistoryEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BitmexCryptoExchange implements CryptoExchange {

    private ConcurrentHashMap<String, Data> orderBookSocket = new ConcurrentHashMap<>();

    public BitmexCryptoExchange() {
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
        return Arrays.asList(new TickerName(getExchangeName(), Currency.BTC, Currency.USD),
                new TickerName(getExchangeName(), Currency.ETH, Currency.USD));
    }

    @Override
    public BigDecimal getPrice(TickerName ticker) throws Exception {
        BigDecimal minAsk = null;
        BigDecimal maxBid = null;
        for (Data data: orderBookSocket.values()) {
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
        return minAsk;
    }

    @Override
    public OrderBook getOrderBook(TickerName pairType) throws Exception {
        BigDecimal price = getPrice(pairType);
        OrderBook retVal = new OrderBook();
        for (Data data: orderBookSocket.values()) {
            if (new BigDecimal(data.getPrice()).compareTo(price.add(new BigDecimal(20))) == -1
                    && new BigDecimal(data.getPrice()).compareTo(price.add(new BigDecimal(-20))) == 1) {
                OrderBook.OrderBookEntity entity = new OrderBook.OrderBookEntity(new BigDecimal(data.getPrice()), new BigDecimal(data.getSize()));
                if ("Sell".equals(data.getSide())) {
                    retVal.getAsk().add(entity);
                } else {
                    retVal.getBid().add(entity);
                }
            }
        }
        retVal.getAsk().sort(Comparator.comparing(a -> a.getPrice()));
        retVal.getBid().sort(Comparator.comparing(a -> a.getPrice()));
        Collections.reverse(retVal.getBid());
//        retVal.getAsk().stream().sorted((obj1, obj2) -> obj1.getPrice().compareTo(obj2.getPrice()));
//        retVal.getBid().stream().sorted((obj1, obj2) -> obj2.getPrice().compareTo(obj1.getPrice()));
        return retVal;
    }

    @Override
    public List<TradeHistoryEntity> getTradeHistory(TickerName pairType) throws Exception {
        return new ArrayList<>();
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
            send("{\"op\": \"subscribe\", \"args\": [\"orderBookL2:XBTUSD\"]}");
            System.out.println("new connection opened");
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("closed with exit code " + code + " additional info: " + reason);
        }

        @Override
        public void onMessage(String message) {
            System.out.println("received message: " + message);
            OrderBookL2Response response =  new Gson().fromJson(message, OrderBookL2Response.class);
            if ("partial".equals(response.getAction())) {
                for (Data data: response.getData()) {
                    exchange.orderBookSocket.put(data.getId(), data);
                }
            } else if("update".equals(response.getAction())) {
                for (Data data: response.getData()) {
                    Data dataFromMap = exchange.orderBookSocket.get(data.getId());
                    dataFromMap.setSide(data.getSide());
                    dataFromMap.setSize(data.getSize());
                }
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
