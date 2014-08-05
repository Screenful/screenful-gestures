package screenful.gestures.detectors;

import com.primesense.nite.HandTrackerFrameRef;
import com.primesense.nite.UserTrackerFrameRef;

/**
 * A container object for passing two consecutive hand and user tracker frames.
 *
 */
public class ConsecutiveFrames {

    public HandTrackerFrameRef handsFrame, previousHandsFrame;
    public UserTrackerFrameRef bonesFrame, previousBonesFrame;

    public ConsecutiveFrames(HandTrackerFrameRef handsFrame, HandTrackerFrameRef previousHandsFrame,
            UserTrackerFrameRef bonesFrame, UserTrackerFrameRef previousBonesFrame) {
        this.handsFrame = handsFrame;
        this.previousHandsFrame = previousHandsFrame;
        this.bonesFrame = bonesFrame;
        this.previousBonesFrame = previousBonesFrame;
    }
}
