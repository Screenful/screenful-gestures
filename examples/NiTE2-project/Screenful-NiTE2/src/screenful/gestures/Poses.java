package screenful.gestures;

import com.primesense.nite.JointType;
import com.primesense.nite.Skeleton;
import com.primesense.nite.UserData;
import static screenful.gestures.JointMetrics.jointToJointDistance;

/**
 * Some basic poses that can be calculated in a single frame based on user data.
 */
public class Poses {

    /**
     * "Click" by essentially clapping the hands in front of the face or
     * otherwise bringing them close enough
     *
     * @param user user data
     * @return true if hands were above the neck and distance between the hands
     * was less than 150 mm
     */
    public static boolean dorkyClick(UserData user) {
        return handsAboveNeck(user) && jointToJointDistance(user, JointType.LEFT_HAND, JointType.RIGHT_HAND) < 150;
    }

    /**
     * Returns true if a user's both hands are above the neck..
     *
     * @param user user data object
     * @return true if hands above neck
     */
    public static boolean handsAboveNeck(UserData user) {
        Skeleton skeleton = user.getSkeleton();
        int lefthandY, righthandY, neckY;
        lefthandY = Math.round(skeleton.getJoint(JointType.LEFT_HAND).getPosition().getY());
        righthandY = Math.round(skeleton.getJoint(JointType.RIGHT_HAND).getPosition().getY());
        neckY = Math.round(skeleton.getJoint(JointType.NECK).getPosition().getY());
        return (lefthandY > neckY) && (righthandY > neckY);
    }
}
