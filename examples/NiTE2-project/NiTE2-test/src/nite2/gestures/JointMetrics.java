package nite2.gestures;

import com.primesense.nite.JointType;
import com.primesense.nite.Point3D;
import com.primesense.nite.Skeleton;
import com.primesense.nite.UserData;
import static nite2.gestures.JointMetrics.Side.*;

/**
 * Static methods for getting skeleton measurements etc.
 */
public final class JointMetrics {

    public enum Side {

        LEFT, RIGHT;
    }

    /**
     * Return X distance of elbow and hand, either side
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

    public static boolean handsAboveNeck(UserData user) {
        Skeleton skeleton = user.getSkeleton();
        int lefthandY, righthandY, neckY;
        lefthandY = Math.round(skeleton.getJoint(JointType.LEFT_HAND).getPosition().getY());
        righthandY = Math.round(skeleton.getJoint(JointType.RIGHT_HAND).getPosition().getY());
        neckY = Math.round(skeleton.getJoint(JointType.NECK).getPosition().getY());
        return (lefthandY > neckY) && (righthandY > neckY);
    }
}
