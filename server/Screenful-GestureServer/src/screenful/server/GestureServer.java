package screenful.server;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import screenful.basic.NiTETracker;
import screenful.gestures.detectors.DirectionDetector;
import screenful.gestures.detectors.Displacement;
import screenful.gestures.Gesture;
import screenful.gestures.GestureListener;
import screenful.gui.visualization.HandsVisualization;

/**
 * Simple WebSocket server for interfacing with the browser UI.
 *
 */
public class GestureServer extends WebSocketServer {

    /**
     * The web socket port number
     */
    private static final int PORT = 8887;

    private static Set<WebSocket> conns;

    /**
     * Messenger handles sending messages to the browser when gestures are
     * detected.
     */
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
                    System.out.println("Sending command: " + dir);
                }
            }
        }
    }

    /**
     * Creates a new WebSocketServer with the wildcard IP accepting all
     * connections.
     */
    public GestureServer() {
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
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        // create tracker
        NiTETracker tracker = new NiTETracker();
        // create visualization (for testing)
        //HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // create the server
        GestureServer server = new GestureServer();
        // create a messenger to send tracker events to the browser
        // (the tracker object is used for stopping hand tracking if needed)
        Messenger messenger = new Messenger(tracker);
        // create a gesture for the tracker to detect
        Gesture gesture = new Gesture(new DirectionDetector(5), 5, 10);
        // add messenger to gesture's listeners
        gesture.addListener(messenger);
        // add gesture to tracker's listeners
        tracker.addHandsListener(gesture);

        // show visualization window (for testing)
        //hands.show();
        // start the server
        server.start();
    }

}
