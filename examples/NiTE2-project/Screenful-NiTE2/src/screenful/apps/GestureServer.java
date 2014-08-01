package screenful.apps;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openni.DeviceInfo;
import org.openni.OpenNI;
import screenful.basic.NiTETracker;
import screenful.detectors.DirectionDetector;
import screenful.gestures.Displacement;
import screenful.gestures.Gesture;
import screenful.gestures.GestureListener;

/**
 * Test program for gesture recognition recovery. Start and open
 * html/carousel-ws.html
 *
 */
public class GestureServer {

    /**
     * Simple listener to demonstrate the directions are still being recognized
     * after disconnecting the sensor and plugging it in again.
     */
    static class PrintDirection implements GestureListener {

        @Override
        public void onGesture(Displacement gesture) {
            System.out.println("" + gesture.getDirection());
        }
    }

    public static void main(String[] args) {
        NiTETracker tracker = new NiTETracker();
        List<DeviceInfo> devs;
        int lastnum = 0;
        // create a gesture listener
        Gesture swipe = new Gesture(new DirectionDetector(5), 5, 10);
        tracker.addHandsListener(swipe);
        PrintDirection printDir = new PrintDirection();
        swipe.addListener(printDir);

        while (true) {

            devs = OpenNI.enumerateDevices();
            if (lastnum != devs.size()) {
                lastnum = devs.size();
                System.out.println("Devices connected: " + lastnum);
            }
            // if no devices, wait until a device is available again
            while (devs.size() < 1) {
                Thread.yield();
                devs = OpenNI.enumerateDevices();
            }
            // when device is connected, do something every now and then
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GestureServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
