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
 * Tracker for NUI features. When a NuiTracker is created, it creates instances
 * of UserTracker and HandTracker, starts them and adds itself to their
 * listeners to get notified of new data.
 *
 * To access the data, you can use NuiTracker.getBones() etc. or add a class
 * implementing HandsListener or BonesListener to its listeners.
 *
 * You can pass the NuiTracker object to a Visualization object to get a
 * graphical window, see BonesAndHandsViewer.
 *
 */
public class NuiTracker implements HandTracker.NewFrameListener, UserTracker.NewFrameListener {

    private final ArrayList<HandsListener> handsListeners;
    private final ArrayList<BonesListener> bonesListeners;

    final HandTracker handTracker;
    final UserTracker userTracker;

    HandTrackerFrameRef lastHandFrame;
    UserTrackerFrameRef lastUserFrame;

    BufferedImage bufferedImage;

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
    public void addHandsListener(HandsListener listener) {
        handsListeners.add(listener);
    }

    /**
     * Remove a hand listener
     *
     * @param listener HandsListener to remove
     */
    public void removeHandsListener(HandsListener listener) {
        handsListeners.remove(listener);
    }

    /**
     * Add a listener for new user frames
     *
     * @param listener BonesListener to add
     */
    public void addBonesListener(BonesListener listener) {
        bonesListeners.add(listener);
    }

    /**
     * Remove a hand listener
     *
     * @param listener HandsListener to remove
     */
    public void removeBonesListener(BonesListener listener) {
        bonesListeners.remove(listener);
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
     * Initialize OpenNI and NiTE, create user and hand trackers, add NuiTracker
     * to their listeners and configure hand tracker to look for click and wave
     * gestures to initiate hand tracking.
     */
    public NuiTracker() {
        OpenNI.initialize();
        NiTE.initialize();
        handsListeners = new ArrayList<>();
        bonesListeners = new ArrayList<>();

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
        notifyBonesListeners();
    }
}
