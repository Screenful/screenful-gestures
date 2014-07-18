package screenful.examples.tracker;

import com.primesense.nite.JointType;
import com.primesense.nite.UserData;
import com.primesense.nite.UserTrackerFrameRef;
import screenful.basic.BonesListener;
import screenful.gestures.JointMetrics;

/**
 * Listen for bone frames (when added to a NiTETracker's listeners) and do
 * something with them, in this case print the distance between the first
 * detected user's hands if the distance is less than 50 cm.
 *
 */
public class HandSeparation implements BonesListener {

    @Override
    public void onNewBonesFrame(UserTrackerFrameRef frame) {
        if (frame.getUsers().isEmpty()) {
            return;
        }
        // get first user's data
        UserData firstUser = frame.getUsers().get(0);
        double dist = JointMetrics.jointToJointDistance(firstUser, JointType.LEFT_HAND, JointType.RIGHT_HAND);
        if (dist > 500) {
            System.out.println("" + dist);

        }
    }
}
