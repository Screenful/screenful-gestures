package screenful.gestures;

import com.primesense.nite.HandTrackerFrameRef;
import com.primesense.nite.UserTrackerFrameRef;
import java.util.ArrayList;
import screenful.basic.BonesListener;
import screenful.basic.HandsListener;
import screenful.detectors.ConsecutiveFrames;
import screenful.detectors.Detector;

/**
 * Gesture implements a generic gesture that notifies its listeners when a
 * gesture has been detected for long enough.
 *
 * The gesture conditions are implemented in subclasses of Gesture, in the
 * detected(..) method. This method is checked each frame and detectedFrameCount
 * is increased for every true condition until requiredFrameCount, when
 * listeners are notified that a gesture has been recognized and the counter is
 * set back to zero.
 *
 * The actual implemented gestures may use the hand or user tracker frames as
 * they wish, as long as they implement the detected(..) method so that it
 * returns true when the desired conditions are met.
 *
 * The previous frames are available via the frames object to allow for
 * detectors that compare consecutive frames.
 *
 */
public class Gesture implements HandsListener, BonesListener {

    private int detectedFrameCount;
    private final int requiredFrameCount;
    ConsecutiveFrames frames;
    private long lastGestureTimeStamp;
    private final int cooldownMillis;

    private final Detector detector;

    ArrayList<GestureListener> listeners;

    public Gesture(Detector detector, int framecount, int cooldown) {
        listeners = new ArrayList<>();
        requiredFrameCount = framecount;
        this.detector = detector;
        lastGestureTimeStamp = System.currentTimeMillis();
        cooldownMillis = cooldown;
        frames = new ConsecutiveFrames(null, null, null, null);
    }

    public void addListener(GestureListener listener) {
        listeners.add(listener);
    }

    private void handleFrames(HandTrackerFrameRef handsFrame) {
        frames.previousHandsFrame = frames.handsFrame;
        frames.handsFrame = handsFrame;
        if (frames.previousHandsFrame == null) {
            return;
        }

        if (System.currentTimeMillis() - lastGestureTimeStamp > cooldownMillis) {
            // use the detector to determine if movement was appropriate
            if (detector.detected(frames)) {
                detectedFrameCount += 1;
            } else {
                detectedFrameCount = 0;
            }
            if (detectedFrameCount == requiredFrameCount) {
                notifyListeners();
                detectedFrameCount = 0;
            }
        }
    }

    private void handleFrames(UserTrackerFrameRef bonesFrame) {
        frames.previousBonesFrame = frames.bonesFrame;
        frames.bonesFrame = bonesFrame;
        if (frames.previousBonesFrame == null) {
            return;
        }
        if (System.currentTimeMillis() - lastGestureTimeStamp > cooldownMillis) {
            // use the detector to determine if movement was appropriate
            if (detector.detected(frames)) {
                detectedFrameCount += 1;
            } else {
                detectedFrameCount = 0;
            }
            if (detectedFrameCount == requiredFrameCount) {
                notifyListeners();
                detectedFrameCount = 0;
            }
        }
    }

    @Override
    public void onNewHandsFrame(HandTrackerFrameRef frame) {
        handleFrames(frame);
    }

    @Override
    public void onNewBonesFrame(UserTrackerFrameRef frame) {
        handleFrames(frame);
    }

    private void notifyListeners() {
        for (GestureListener listener : listeners) {
            listener.onGesture(detector.getData());
        }
        lastGestureTimeStamp = System.currentTimeMillis();
    }

}
