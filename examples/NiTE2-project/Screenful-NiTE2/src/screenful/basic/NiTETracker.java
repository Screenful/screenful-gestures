package screenful.basic;

import com.primesense.nite.GestureData;
import com.primesense.nite.GestureType;
import com.primesense.nite.HandData;
import com.primesense.nite.HandTracker;
import com.primesense.nite.HandTrackerFrameRef;
import com.primesense.nite.NiTE;
import com.primesense.nite.UserData;
import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openni.DeviceInfo;
import org.openni.OpenNI;

/**
 * Tracker for NUI features. When a NiTETracker is created, it creates instances
 * of UserTracker and HandTracker, starts them and adds itself to their
 * listeners to get notified of new data.
 *
 * To access the data, you can use NiTETracker.getBones() etc. or add a class
 * implementing HandsListener or BonesListener to its listeners.
 *
 * You can pass the NiTETracker object to a Visualization object to get a
 * graphical window, see BonesAndHandsViewer.
 *
 * To recover from intermittent disconnects of the sensor, the object attempts
 * to keep track when a sensor disconnects and stops updating frames until a
 * device is available again.
 *
 */
public class NiTETracker implements
        HandTracker.NewFrameListener,
        UserTracker.NewFrameListener,
        OpenNI.DeviceDisconnectedListener,
        OpenNI.DeviceConnectedListener {

    private ArrayList<HandsListener> handsListeners;
    private ArrayList<BonesListener> bonesListeners;

    HandTracker handTracker;
    UserTracker userTracker;

    HandTrackerFrameRef lastHandFrame;
    UserTrackerFrameRef lastUserFrame;

    BufferedImage bufferedImage;
    boolean deviceConnected;

    /**
     * Return hand tracker for reading hand positions and gestures
     *
     * @return HandTracker object
     */
    public HandTracker getHandTracker() {
        return handTracker;
    }

    /**
     * Return user tracker for reading user poses and skeletal data
     *
     * @return UserTracker object
     */
    public UserTracker getUserTracker() {
        return userTracker;
    }

    /**
     * Access current hand tracker frame
     *
     * @return current hand tracker frame
     */
    public synchronized HandTrackerFrameRef getHandFrame() {
        return lastHandFrame;
    }

    /**
     * Access current user tracker frame
     *
     * @return current user tracker frame
     */
    public synchronized UserTrackerFrameRef getUserFrame() {
        return lastUserFrame;
    }

    /**
     * Access current depth image frame
     *
     * @return current depth image frame
     */
    public synchronized BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    /**
     * Add a listener for new hand frames
     *
     * @param listener HandsListener to add
     */
    public synchronized void addHandsListener(HandsListener listener) {
        handsListeners.add(listener);
    }

    /**
     * Remove a hand listener
     *
     * @param listener HandsListener to remove
     */
    public synchronized void removeHandsListener(HandsListener listener) {
        handsListeners.remove(listener);
    }

    /**
     * Add a listener for new user frames
     *
     * @param listener BonesListener to add
     */
    public synchronized void addBonesListener(BonesListener listener) {
        bonesListeners.add(listener);
    }

    /**
     * Remove a hand listener
     *
     * @param listener HandsListener to remove
     */
    public synchronized void removeBonesListener(BonesListener listener) {
        bonesListeners.remove(listener);
    }

    /**
     * Remove all hands and bones listeners
     */
    public synchronized void removeAllListeners() {
        handsListeners.clear();
        bonesListeners.clear();
    }

    /**
     * Notify listeners of new hand frames
     */
    private void notifyHandsListeners() {
        for (HandsListener listener : handsListeners) {
            listener.onNewHandsFrame(lastHandFrame);
        }
    }

    /**
     * Notify listeners of new user frames
     */
    private void notifyBonesListeners() {
        for (BonesListener listener : bonesListeners) {
            listener.onNewBonesFrame(lastUserFrame);
        }
    }

    /**
     * Wait until a device is connected. Check every second.
     */
    private void waitForDevice() {
        while (OpenNI.enumerateDevices().size() < 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NiTETracker.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("No devices found, waiting...");
        }
        deviceConnected = true;
    }

    private void initialize() {
        handsListeners = new ArrayList<>();
        bonesListeners = new ArrayList<>();

        OpenNI.initialize();
        System.out.println("OPENNI INIT");
        waitForDevice();
        NiTE.initialize();
        System.out.println("NITE INIT");
        createTrackers();
    }

    /**
     * Shut down NiTE and OpenNI.
     */
    private void shutdown() {
        NiTE.shutdown();
        OpenNI.shutdown();
    }

    /**
     * Create hand and user trackers.
     */
    private void createTrackers() {
        try {
            userTracker = UserTracker.create();
            userTracker.addNewFrameListener(this);
        } catch (RuntimeException e) {
            System.out.println("CREATING USER TRACKER FAILED");
        }

        try {
            handTracker = HandTracker.create();
            handTracker.addNewFrameListener(this);

            handTracker.startGestureDetection(GestureType.CLICK);
            handTracker.startGestureDetection(GestureType.WAVE);
        } catch (RuntimeException e) {
            System.out.println("CREATING HAND TRACKER FAILED");
        }

    }

    /**
     * Initialize OpenNI and NiTE, create user and hand trackers, add NuiTracker
     * to their listeners and configure hand tracker to look for click and wave
     * gestures to initiate hand tracking.
     */
    public NiTETracker() {
        deviceConnected = false;
        initialize();

        OpenNI.addDeviceDisconnectedListener(this);
        OpenNI.addDeviceConnectedListener(this);
    }

    /**
     * Return list of detected hands
     *
     * @return List of hand data
     */
    public List<HandData> getHands() {
        if (lastUserFrame != null) {
            return lastHandFrame.getHands();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Return list of detected skeletons
     *
     * @return List of user data
     */
    public List<UserData> getBones() {
        if (lastUserFrame != null) {
            return lastUserFrame.getUsers();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onNewFrame(HandTracker ht) {
        if (deviceConnected) {
            if (lastHandFrame != null) {
                lastHandFrame.release();
                lastHandFrame = null;
            }

            lastHandFrame = ht.readFrame();

            if (lastHandFrame.getGestures().size() > 0) {
                System.out.println("Gestures: " + lastHandFrame.getGestures().size());
            }
            // check if any gesture detected
            for (GestureData gesture : lastHandFrame.getGestures()) {
                if (gesture.isComplete()) {
                    // start hand tracking
                    System.out.println("Starting hand tracking");
                    handTracker.startHandTracking(gesture.getCurrentPosition());
                }
            }
            notifyHandsListeners();
        }
    }

    @Override
    public void onNewFrame(UserTracker ut) {
        if (deviceConnected) {
            if (lastUserFrame != null) {
                lastUserFrame.release();
                lastUserFrame = null;
            }

            lastUserFrame = ut.readFrame();

            // check if any new user detected
            for (UserData user : lastUserFrame.getUsers()) {
                if (user.isNew()) {
                    // start skeleton tracking
                    System.out.println("User " + user.getId() + " found, starting tracking");
                    userTracker.startSkeletonTracking(user.getId());
                }
            }
            notifyBonesListeners();
        }
    }

    @Override
    public void onDeviceDisconnected(DeviceInfo di) {
        System.out.println("" + di.getName() + " -- Disconnected");
        deviceConnected = false;
    }

    @Override
    public void onDeviceConnected(DeviceInfo di) {
        System.out.println("" + di.getName() + " -- Connected");
        createTrackers();
        waitForDevice();
    }
}
