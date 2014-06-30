package nite2.basic;

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
import org.openni.OpenNI;

/**
 * Tracker for NUI features, ie. both skeleton tracking and hand tracking.
 *
 */
public class NuiTracker implements HandTracker.NewFrameListener, UserTracker.NewFrameListener {

    private final ArrayList<HandListener> handListeners;
    private final ArrayList<SkeletonListener> userListeners;

    final HandTracker handTracker;
    final UserTracker userTracker;

    HandTrackerFrameRef lastHandFrame;
    UserTrackerFrameRef lastUserFrame;

    BufferedImage bufferedImage;

    /**
     * Return tracker objects (used in limb coordinate calculations when
     * rendering)
     */
    public HandTracker getHandTracker() {
        return handTracker;
    }

    public UserTracker getUserTracker() {
        return userTracker;
    }

    /**
     * Access current frames
     */
    public synchronized HandTrackerFrameRef getHandFrame() {
        return lastHandFrame;
    }

    public synchronized UserTrackerFrameRef getUserFrame() {
        return lastUserFrame;
    }

    public synchronized BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    /**
     * Add a listener for new hand frames
     */
    public void addHandListener(HandListener listener) {
        handListeners.add(listener);
    }

    /**
     * Add a listener for new user frames
     */
    public void addSkeletonListener(SkeletonListener listener) {
        userListeners.add(listener);
    }

    /**
     * Notify listeners of new hand or user frames
     */
    private void notifyHandListeners() {
        for (HandListener listener : handListeners) {
            listener.onNewHandFrame(lastHandFrame);
        }
    }

    private void notifyUserListeners() {
        for (SkeletonListener listener : userListeners) {
            listener.onNewSkeletonFrame(lastUserFrame);
        }
    }

    /**
     * Initialize OpenNI and NiTE, add tracker to skeleton and hand data
     * listeners and configure hand tracker to look for click and wave gestures
     * to initiate tracking.
     */
    public NuiTracker() {
        OpenNI.initialize();
        NiTE.initialize();
        handListeners = new ArrayList<>();
        userListeners = new ArrayList<>();

        userTracker = UserTracker.create();
        userTracker.addNewFrameListener(this);

        handTracker = HandTracker.create();
        handTracker.addNewFrameListener(this);

        handTracker.startGestureDetection(GestureType.CLICK);
        handTracker.startGestureDetection(GestureType.WAVE);
    }

    /**
     * Return list of detected hands
     *
     * @return List<HandData> of hand data
     */
    public List<HandData> getHands() {
        if (lastUserFrame != null) {
            return lastHandFrame.getHands();
        } else {
            return new ArrayList<HandData>();
        }
    }

    /**
     * Return list of detected skeletons
     *
     * @return List<UserData> of skeletons
     */
    public List<UserData> getSkeletons() {
        if (lastUserFrame != null) {
            return lastUserFrame.getUsers();
        } else {
            return new ArrayList<UserData>();
        }
    }

    @Override
    public void onNewFrame(HandTracker ht) {
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
        notifyHandListeners();
    }

    @Override
    public void onNewFrame(UserTracker ut) {
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
        notifyUserListeners();
    }
}
