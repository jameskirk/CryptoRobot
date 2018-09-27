package robot.backend.trade.exchange.bitmex;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import robot.backend.trade.exchange.bitmex.model.*;
import robot.backend.trade.model.contant.Currency;
import robot.backend.trade.model.rest.TickerName;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class BitmexWebSocket extends WebSocketClient {

    private static final Log logger = LogFactory.getLog(BitmexWebSocket.class);

    private BitmexCryptoExchange exchange;

    public BitmexWebSocket(BitmexCryptoExchange exchange) throws URISyntaxException {
        super(new URI("wss://www.bitmex.com/realtime"));
        this.connect();
        this.exchange = exchange;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("{\"op\": \"subscribe\", \"args\": [\"orderBookL2:XBTUSD\", \"trade:XBTUSD\", \"orderBookL2:ETHUSD\", \"trade:ETHUSD\"]}");
        logger.info("new connection opened with Bitmex: " + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.error("closed with exit code " + code + " additional info: " + reason);
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.reconnect();
    }

    @Override
    public void onMessage(String message) {
        try {
            logger.debug("received message: " + message);
            AbstractBitmexResponse abstractResponse = new Gson().fromJson(message, AbstractBitmexResponse.class);
            if ("orderBookL2".equals(abstractResponse.getTable())) {
                OrderBookL2Response response = new Gson().fromJson(message, OrderBookL2Response.class);

                BitmexTickerInfo t =  exchange.getTickerInfoMap().get(new TickerName(Currency.XBT, Currency.USD));
                if ("XBTUSD".equals(response.getData().get(0).getSymbol())) {
                    t = exchange.getTickerInfoMap().get(new TickerName(Currency.XBT, Currency.USD));
                } else if ("ETHUSD".equals(response.getData().get(0).getSymbol())) {
                    t = exchange.getTickerInfoMap().get(new TickerName(Currency.ETH, Currency.USD));
                }
                if ("partial".equals(response.getAction())) {

                    for (OrderBookL2Data data : response.getData()) {
                        t.getOrderBookSocket().put(data.getId(), data);
                    }
                } else if ("update".equals(response.getAction())) {
                    for (OrderBookL2Data data : response.getData()) {
                        OrderBookL2Data dataFromMap = t.getOrderBookSocket().get(data.getId());
                        if (dataFromMap == null) {
                            dataFromMap = new OrderBookL2Data();
                            t.getOrderBookSocket().put(data.getId(), dataFromMap);
                        }
                        dataFromMap.setSide(data.getSide());
                        dataFromMap.setSize(data.getSize());
                    }
                }  else if ("insert".equals(response.getAction())) {
                    for (OrderBookL2Data data : response.getData()) {
                        OrderBookL2Data dataFromMap = new OrderBookL2Data();
                        t.getOrderBookSocket().put(data.getId(), dataFromMap);
                        dataFromMap.setSide(data.getSide());
                        dataFromMap.setSize(data.getSize());
                        dataFromMap.setPrice(data.getPrice());
                    }
                }  else if ("remove".equals(response.getAction())) {
                    for (OrderBookL2Data data : response.getData()) {
                        OrderBookL2Data dataFromMap = new OrderBookL2Data();
                        t.getOrderBookSocket().remove(data.getId());
                    }
                }
            }

            else if ("trade".equals(abstractResponse.getTable())) {
                TradeResponse response = new Gson().fromJson(message, TradeResponse.class);
                BitmexTickerInfo t =  exchange.getTickerInfoMap().get(new TickerName(Currency.XBT, Currency.USD));
                if ("XBTUSD".equals(response.getData().get(0).getSymbol())) {
                    t = exchange.getTickerInfoMap().get(new TickerName(Currency.XBT, Currency.USD));
                } else if ("ETHUSD".equals(response.getData().get(0).getSymbol())) {
                    t = exchange.getTickerInfoMap().get(new TickerName(Currency.ETH, Currency.USD));
                }
                for (TradeData data : response.getData()) {
                    t.getHistorySocket().push(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMessage(ByteBuffer message) {
        logger.debug("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        logger.error("an error occurred:" + ex);
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.reconnect();
    }
}