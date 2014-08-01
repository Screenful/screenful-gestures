package screenful.server;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import screenful.basic.NiTETracker;
import screenful.detectors.DirectionDetector;
import screenful.gestures.Displacement;
import screenful.gestures.Gesture;
import screenful.gestures.GestureListener;
import screenful.gui.visualization.HandsVisualization;

/**
 * Simple proof of concept WebSocket server for gestures
 *
 */
public class GestureSocket extends WebSocketServer {

    static class Messenger implements GestureListener {

        boolean run;
        NiTETracker tracker;

        Messenger(NiTETracker tracker) {
            this.tracker = tracker;
        }

        @Override
        public void onGesture(Displacement gesture) {
            String dir = "stable";
            switch (gesture.getDirection()) {
                case LEFT:
                    dir = "left";
                    break;
                case RIGHT:
                    dir = "right";
                    break;
                case OUT:
                    tracker.forgetHands();
                    break;
            }
            if (!dir.equals("stable")) {
                for (WebSocket sock : conns) {
                    sock.send(dir);
                    System.out.println("sending command: " + dir);
                }
            }
        }
    }

    /**
     * The web socket port number
     */
    private static int PORT = 8887;

    private static Set<WebSocket> conns;

    /**
     * Creates a new WebSocketServer with the wildcard IP accepting all
     * connections.
     */
    public GestureSocket() {
        super(new InetSocketAddress(PORT));
        conns = new HashSet<>();
    }

    /**
     * Method handler when a new connection has been opened.
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /**
     * Method handler when a connection has been closed.
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /**
     * Method handler when a message has been received from the client.
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received: " + message);
    }

    /**
     * Method handler when an error has occured.
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        conns.remove(conn);
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {
        NiTETracker tracker = new NiTETracker();
        HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        GestureSocket server = new GestureSocket();
        Messenger messenger = new Messenger(tracker);
        Gesture gesture = new Gesture(new DirectionDetector(5), 5, 250);
        gesture.addListener(messenger);
        tracker.addHandsListener(gesture);

        hands.show();
        server.start();
    }

}
