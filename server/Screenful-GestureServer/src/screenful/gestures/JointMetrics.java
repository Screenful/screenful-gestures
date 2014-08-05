package screenful.gestures;

import com.primesense.nite.JointType;
import com.primesense.nite.Skeleton;
import com.primesense.nite.UserData;

/**
 * Static methods for getting skeleton measurements etc.
 */
public final class JointMetrics {

    public enum Side {

        LEFT, RIGHT;
    }

    /**
     * Calculate euclidean distance between two joints of a user in space
     *
     * @param user user data
     * @param from the first joint, eg. JointType.RIGHT_HAND
     * @param to the second joint, eg. JointType.NECK
     * @return distance in millimeters
     */
    public static double jointToJointDistance(UserData user, JointType from, JointType to) {
        Skeleton skeleton = user.getSkeleton();
        return Utilities.distance3d(skeleton.getJoint(from).getPosition(), skeleton.getJoint(to).getPosition());
    }
}
