package screenful.apps;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openni.DeviceInfo;
import org.openni.OpenNI;
import screenful.basic.NiTETracker;

/**
 * Just print available devices in a loop for testing how disconnects are
 * handled.
 *
 */
public class DisconnectTest {

    public static void main(String[] args) {
        NiTETracker tracker = new NiTETracker();

        while (true) {

            List<DeviceInfo> devs = OpenNI.enumerateDevices();
            System.out.println("" + devs.size() + " " + tracker.getBones().size());
            while (devs.size() < 1) {
                devs = OpenNI.enumerateDevices();
                Thread.yield();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DisconnectTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
