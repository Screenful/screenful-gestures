package nite2.basic;

import com.primesense.nite.NiTE;
import com.primesense.nite.UserData;
import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.openni.OpenNI;

public class SkeletonTracker implements UserTracker.NewFrameListener {

    private final ArrayList<SkeletonListener> listeners;

    UserTrackerFrameRef lastFrame;
    BufferedImage bufferedImage;
    final UserTracker tracker;

    /**
     * Return tracker object (for limb coordinate calculations in rendering)
     */
    public UserTracker getTracker() {
        return tracker;
    }

    public SkeletonTracker() {
        OpenNI.initialize();
        NiTE.initialize();
        listeners = new ArrayList<>();
        tracker = UserTracker.create();
        // start listening for new frames
        tracker.addNewFrameListener(this);
    }

    /**
     * Add a listener for new user frames
     */
    public void addSkeletonListener(SkeletonListener listener) {
        listeners.add(listener);
    }

    /**
     * Notify listeners of new user frames
     */
    private void notifyListeners() {
        for (SkeletonListener listener : listeners) {
            listener.onNewSkeletonFrame(lastFrame);
        }
    }

    /**
     * Return list of detected skeletons
     *
     * @return List<UserData> of skeletons
     */
    public List<UserData> getSkeletons() {
        if (lastFrame != null) {
            return lastFrame.getUsers();
        } else {
            return new ArrayList<UserData>();
        }

    }

    @Override
    public synchronized void onNewFrame(UserTracker tracker) {
        if (lastFrame != null) {
            lastFrame.release();
            lastFrame = null;
        }

        lastFrame = this.tracker.readFrame();

        // check if any new user detected
        for (UserData user : lastFrame.getUsers()) {
            if (user.isNew()) {
                // start skeleton tracking
                System.out.println("User " + user.getId() + " found, starting tracking");
                this.tracker.startSkeletonTracking(user.getId());
            }
        }
        // notify SkeletonListeners
        notifyListeners();

    }

    public void showImageWindow() {
        SkeletonWindow win = new SkeletonWindow(this);
        // Add window's renderer to SkeletonListeners
        this.addSkeletonListener(win.getSkeletonRender());
        new Thread(win).start();
    }

}
