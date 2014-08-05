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

/**
 * Simple WebSocket server for interfacing with the browser UI.
 *
 */
public class GestureServer extends WebSocketServer {

    private static Set<WebSocket> conns;
    static Settings settings;

    /**
     * Messenger handles sending messages to the browser when gestures are
     * detected.
     */
    static class Messenger implements GestureListener {

        boolean run;
        NiTETracker tracker;
        int startdelay;

        Messenger(NiTETracker tracker, int startdelay) {
            this.tracker = tracker;
            this.startdelay = startdelay;
        }

        /**
         * When a gesture occurs, first check if enough time has passed after
         * hand tracking started.
         *
         * @param gesture displacement data of the gesture
         */
        @Override
        public void onGesture(Displacement gesture) {
            if (System.currentTimeMillis()
                    > tracker.getLastHandTrackingStartTime() + startdelay) {
                String dir = "STABLE";
                if (settings.exitDirections.contains(gesture.getDirection())) {
                    tracker.forgetHands();
                    System.out.println("User stopped interaction.");
                } else {
                    if (settings.enabledDirections.contains(gesture.getDirection())) {
                        dir = gesture.getDirection().toString().toLowerCase();
                    }
                }
                if (!dir.equals("STABLE")) {
                    for (WebSocket sock : conns) {
                        sock.send(dir);
                        System.out.println("Sending command: " + dir);
                    }
                }
            }
        }
    }

    /**
     * Creates a new WebSocketServer with the wildcard IP accepting all
     * connections.
     */
    public GestureServer(String address, int port) {
        super(new InetSocketAddress(address, port));
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
        if (args.length > 0) {
            settings = new Settings(args[0]);
        } else {
            settings = new Settings();
            // create a default.conf as a starting point
            settings.save();
        }

        // parse settings
        int port = Integer.parseInt(settings.prop.getProperty("port"));
        String address = settings.prop.getProperty("address");
        int startdelay = Integer.parseInt(settings.prop.getProperty("startdelay"));
        int traveldistance = Integer.parseInt(settings.prop.getProperty("traveldistance"));
        int travelframes = Integer.parseInt(settings.prop.getProperty("travelframes"));
        int cooldown = Integer.parseInt(settings.prop.getProperty("cooldown"));

        System.out.println("Starting WebSocket server " + address + ":" + port + " ...");
        System.out.println("Settings: " + settings.prop);

        // create tracker
        NiTETracker tracker = new NiTETracker();
        // create visualization (for testing)
        //HandsVisualization hands = new HandsVisualization(tracker, "Hand tracker window");
        // create the server
        GestureServer server = new GestureServer(address, port);
        // create a messenger to send tracker events to the browser
        // (the tracker object is used for stopping hand tracking if needed)
        Messenger messenger = new Messenger(tracker, startdelay);
        // create a gesture for the tracker to detect
        Gesture gesture = new Gesture(new DirectionDetector(traveldistance), travelframes, cooldown);
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
