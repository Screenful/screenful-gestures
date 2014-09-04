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
import java.util.Iterator;
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

    private TrackerSettings trackersettings;

    private ArrayList<HandsListener> handsListeners;
    private ArrayList<BonesListener> bonesListeners;
    private ArrayList<TrackingListener> trackingListeners;

    HandTracker handTracker;
    UserTracker userTracker;

    HandTrackerFrameRef lastHandFrame;
    UserTrackerFrameRef lastUserFrame;

    BufferedImage bufferedImage;
    boolean deviceConnected;
    private long lastHandTrackingStartTime;

    private final boolean handTrackingEnabled;
    private final boolean userTrackingEnabled;
    private boolean handsTracked;

    /**
     * Initialize OpenNI and NiTE, create user and hand trackers. A watchdog
     * makes sure the depth stream actually starts or the program exits (to be
     * respawned again).
     */
    public NiTETracker(TrackerSettings settings) {
        this.trackersettings = settings;
        this.deviceConnected = false;
        this.handsTracked = false;
        this.handTrackingEnabled = settings.handTrackerEnabled;
        this.userTrackingEnabled = settings.skeletonTrackerEnabled;
        this.lastHandTrackingStartTime = 0;
        initialize();
        new Thread(new StreamStartWatchdog()).start();
        OpenNI.addDeviceDisconnectedListener(this);
        OpenNI.addDeviceConnectedListener(this);
    }

    /**
     * Occasionally (a small percentage of the time) the depth stream does not
     * start properly. This watchdog will simply wait one second and check if
     * there are hand tracker frames (which should be there if everything went
     * correctly), if not, a restart is needed so we exit the JVM.
     */
    class StreamStartWatchdog implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NiTETracker.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (lastHandFrame == null) {
                System.exit(1);
            }
        }
    }

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
     * Add a listener for hand tracker start/stop events
     *
     * @param listener TrackingListener to add
     */
    public synchronized void addTrackerListener(TrackingListener listener) {
        trackingListeners.add(listener);
    }

    /**
     * Remove a tracking listener
     *
     * @param listener TrackingListener to remove
     */
    public synchronized void removeTrackingListener(TrackingListener listener) {
        trackingListeners.remove(listener);
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
        trackingListeners.clear();
    }

    /**
     * Notify listeners that hand tracking has started.
     */
    private void notifyTrackingStarted() {
        for (Iterator<TrackingListener> it = trackingListeners.iterator(); it.hasNext();) {
            TrackingListener next = it.next();
            next.onHandTrackingStarted();
        }
    }

    /**
     * Notify listeners that hand tracking has stopped.
     */
    private void notifyTrackingStopped() {
        for (Iterator<TrackingListener> it = trackingListeners.iterator(); it.hasNext();) {
            TrackingListener next = it.next();
            next.onHandTrackingStopped();
        }
    }

    /**
     * Notify listeners of new hand frames
     */
    private void notifyHandsListeners() {
        for (Iterator<HandsListener> it = handsListeners.iterator(); it.hasNext();) {
            HandsListener next = it.next();
            next.onNewHandsFrame(lastHandFrame);
        }
    }

    /**
     * Notify listeners of new user frames
     */
    private void notifyBonesListeners() {
        for (Iterator<BonesListener> it = bonesListeners.iterator(); it.hasNext();) {
            BonesListener next = it.next();
            next.onNewBonesFrame(lastUserFrame);
        }
    }

    /**
     * Wait until a device is connected. Check every second.
     */
    private void waitForDevice() {
        boolean printedOnce = false;
        while (OpenNI.enumerateDevices().size() < 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NiTETracker.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!printedOnce) {
                System.out.println("No devices found, waiting...");
                printedOnce = true;
            }
        }
        deviceConnected = true;
    }

    /**
     * Perform initialization tasks.
     */
    private void initialize() {
        handsListeners = new ArrayList<>();
        bonesListeners = new ArrayList<>();
        trackingListeners = new ArrayList<>();

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
        if (userTrackingEnabled) {
            try {
                userTracker = UserTracker.create();
                userTracker.addNewFrameListener(this);
            } catch (RuntimeException e) {
                System.out.println("CREATING USER TRACKER FAILED");
                // If creation failed, there is a problem that's unlikely to be fixed without restarting
                System.exit(1);
            }
        }

        if (handTrackingEnabled) {
            try {
                handTracker = HandTracker.create();
                handTracker.addNewFrameListener(this);
                for (GestureType g : trackersettings.startGestures) {
                    handTracker.startGestureDetection(g);
                }
            } catch (RuntimeException e) {
                System.out.println("CREATING HAND TRACKER FAILED");
                // If creation failed, there is a problem that's unlikely to be fixed without restarting
                System.exit(1);
            }
        }
    }

    /**
     * Return the last time hand tracking was started.
     */
    public long getLastHandTrackingStartTime() {
        return lastHandTrackingStartTime;
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
     * Stop tracking a specific hand ID
     *
     * @param id ID number
     */
    public void forgetHand(short id) {
        handTracker.stopHandTracking(id);
    }

    /**
     * Stop all hand tracking
     */
    public void forgetHands() {
        if (lastHandFrame != null) {
            for (HandData hand : lastHandFrame.getHands()) {
                forgetHand(hand.getId());
            }
        }
    }

    /**
     * Return a list of all tracked hands
     *
     * @return List of HandData
     */
    public List<HandData> getTrackedHands() {
        ArrayList<HandData> trackedHands = new ArrayList<>();
        if (lastHandFrame != null) {
            for (HandData hand : lastHandFrame.getHands()) {
                if (hand.isTracking()) {
                    trackedHands.add(hand);
                }
            }
        }
        return trackedHands;
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

    /**
     * Handle hand tracker frame
     *
     * @param ht frame
     */
    @Override
    public void onNewFrame(HandTracker ht) {
        /**
         * Skip checking for lost hands this frame if tracking is started for a
         * new hand, otherwise stop event is sent immediately after start.
         */
        boolean skip = false;
        if (deviceConnected) {
            if (lastHandFrame != null) {
                lastHandFrame.release();
                lastHandFrame = null;
            }

            lastHandFrame = ht.readFrame();

            if (lastHandFrame.getGestures().size() > 0) {
                System.out.print("Detected hand gestures:");
                for (GestureData gesture : lastHandFrame.getGestures()) {
                    System.out.print(" " + gesture.getType().name());
                }
                System.out.println("");
            }
            // check if any gesture detected
            for (GestureData gesture : lastHandFrame.getGestures()) {
                if (gesture.isComplete()) {
                    // start hand tracking
                    System.out.println("Starting hand tracking");
                    handTracker.startHandTracking(gesture.getCurrentPosition());
                    lastHandTrackingStartTime = System.currentTimeMillis();
                    notifyTrackingStarted();
                    handsTracked = true;
                    skip = true;
                }
            }
            /**
             * If no hands were just found and hands were tracking but none of
             * them are now, notify tracking has stopped. Doesn't work
             * perfectly.
             */
            int numhands = getTrackedHands().size();
            if (numhands > 0 && handsTracked == false) {
                notifyTrackingStarted();
                handsTracked = true;
            }

            if (!skip && handsTracked && numhands < 1) {
                notifyTrackingStopped();
                handsTracked = false;
            }
            notifyHandsListeners();
        }
    }

    /**
     * Handle user tracker frame
     *
     * @param ut frame
     */
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

    /**
     * Actions to be done when the sensor disconnects
     *
     * @param di device info object
     */
    @Override
    public void onDeviceDisconnected(DeviceInfo di) {
        System.out.println("" + di.getName() + " -- Disconnected");
        deviceConnected = false;
    }

    /**
     * Actions to be done when a sensor connects
     *
     * @param di device info object
     */
    @Override
    public void onDeviceConnected(DeviceInfo di) {
        System.out.println("" + di.getName() + " -- Connected");
        // recreate trackers
        createTrackers();
        // make sure device is available
        waitForDevice();
    }
}
