package nite2.apps;

import com.primesense.nite.JointType;
import com.primesense.nite.Point3D;
import com.primesense.nite.Skeleton;
import com.primesense.nite.UserData;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nite2.basic.SkeletonTracker;
import static nite2.basic.Utilities.distance3d;

/**
 * Dodgy test program, "click" when hand is near the shoulder (left hand to
 * right shoulder, apparently joints are mirrored). Doesn't work very well up
 * close (or at all).
 */
public class ShoulderClick {

    private static SkeletonTracker tracker;
    private static long clickTimeStamp = 0;

    private static void doClick() {
        System.out.println("" + clickTimeStamp + " -- Click!");
    }

    /**
     * Check if user's hand is closer than 300mm to the shoulder
     */
    private static boolean checkForClick(UserData user) {
        Skeleton skeleton = user.getSkeleton();
        Point3D hand = skeleton.getJoint(JointType.RIGHT_HAND).getPosition();
        Point3D shoulder = skeleton.getJoint(JointType.LEFT_SHOULDER).getPosition();
        return distance3d(hand, shoulder) < 300;
    }

    private static boolean anyUserClicked() {
        List<UserData> users = tracker.getSkeletons();
        for (UserData user : users) {
            if (checkForClick(user)) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - clickTimeStamp > 1000) {
                    clickTimeStamp = System.currentTimeMillis();
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        tracker = new SkeletonTracker();
        tracker.showSkeletonWindow();
        try {
            while (System.in.available() == 0) {
                Thread.sleep(100);
                if (anyUserClicked()) {
                    doClick();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ShoulderClick.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
