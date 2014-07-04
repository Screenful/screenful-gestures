package screenful.gestures;

import com.primesense.nite.JointType;
import com.primesense.nite.Point3D;
import com.primesense.nite.Skeleton;
import com.primesense.nite.UserData;
import static screenful.gestures.JointMetrics.Side.*;

/**
 * Static methods for getting skeleton measurements etc.
 */
public final class JointMetrics {

    public enum Side {

        LEFT, RIGHT;
    }

    /**
     * Calculate euclidean distance between two joints in space
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

    /**
     * Return X distance of elbow and hand, either side
     *
     * @param user user data object
     * @param side Side.LEFT or Side.RIGHT
     * @return euclidean distance between elbow and hand in 3D space
     */
    public static double elbowHandXOffset(UserData user, Side side) {
        Skeleton skeleton = user.getSkeleton();
        Point3D elbow, hand;
        if (side == RIGHT) {
            elbow = skeleton.getJoint(JointType.RIGHT_ELBOW).getPosition();
            hand = skeleton.getJoint(JointType.RIGHT_HAND).getPosition();
        } else if (side == LEFT) {
            elbow = skeleton.getJoint(JointType.LEFT_ELBOW).getPosition();
            hand = skeleton.getJoint(JointType.LEFT_HAND).getPosition();
        } else {
            return 0;
        }
        return Math.round((float) elbow.getX()) - Math.round((float) hand.getX());
    }
}
