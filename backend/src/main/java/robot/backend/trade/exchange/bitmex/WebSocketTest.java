package robot.backend.trade.exchange.bitmex;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class WebSocketTest {

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new EmptyClient(new URI("wss://www.bitmex.com/realtime"));
        client.connect();
    }

    static public class EmptyClient extends WebSocketClient {

        public EmptyClient(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        public EmptyClient(URI serverURI) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            send("{\"op\": \"subscribe\", \"args\": [\"trade:XBTUSD\"]}");
            System.out.println("new connection opened");
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("closed with exit code " + code + " additional info: " + reason);
        }

        @Override
        public void onMessage(String message) {
            System.out.println("received message: " + message);
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
