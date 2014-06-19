package nite2.gui;

import com.primesense.nite.UserTracker;
import com.primesense.nite.UserTrackerFrameRef;
import java.awt.Component;
import java.awt.Graphics;
import nite2.basic.SkeletonListener;
import nite2.basic.SkeletonTracker;

/**
 * Base class to draw things based on skeleton data, extend and override paint()
 */
public class GraphicsRenderer extends Component implements SkeletonListener {

    SkeletonTracker skelTracker;
    UserTracker tracker;
    UserTrackerFrameRef lastFrame;

    public GraphicsRenderer(SkeletonTracker tracker) {
        this.skelTracker = tracker;
        this.tracker = skelTracker.getTracker();
        this.lastFrame = null;
    }

    @Override
    public void onNewSkeletonFrame(UserTrackerFrameRef frame) {
        if (lastFrame != null) {
            lastFrame.release();
            lastFrame = null;
        }
        lastFrame = frame;
        repaint();
    }

    @Override
    public synchronized void paint(Graphics g) {
    }
}
