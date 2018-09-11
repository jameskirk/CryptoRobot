package robot.backend.trade.exchange;

import robot.backend.trade.RestHelper;
import robot.backend.trade.model.dto.CexIoLastPrice;
import robot.backend.trade.model.dto.CexIoOrderBook;
import robot.backend.trade.model.internal.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CexIoCryptoExchange implements CryptoExchange {

    public CryptoExchangeName getName() {
        return  CryptoExchangeName.CexIo;
    }

    private RestHelper restHelper = new RestHelper();

    @Override
    public BigDecimal getPrice(PairType pairType) throws Exception {
        CexIoLastPrice cexIo = restHelper.mapDtoFromUrl("https://cex.io/api/last_price/" + pairType.getCurrency1() + "/" + pairType.getCurrency2(), CexIoLastPrice.class);
        return new BigDecimal(cexIo.lprice);
    }

    @Override
    public List<Position> getOpenPosition(PairType pairType) throws Exception {
        List<Position> retVal = new ArrayList<>();
//        CexIoRequest request = new CexIoRequest();
//        request.key = "OsM41QjJQzK4AmL9ZwYckitzc";
//        request.nonce = Long.toString(new Date().getTime());
//        String secret = "i5L0vBEc7itbqlYoUrlsPeBBxo";
//        request.signature = encode(secret, request.nonce + "up119890023" + request.key);
//        String jsonRequest = new Gson().toJson(request);
//
//        CexIoOpenPositions cexIoOpenPositions = restHelper.mapDtoFromUrlPost("https://cex.io/api/open_positions/" + pairType.getCurrency1() + "/" + pairType.getCurrency2() ,
//                jsonRequest, CexIoOpenPositions.class);
//        System.out.println("Orders:");
//        if (cexIoOpenPositions != null && cexIoOpenPositions.data != null) {
//            for (CexIoFullPosition pos : cexIoOpenPositions.data) {
//                System.out.println("pos " +pos.pair + pos.ptype + pos.omamount);
//                PositionMapper mapper = new PositionMapper();
//                Position postion = mapper.mapFromCexIo(pos);
//                retVal.add(postion);
//            }
//        }
        return retVal;
    }

    @Override
    public OrderBook getOrderBook(PairType pairType) throws Exception {
        OrderBook retVal = new OrderBook();
        CexIoOrderBook cexIo = restHelper.mapDtoFromUrl("https://cex.io/api/order_book/" + pairType.getCurrency1() + "/" + pairType.getCurrency2(), CexIoOrderBook.class);
        int i = 0;
        for (List<String> twoNumbers: cexIo.asks) {
            BigDecimal price = new BigDecimal(twoNumbers.get(0));
            BigDecimal amount = new BigDecimal(twoNumbers.get(1));
            Order order = new Order();
            order.setPrice(price);
            order.setAmount(amount);
            order.setTotalCost(price.multiply(amount));
            retVal.getSellOrders().add(order);
            i++;
            if (i > 20) {
                break;
            }
        }
        i = 0;
        for (List<String> twoNumbers: cexIo.bids) {
            BigDecimal price = new BigDecimal(twoNumbers.get(0));
            BigDecimal amount = new BigDecimal(twoNumbers.get(1));
            Order order = new Order();
            order.setPrice(price);
            order.setAmount(amount);
            order.setTotalCost(price.multiply(amount));
            retVal.getBuyOrders().add(order);
            i++;
            if (i > 20) {
                break;
            }
        }
        return retVal;
    }

    public List<PairType> getPairs() throws Exception {
        return Arrays.asList(PairType.BTC_USD, PairType.ETH_USD);
    }

    private String encode(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] b = sha256_HMAC.doFinal(message.getBytes());
        String hash = DatatypeConverter.printHexBinary(b);
        return  hash;
    }
}
